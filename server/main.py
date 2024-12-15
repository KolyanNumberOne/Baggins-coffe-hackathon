from flask import Flask, request, jsonify
import waitress

from product_rec import Recommend
from recommendations import get_customer_info, generate_recommendations

app = Flask(__name__)


class Recommendation:
    def __init__(self, title, body):
        self.title = title
        self.body = body

    def to_dict(self):
        """Метод для сериализации объекта в словарь."""
        return {
            "title": self.title,
            "body": self.body
        }


@app.route('/recommendations', methods=['GET'])
def get_recommendations():
    try:
        user_id = request.args.get('id')
        try:
            user_id = int(user_id)
        except (TypeError, ValueError):
            return jsonify({"error": "Invalid 'id' parameter, must be an integer"}), 400

        customer_data = get_customer_info(user_id)

        if customer_data is None:
            return jsonify({"error": f"Customer with ID {user_id} not found"}), 404


        popular_product = recommendation_system.most_popular_product(user_id)
        popular_product = str(popular_product)

        recommended_items = recommendation_system.recommend_items(user_id, top_n=5)
        recommended_items = ", ".join(map(str, recommended_items))

        raw_recommendations = generate_recommendations(customer_data)
        recommendations = [
            Recommendation(title, body) for title, body in raw_recommendations.items()
        ]

        all_recommendations = []
        all_recommendations.append({
            "title": "Самый популярный товар для пользователя",
            "body": popular_product
        })

        all_recommendations.append({
            "title": "Рекомендации к любимому товару пользователя",
            "body": recommended_items
        })

        for rec in recommendations:
            all_recommendations.append(rec.to_dict())

        return jsonify(all_recommendations), 200

    except Exception as e:
        print("Ошибка", str(e))
        return jsonify({"error": "An error occurred", "details": str(e)}), 500


if __name__ == '__main__':
    recommendation_system = Recommend(path='hakaton.csv', sep=';')
    print("Starting server on http://0.0.0.0:8080")
    waitress.serve(app, host='0.0.0.0', port=8080)
