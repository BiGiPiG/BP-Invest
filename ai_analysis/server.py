from g4f.client import Client
from flask import Flask, request, jsonify
from g4f.typing import Message

app = Flask(__name__)

client = Client()

GPT_PROMPT = """
    Analyze the stock of {symbol} and provide a brief investment assessment.
    
    ### Required response structure
    1. Overall Assessment: Give a short summary (2–3 sentences) of the company's investment attractiveness.
    
    2. Attractiveness Rating: Express the final rating in the format X/10 (where 10 means highly attractive investment).
    
    3. Pros: List the key strengths of the company (1–3 items).
    
    4. Cons: List the main weaknesses or risks of the company (1–3 items).
    
    ### Consider:
    - The company's financial condition (e.g., P/E, P/S, P/B, EPS, market capitalization).
    - Market position and industry factors.
    - Current multipliers and their impact on the assessment.
    
    Response format:
    - Concise and to the point.
    - List pros and cons in one line, separated by commas.
    - Start each item on a new line.
    
    Example output:
    "Description: Apple Inc. has a strong market position and solid financials, making it attractive for investment.
     Rating: 7 - moderately attractive
    
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
    app.run(host='0.0.0.0', port=9000, debug=True)