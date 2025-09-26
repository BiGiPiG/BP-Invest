from g4f.client import Client
from flask import Flask, request, jsonify
from g4f.typing import Message

app = Flask(__name__)

client = Client()

prompt = """
    Analyze the stock of {symbol} and provide a structured investment assessment.
    
    RESPONSE MUST FOLLOW THIS EXACT FORMAT:

    Overall Assessment: [2-3 sentence summary of investment attractiveness]
    Attractiveness Rating: [X/10 where X is a number and short conclusion (2-3 words)]
    Pros: [comma-separated list of 1-3 strengths(maximum 3)]
    Cons: [comma-separated list of 1-3 weaknesses(maximum 3)]
    
    Consider financial metrics, market position, and industry factors.
    """

def build_prompt(symbol: str) -> str:
    return prompt.format(symbol=symbol)

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
    app.run(host='0.0.0.0', port=9000, debug=True)