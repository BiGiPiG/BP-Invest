<template>
  <div class="app-root">
    <header>
      <HeaderBar @search="handleSearch" />
    </header>
    <div class="main-container">
      <aside>
        <SideBar :trackedShares="info.trackedShares"/>
      </aside>
      <main class="main-content" :class="{ active: isMainInfo || isAnalyse || isChart }">

        <ul class="switcher">
          <li @click="switchToMainInfo" :class="{ active: isMainInfo }">
            <span class="label">Main info</span>
          </li>
          <li @click="switchToChart" :class="{ active: isChart }">
            <span class="label">Chart</span>
          </li>
          <li @click="switchToAnalyse" :class="{ active: isAnalyse }">
            <span class="label">AI analysis</span>
          </li>
        </ul>

        <div class="content-container">
          <div v-if="isMainInfo" class="content-section">
            <InfoPanel
                class="info-panel"
                :ticker="info.ticker"
                :company="info.company"
                :metrics="info.metrics"
                :marketCap="info.marketCap"
                :isLoading="info.isLoading"
            />
          </div>
          <div v-if="isChart" class="content-section">
            <ChartPanel
                class="chart-panel"
                :chartData="data"
                :isLoading="data.isLoading"
            />
          </div>
          <div v-if="isAnalyse" class="content-section">
            <AnalysisPanel
                class="analysis-panel"
                :title="analysis.title"
                :description="analysis.description"
                :score="analysis.score"
                :pros="analysis.pros"
                :cons="analysis.cons"
                :isLoading="analysis.isLoading"
            />
          </div>
        </div>

      </main>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue';

import HeaderBar from '../components/MainPage/HeaderBar.vue';
import SideBar from '../components/MainPage/SideBar.vue';
import AnalysisPanel from '../components/MainPage/AnalysisPanel.vue';
import InfoPanel from '../components/MainPage/InfoPanel.vue';
import ChartPanel from '../components/MainPage/ChartPanel.vue';
import router from "../router/index.js";

// tab management
const isMainInfo = ref(true);
const isChart = ref(false);
const isAnalyse = ref(false);

function authHeaders() {
  const token = localStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : {};
}

const info = reactive({
  ticker: 'EXAMPLE',
  company: 'Search for a company',
  marketCap: 'Waiting for input...',
  metrics: [
    { type: 'P/E', value: '---' },
    { type: 'P/S', value: '---' },
    { type: 'P/B', value: '---' },
    { type: 'EPS', value: '---' },
  ],
  trackedShares: [
      'IBM',
      'Apple Inc.',
      'Google'
  ],
  isLoading: false
});

const analysis = reactive({
  title: 'Welcome to Stock Analyzer',
  description: 'Use the search bar to analyze any stock. Get instant AI-powered insights, financial metrics, and interactive charts.',
  score: 'Search to see rating',
  pros: [
    'Real-time financial data',
    'AI-powered analysis',
    'Interactive charts',
    'Comprehensive metrics'
  ],
  cons: [
    'Select a stock to begin analysis'
  ],
  isLoading: false
});

const data = {
  dates: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'],
  values: [100, 105, 102, 108, 110]
};

const testData = {
  dates: ['2024-01-01', '2024-01-02', '2024-01-03', '2024-01-04', '2024-01-05', '2024-01-06', '2024-01-07', '2024-01-08', '2024-01-09', '2024-01-10', '2024-01-11', '2024-01-12'],
  values: [150, 152, 148, 155, 158, 160, 162, 159, 165, 168, 170, 172]
};

data.dates = testData.dates;
data.values = testData.values;

async function handleSearch(ticker) {
  if (!ticker) return;
  updateMainInfo(ticker);
  updateChart(ticker);
  updateAnalys(ticker);
}

