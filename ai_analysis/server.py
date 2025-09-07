from g4f.client import Client
from flask import Flask, request, jsonify
from g4f.typing import Message

app = Flask(__name__)

client = Client()

GPT_PROMPT = """
    Проанализируй акцию {symbol} и предоставь краткую инвестиционную оценку.
    
    ### Требуемая структура ответа
    1. Общая оценка: Дай короткое резюме (2-3 предложения) об инвестиционной привлекательности компании.
    
    2. Оценка привлекательности: Вырази итоговую оценку в формате X/10 (где 10 — максимально привлекательная инвестиция).
    
    3. Плюсы (Pros): Укажи ключевые сильные стороны компании (1–3 пункта).
    
    4. Минусы (Cons): Укажи ключевые слабые стороны или риски (1–3 пункта).
    
    ### Учитывай:
    - Финансовое состояние компании (например, P/E, P/S, P/B, EPS, рыночная капитализация).
    - Рыночную позицию и отраслевые факторы.
    - Текущие мультипликаторы и их влияние на оценку.
    
    Формат ответа:
    - Кратко и по делу.
    - Списки плюсов и минусов оформи через запятую в одну строчку.
    - Каждый пункт начинай с новой строки
    
    Образец вывода:
    "Описание: Apple inc. имеет сильные рыночные позиции и устойчивые финансовые показатели, что делает её привлекательной для инвестиций.
     Оценка: 7 - умеренно привлекательна
    
    Pros: pros1, pros2, ...
    
    Cons: cons1, cons2, ..."
    """

def build_prompt(symbol: str) -> str:
    return GPT_PROMPT.format(symbol=symbol)

@app.route('/analyze', methods=['POST'])
def analyze():
    data = request.get_json()
    symbol = data.get('symbol')

    if not symbol:
        return jsonify({"error": "Missing 'symbol' parameter"}), 400

    prompt = build_prompt(symbol)

    try:
        messages = [Message(role="user", content=prompt)]

        response = client.chat.completions.create(
            model="gpt-4.1",
            messages=messages,
            web_search=False
        )
        analysis = response.choices[0].message.content
        return jsonify({"symbol": symbol, "analysis": analysis})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=9000, debug=True)