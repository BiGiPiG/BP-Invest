FROM python:3.13-alpine

WORKDIR /app

COPY requirements.txt ./
RUN python -m venv .venv \
&& source .venv/bin/activate \
&& pip install --no-cache-dir -r requirements.txt

COPY server.py ./

CMD ["/app/.venv/bin/python", "./server.py"]