async function updateMainInfo(ticker) {
  info.isLoading = true;
  const cleanedTicker = ticker.trim().toUpperCase();

  try {
    const res = await fetch(`/bp-invest/api/v1/main-info?ticker=${cleanedTicker}`, {
      headers: {
        ...authHeaders()
      }
    });
    if (res.status === 403) {
      console.log("exchange");
      localStorage.removeItem('token');
      await router.push('/login');
      return;
    }
    if (!res.ok) {
      throw new Error('Ошибка получения информации о компании');
    }

    const data = await res.json();

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
  info.isLoading = false;
}

async function updateChart(ticker) {
  data.isLoading = true;
  const cleanedTicker = ticker.trim().toUpperCase();

  try {
    const res = await fetch(`/bp-invest/api/v1/chart-info?ticker=${cleanedTicker}`, {
      headers: {
        ...authHeaders()
      }
    });
    if (res.status === 403) {
      console.log("exchange");
      localStorage.removeItem('token');
      await router.push('/login');
      return;
    }
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
  data.isLoading = false;
}

async function updateAnalys(ticker) {
  analysis.isLoading = true;
  const cleanedTicker = ticker.trim().toUpperCase();

  try {
    const res = await fetch(`/bp-invest/api/v1/ai-analyse?ticker=${cleanedTicker}`, {
      headers: {
        ...authHeaders()
      }
    });
    if (res.status === 403) {
      console.log("exchange");
      localStorage.removeItem('token');
      await router.push('/login');
      return;
    }
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

  analysis.isLoading = false;
}

function switchToMainInfo() {
  isMainInfo.value = true;
  isChart.value = false;
  isAnalyse.value = false;
}

function switchToChart() {
  isMainInfo.value = false;
  isChart.value = true;
  isAnalyse.value = false;
}

function switchToAnalyse() {
  isMainInfo.value = false;
  isChart.value = false;
  isAnalyse.value = true;
}

</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
  background-color: #0f1320;
  color: #e0e0e0;
  font-family: "Inter", sans-serif;
  line-height: 1.6;
}

.app-root {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header {
  display: flex;
  width: 100%;
  height: 70px;
  background: linear-gradient(90deg, #151A27 0%, #1a2238 100%);
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 10;
}

.main-container {
  display: flex;
  flex: 1;
  width: 100%;
  padding: 20px;
  gap: 20px;
}

aside {
  width: 250px;
  background: linear-gradient(180deg, #151A27 0%, #1a2238 100%);
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  flex-shrink: 0;
  padding: 20px 0;
}

.main-content {
  flex: 1;
  background: linear-gradient(145deg, #1a2238 0%, #151A27 100%);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.switcher {
  display: flex;
  list-style: none;
  background-color: #151A27;
  border-bottom: 1px solid #2a324b;
  margin: 0;
  padding: 0;
  flex-shrink: 0; /* Добавлено: предотвращает сжатие switcher */
}

.switcher > li {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  padding: 16px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  gap: 8px;
  position: relative;
  overflow: hidden;
}

.switcher > li:hover {
  background-color: rgba(35, 53, 97, 0.4);
}

.switcher > li.active {
  background-color: #233561;
  color: #fff;
}

.switcher > li.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, #4a6bff, #38b6ff);
}

.switcher > li .icon {
  font-size: 18px;
}

.switcher > li .label {
  font-family: "Inter",serif;
  font-size: 2rem;
  font-weight: 700;
  line-height: 1.2;
  color: #fff;
  margin: 0;
  background-clip: text;
}

.content-container {
  flex: 1; /* Изменено: теперь занимает все доступное пространство */
  padding: 24px;
  overflow-y: auto;
  display: flex; /* Добавлено */
}

.content-section {
  animation: fadeIn 0.4s ease;
  flex: 1; /* Добавлено: занимает все пространство контейнера */
  display: flex; /* Добавлено */
  flex-direction: column; /* Добавлено */
}

/* Стили для дочерних компонентов, чтобы они тоже занимали все пространство */
.info-panel,
.chart-panel,
.analysis-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Адаптивность */
@media (max-width: 1200px) {
  .main-container {
    flex-direction: column;
    padding: 15px;
  }

  aside {
    width: 100%;
    margin-bottom: 15px;
  }
}

@media (max-width: 768px) {
  .switcher {
    flex-direction: column;
  }

  .switcher > li {
    padding: 12px 15px;
    justify-content: flex-start;
  }

  .content-container {
    padding: 16px;
  }

  .main-container {
    padding: 10px;
    gap: 10px;
  }
}

@media (max-width: 480px) {
  header {
    height: 60px;
  }

  .switcher > li {
    padding: 10px 12px;
  }

  .switcher > li .icon {
    font-size: 16px;
  }

  .switcher > li .label {
    font-size: 14px;
  }

  .content-container {
    padding: 12px;
  }
}

.content-container::-webkit-scrollbar {
  width: 8px;
}

.content-container::-webkit-scrollbar-track {
  background: #151a27;
  border-radius: 4px;
}

.content-container::-webkit-scrollbar-thumb {
  background: #2a324b;
  border-radius: 4px;
}

.content-container::-webkit-scrollbar-thumb:hover {
  background: #233561;
}
</style>