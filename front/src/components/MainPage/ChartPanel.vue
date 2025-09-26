<template>
  <div v-if="isLoading" class="chart-spinner">
    <div class="spinner-container">
      <div class="spinner"></div>
      <p class="loading-text">Generating Chart...</p>
    </div>
  </div>
  <div v-else class="board" id="chart">
    <div class="chart-header">
      <div class="chart-title">Price Chart</div>
      <ul class="period-selector">
        <li
            v-for="period in periods"
            :key="period"
            :class="{ active: currentPeriod === period }"
            @click="setPeriod(period)"
            class="period-item"
        >
          {{ period }}
        </li>
      </ul>
    </div>

    <div class="chart-container">
      <canvas id="acquisitions"></canvas>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch, nextTick } from 'vue';
import Chart from 'chart.js/auto';

const currentPeriod = ref('1M');
const periods = ['1W', '1M', '1Y'];
let chartInstance = null;
let datasetsByPeriod = {
  '1W': { labels: [], values: [] },
  '1M': { labels: [], values: [] },
  '1Y': { labels: [], values: [] }
};

const props = defineProps({
  chartData: {
    type: Object,
    default: () => ({ dates: [], values: [] })
  },
  isLoading: false
});

function setPeriod(period) {
  if (!datasetsByPeriod[period] || !chartInstance) {
    currentPeriod.value = period;
    return;
  }
  currentPeriod.value = period;
  const ds = datasetsByPeriod[period];
  chartInstance.data.labels = ds.labels;
  chartInstance.data.datasets[0].data = ds.values;
  chartInstance.update('none');
}

function updateDatasets() {
  const allLabels = props.chartData.dates || [];
  const allValues = props.chartData.values || [];

  const sliceLast = (arr, n) => (n > 0 ? arr.slice(-n) : arr.slice());
  datasetsByPeriod = {
    '1W': { labels: sliceLast(allLabels, 7), values: sliceLast(allValues, 7) },
    '1M': { labels: sliceLast(allLabels, 30), values: sliceLast(allValues, 30) },
    '1Y': { labels: allLabels, values: allValues }
  };
}

function createChart() {
  const canvas = document.getElementById('acquisitions');
  if (!canvas) return;

  if (chartInstance) {
    chartInstance.destroy();
  }

  const context = canvas.getContext('2d');

  const gradient = context.createLinearGradient(0, 0, 0, 400);
  gradient.addColorStop(0, 'rgba(61, 237, 205, 0.3)');
  gradient.addColorStop(0.7, 'rgba(61, 237, 205, 0.1)');
  gradient.addColorStop(1, 'rgba(61, 237, 205, 0.01)');

  const lineGradient = context.createLinearGradient(0, 0, 0, 400);
  lineGradient.addColorStop(0, '#3DEDCD');
  lineGradient.addColorStop(1, '#2bb7a9');

  chartInstance = new Chart(canvas, {
    type: 'line',
    options: {
      responsive: true,
      maintainAspectRatio: true,
      layout: {
        padding: {
          top: 20,
          right: 20,
          bottom: 10,
          left: 20
        }
      },
      scales: {
        x: {
          display: true,
          grid: {
            display: false,
            drawBorder: false
          },
          ticks: {
            display: false
          }
        },
        y: {
          display: true,
          grid: {
            color: 'rgba(255, 255, 255, 0.05)',
            drawBorder: false
          },
          ticks: {
            color: '#93A0AF',
            font: {
              family: 'Inter',
              size: 11
            },
            callback: function(value) {
              return '$' + value.toLocaleString();
            }
          }
        }
      },
      elements: {
        point: {
          radius: 0,
          hoverRadius: 6,
          hoverBackgroundColor: '#3DEDCD',
          hoverBorderColor: '#fff',
          hoverBorderWidth: 2
        },
        line: {
          tension: 0.4,
          borderWidth: 3
        }
      },
      interaction: {
        intersect: false,
        mode: 'index'
      },
      plugins: {
        legend: { display: false },
        tooltip: {
          enabled: true,
          backgroundColor: 'rgba(26, 31, 46, 0.95)',
          titleColor: '#fff',
          bodyColor: '#e0e3eb',
          borderColor: 'rgba(61, 237, 205, 0.3)',
          borderWidth: 1,
          padding: 12,
          cornerRadius: 8,
          displayColors: false,
          callbacks: {
            label: function(context) {
              return `Price: $${context.parsed.y.toLocaleString()}`;
            }
          }
        }
      }
    },
    data: {
      labels: datasetsByPeriod[currentPeriod.value].labels,
      datasets: [
        {
          label: 'Price',
          data: datasetsByPeriod[currentPeriod.value].values,
          backgroundColor: gradient,
          borderColor: lineGradient,
          fill: true,
          pointHoverBackgroundColor: '#3DEDCD',
          pointHoverBorderColor: '#fff',
          pointHoverBorderWidth: 2
        }
      ]
    }
  });
}

