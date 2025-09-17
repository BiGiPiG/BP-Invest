<template>
    <div class="app-root">
      <HeaderBar @search="handleSearch" />
      <div class="main_container">
        <SideBar />
        <main>
          <InfoPanel :ticker="info.ticker" :company="info.company" :metrics="info.metrics" :marketCap="info.marketCap" />
          <ChartPanel :chartData="data" />
          <AnalysisPanel
            :title="analysis.title"
            :description="analysis.description"
            :score="analysis.score"
            :pros="analysis.pros"
            :cons="analysis.cons"
          />
        </main>
      </div>
    </div>
  </template>

  <script setup>
  import { reactive } from 'vue';

  import HeaderBar from '../components/MainPage/HeaderBar.vue';
  import SideBar from '../components/MainPage/SideBar.vue';
  import AnalysisPanel from '../components/MainPage/AnalysisPanel.vue';
  import InfoPanel from '../components/MainPage/InfoPanel.vue';
  import ChartPanel from '../components/MainPage/ChartPanel.vue';

  function authHeaders() {
    const token = localStorage.getItem('token');
    return token ? { Authorization: `Bearer ${token}` } : {};
  }

  const analysis = reactive({
    title: 'AI Analysis',
    description:
      'Apple inc. has a strong market position and solid financials. making it an attractive investment.',
    score: '7/10 - moderately attractive',
    pros: ['Strong market position'],
    cons: ['High P/S ratio'],
  });

  const info = reactive({
    ticker: 'AAPL',
    company: 'Apple Inc.',
    marketCap: '2.4T',
    metrics: [
      { type: 'P/E', value: '28.7' },
      { type: 'P/S', value: '7.5' },
      { type: 'P/B', value: '40.1' },
      { type: 'EPS', value: '6.21' },
    ],
  });

  const data = reactive({
    dates: [],
    values: []
  });

  const testData = {
    dates: ['2024-01-01', '2024-01-02', '2024-01-03', '2024-01-04', '2024-01-05', '2024-01-06', '2024-01-07', '2024-01-08', '2024-01-09', '2024-01-10', '2024-01-11', '2024-01-12'],
    values: [150, 152, 148, 155, 158, 160, 162, 159, 165, 168, 170, 172]
  };

  data.dates = testData.dates;
  data.values = testData.values;

  async function handleSearch(ticker) {
    if (!ticker) return;
    console.log("App get: " + ticker);
    updateMainInfo(ticker);
    updateChart(ticker);
    updateAnalys(ticker);
  }

  async function updateMainInfo(ticker) {

    try {
      const res = await fetch(`/bp-invest/api/v1/main-info?ticker=${ticker}`, {
        headers: {
          ...authHeaders()
        }
      });
      if (!res.ok) throw new Error('Ошибка получения информации о компании');

      const data = await res.json();
      console.log(data);

      info.ticker = data.ticker ?? data.Symbol ?? cleanedTicker;
      info.company = data.name ?? data.Name ?? '-';
      info.marketCap = data.market_cap ?? data.MarketCapitalization ?? '-';

      info.metrics = [
          { type: 'EPS', value: data.EPS ?? data.eps ?? '-' },
          { type: 'P/E', value: data.PERatio ?? data['P/E'] ?? '-' },
          { type: 'P/S', value: data.PriceToSalesRatioTTM ?? data['P/S'] ?? '-' },
          { type: 'P/B', value: data.PriceToBookRatio ?? data['P/B'] ?? '-' }
      ].filter(metric => metric.value !== '-');
    } catch (err) {
      console.error('Ошибка:', err);
      info.ticker = cleanedTicker;
      info.company = '-';
      info.marketCap = '-';
      info.metrics = [];
    }
  }

  async function updateChart(ticker) {
    try {
      const res = await fetch(`/bp-invest/api/v1/chart-info?ticker=${ticker}`, {
        headers: {
          ...authHeaders()
        }
      });
      if (!res.ok) throw new Error('Ошибка получения информации о компании');

      const chartJson = await res.json();
      console.log('Chart data received:', chartJson);

      if (Array.isArray(chartJson) && chartJson.length > 0) {
        data.dates = chartJson.map(item => item.day);
        data.values = chartJson.map(item => item.val);
        console.log('Chart data updated:', { dates: data.dates, values: data.values });
      } else {
        console.warn('No chart data received, keeping test data');
      }
    } catch (err) {
      console.error('Error fetching chart data:', err);
    }
  }

  async function updateAnalys(ticker) {
    try {
      const res = await fetch(`/bp-invest/api/v1/ai-analyse?ticker=${ticker}`, {
        headers: {
          ...authHeaders()
        }
      });
      if (!res.ok) throw new Error('Ошибка анализа');

      const data = await res.json();
      console.log(data);

      analysis.title = 'AI Analysis';
      analysis.description = data.overallAssessment ?? '';
      analysis.score = data.rating ?? '';
      analysis.pros = data.pros ?? [];
      analysis.cons = data.cons ?? [];
    } catch (err) {
      console.error(err);
    }
  }

  </script>

  <style>
  html, body {
    margin: 0;
    padding: 0;
    height: 100%;
    background-color: #151A27;
  }

  .app-root {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }

  header {
    display: flex;
    width: 100%;
    height: 60px;
    background-color: #151A27;
    flex-shrink: 0;
  }

  .main_container {
    display: flex;
    flex: 1;
    height: calc(100% - 60px);
  }

  aside {
    width: 15%;
    color: #a0a0a0;
    display: flex;
    flex-direction: column;
  }

  main {
    width: 85%;
    padding: 1%;
    background-color: #1F2331;
    margin: 20px;
    border-radius: 10px;
    color: #a0a0a0;
    display: grid;
    flex: 1;
    gap: 20px;
    grid-template-areas:
      "A C"
      "B C";
    grid-template-rows: 1fr 1fr;
    grid-template-columns: 1fr 1fr;
    resize: none;
    overflow: hidden;
  }
  </style>
