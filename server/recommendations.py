from typing import Optional
import pandas as pd
from typing import Dict



def get_customer_info(customer_id: int) -> Optional[pd.Series]:
    file_path = 'sorted_customers.csv'
    df = pd.read_csv(file_path)
    df = df.drop(columns=['purchases_per_week'])
    """
    Получает информацию о клиенте по его customer_id.

    :param customer_id: ID клиента
    :return: Строка из DataFrame с данными клиента или None, если ID не найден
    """
    customer_row = df[df['customer_id'] == customer_id]
    return customer_row.iloc[0] if not customer_row.empty else None

def generate_recommendations(customer_data: Dict) -> Dict:
    """
    Генерирует рекомендации для пользователя на основе его данных.

    :param customer_data: Словарь с характеристиками пользователя
    :return: Словарь с рекомендациями
    """
    recommendations = {}

    # Определяем стратегию в зависимости от классификации клиента
    if customer_data['customer_classification'] == 'Редкий':
        recommendations['Акции для редких клиентов'] = "Вы делаете покупки нечасто. Получите подарок к следующему заказу, чтобы сделать процесс приятнее!"

    elif customer_data['customer_classification'] == 'Новый':
        recommendations["Акции для Новых клиентов"] = "Добро пожаловать! Мы рады видеть вас среди наших клиентов. Получите скидку 10% на первую покупку."

    elif customer_data['customer_classification'] == 'Регулярный':
        recommendations["Акции для Регулярных клиентов"] = f"Спасибо за вашу преданность! Получите скидку на ваш любимый товар{customer_data['favority_entity_id']}!"

    elif customer_data['customer_classification'] == 'Постоянных':
        recommendations["Акции для Постоянных клиентов"] = f"Мы ценим вашу лояльность! Наслаждайтесь эксклюзивной акцией 5ое кофе в подарок. Только до конца {customer_data['most_frequent_season']}"

    # Рекомендации на основе среднего количества товаров в заказе
    if customer_data['avg_products_per_order'] < 2:
        recommendations['Комплект со скидкой'] = 'Предложите комплект товаров со скидкой.'

    '''# Используем время и сезон для таргетированных предложений
    if customer_data['most_frequent_time'] == 1:
        recommendations['time_based_offer'] = 'Утренние скидки: заказывайте до 12:00 и получите бонусы!'
    if customer_data['most_frequent_season'] == 2:
        recommendations['seasonal_offer'] = 'Весенняя распродажа только для вас!'''

    # Реактивация неактивных клиентов
    if customer_data['days_since_last_purchase'] > 365:
        recommendations['Скидка за возвращение'] = 'Мы скучаем по вам! Возвращайтесь и получите 30% скидку на любимый товар.'


    return recommendations
