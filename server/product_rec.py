import pandas as pd
import plotly.express as px
from sklearn.metrics.pairwise import cosine_similarity


class Recommend:

    def __init__(self, path: str, sep: str):

        self.df = pd.read_csv(path, sep=sep)
        self.df['create_datetime'] = pd.to_datetime(self.df['create_datetime'], dayfirst=True)

        self.data_pivot = self.df.pivot_table(index=['order_id'], values='entity_id', aggfunc=['count'])
        self.data_pivot.columns = ['count']
        self.data_pivot = self.data_pivot[self.data_pivot['count'] > 1]

        # Индексы из data_pivot — это order_id с более чем 1 товаром
        valid_orders = self.data_pivot.index
        # Фильтрация основного DataFrame
        filtered_df = self.df[self.df['order_id'].isin(valid_orders)]

        # Данные: сгруппированные заказы
        grouped = filtered_df.groupby('order_id')['entity_id'].apply(list)

        # Матрица "товар × заказ"
        transactions = grouped.values.tolist()
        unique_items = sorted(set(item for sublist in transactions for item in sublist))

        item_order_matrix = pd.DataFrame(0, index=unique_items, columns=grouped.index)
        for order_id, items in zip(grouped.index, transactions):
            item_order_matrix.loc[items, order_id] = 1

        # Матрица "товар × товар" (сходство по косинусной метрике)
        item_similarity = cosine_similarity(item_order_matrix)

        # Преобразуем в DataFrame для удобства работы
        self.item_similarity_df = pd.DataFrame(item_similarity, index=unique_items, columns=unique_items)

    def most_popular_product(self, customer_id: int):
        df_customer = self.df[self.df['customer_id'] == customer_id]
        customer_pivot = df_customer.pivot_table(index=['entity_id'], values='order_id', aggfunc=['count'])
        customer_pivot.columns = ['count']
        customer_pivot = customer_pivot['count'].sort_values(ascending=False)

        return int(customer_pivot.index[0])

    def most_popular_product_gist(self, customer_id: int):

        # Фильтруем данные по customer_id
        df_customer = self.df[self.df['customer_id'] == customer_id]

        # Группируем данные по entity_id и считаем количество заказов
        grouped = df_customer['entity_id'].value_counts().reset_index()
        grouped.columns = ['entity_id', 'count']

        # Создаем гистограмму
        fig = px.bar(
            grouped,
            x='entity_id',
            y='count',
            text='entity_id',  # Добавляем текст (id товаров)
            labels={'count': 'Количество заказов', 'entity_id': 'ID товара'}  # Настройка осей
        )

        # Настраиваем отображение текста
        fig.update_traces(textposition='outside')  # Текст снаружи столбцов

        # Показываем график
        fig.show()

    # Функция для получения рекомендаций
    def recommend_items(self, customer_id, top_n=5):

        item_id = self.most_popular_product(customer_id)

        if item_id not in self.item_similarity_df.index:
            return f"Товар с ID {item_id} не найден."

        # Сортируем товары по убыванию схожести
        similar_items = self.item_similarity_df[item_id].sort_values(ascending=False)

        # Исключаем сам товар из списка рекомендаций
        recommendations = similar_items.drop(labels=[item_id]).head(top_n)

        return [int(item) for item in recommendations.index]

    # Функция для построения графика
    def plot_recommendations(self, customer_id):
        top_recommendations = self.recommend_items(customer_id)
        # Создаем гистограмму
        fig = px.bar(
            x=top_recommendations.index.values, y=top_recommendations.values
        )
        # Настраиваем отображение текста
        fig.update_traces(textposition='outside')  # Текст снаружи столбцов
        # Показываем график
        fig.show()

    def return_head(self):
        return self.df.head()