watch(() => props.chartData, (newData) => {
  if (newData && newData.dates && newData.values) {
    updateDatasets();
    if (chartInstance) {
      chartInstance.data.labels = datasetsByPeriod[currentPeriod.value].labels;
      chartInstance.data.datasets[0].data = datasetsByPeriod[currentPeriod.value].values;
      chartInstance.update();
    } else {
      createChart();
    }
  }
}, { deep: true });

onMounted(async () => {
  await nextTick();
  updateDatasets();
  createChart();
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

.board {
  padding: 2.5rem;
  background: linear-gradient(135deg, #1a1f2e 0%, #242a3d 100%);
  border-radius: 16px;
  box-shadow:
      0 12px 40px rgba(0, 0, 0, 0.4),
      inset 0 1px 0 rgba(255, 255, 255, 0.05);
  font-family: "Inter", sans-serif;
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  height: fit-content;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.chart-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #fff;
  background: linear-gradient(135deg, #fff 0%, #b0b5c3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
}

.period-selector {
  display: flex;
  gap: 0.8rem;
  list-style: none;
  margin: 0;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  padding: 0.4rem;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.period-item {
  padding: 0.6rem 1.2rem;
  color: #93A0AF;
  cursor: pointer;
  border-radius: 8px;
  font-weight: 500;
  font-size: 0.9rem;
  transition: all 0.2s ease;
  min-width: 50px;
  text-align: center;
}

.period-item:hover {
  color: #ffffff;
  background: rgba(255, 255, 255, 0.05);
}

.period-item.active {
  color: #0f141f;
  background: linear-gradient(135deg, #3DEDCD 0%, #2bb7a9 100%);
  box-shadow: 0 4px 12px rgba(61, 237, 205, 0.3);
  font-weight: 600;
}

.chart-container {
  width: 100%;
  height: 100%;
  min-height: 300px;
  position: relative;
  padding: 0;
  margin: 0;
}

.chart-container canvas {
  width: 100% !important;
  height: 100% !important;
  max-height: 500px;
  min-height: 250px;
}

.board {
  animation: fadeInUp 0.6s ease-out 0.2s both;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 1024px) {
  .chart-container {
    min-height: 250px;
  }
}

@media (max-width: 768px) {
  .chart-container {
    min-height: 220px;
  }
}

@media (max-width: 480px) {
  .chart-container {
    min-height: 200px;
  }
}

@media (max-width: 768px) {
  .board {
    padding: 2rem;
    border-radius: 12px;
  }

  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .chart-title {
    font-size: 1.6rem;
  }

  .period-selector {
    align-self: stretch;
    justify-content: center;
  }

  .period-item {
    flex: 1;
    padding: 0.5rem 0.8rem;
    font-size: 0.85rem;
  }

  .chart-container {
    height: 220px;
  }
}

@media (max-width: 480px) {
  .board {
    padding: 1.5rem;
  }

  .period-selector {
    flex-wrap: wrap;
  }

  .period-item {
    min-width: 60px;
  }

  .chart-container {
    height: 200px;
  }
}

/* Стили для спиннера */
.chart-spinner {
  padding: 2rem;
  background: linear-gradient(135deg, #1a1f2e 0%, #242a3d 100%);
  border-radius: 16px;
  box-shadow:
      0 12px 40px rgba(0, 0, 0, 0.4),
      inset 0 1px 0 rgba(255, 255, 255, 0.05);
  font-family: "Inter", sans-serif;
  color: #fff;
  max-width: 1000px;
  width: 90%; /* Добавил для адаптивности */
  margin: 2rem auto; /* Отступ сверху и снизу */
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  box-sizing: border-box; /* Важно для правильного расчета размеров */
  height: fit-content;
}

.spinner-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(255, 255, 255, 0.1);
  border-left: 4px solid #3DEDCD;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1.5rem;
  box-shadow: 0 0 20px rgba(61, 237, 205, 0.3);
}

.loading-text {
  color: #b0b5c3;
  font-size: 1.1rem;
  font-weight: 500;
  margin: 0;
  background: linear-gradient(135deg, #b0b5c3 0%, #8a8f9d 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>