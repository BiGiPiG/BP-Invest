<template>
  <div class="board" id="chart">
    <div id="header">
      <div id="price_label">Price</div>
      <ul id="period">
        <li :class="{ active: currentPeriod === '1W' }" @click="setPeriod('1W')">1W</li>
        <li :class="{ active: currentPeriod === '1M' }" @click="setPeriod('1M')">1M</li>
        <li :class="{ active: currentPeriod === '1Y' }" @click="setPeriod('1Y')">1Y</li>
      </ul>
    </div>
    <div style="width: 100%; max-width: 800px; margin: 0 auto; padding: 16px 0;">
      <canvas id="acquisitions"></canvas>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch, nextTick } from 'vue';
import Chart from 'chart.js/auto';

const currentPeriod = ref('1M');
let chartInstance = null;
let datasetsByPeriod = { '1W': { labels: [], values: [] }, '1M': { labels: [], values: [] }, '1Y': { labels: [], values: [] } };

const props = defineProps({
  chartData: {
    type: Object,
    default: () => ({ dates: [], values: [] })
  }
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
  chartInstance.update();
}

function updateDatasets() {
  const allLabels = props.chartData.dates || [];
  const allValues = props.chartData.values || [];

  // Простейшее разбиение: 1W - 7 последних точек, 1M - 12, 1Y - все
  const sliceLast = (arr, n) => (n > 0 ? arr.slice(-n) : arr.slice());
  datasetsByPeriod = {
    '1W': { labels: sliceLast(allLabels, 7), values: sliceLast(allValues, 7) },
    '1M': { labels: sliceLast(allLabels, 12), values: sliceLast(allValues, 12) },
    '1Y': { labels: allLabels, values: allValues }
  };
}

function createChart() {
  const canvas = document.getElementById('acquisitions');
  if (!canvas) return;
  
  // Удаляем предыдущий график, если он существует
  if (chartInstance) {
    chartInstance.destroy();
  }
  
  const context = canvas.getContext('2d');

  const gradient = context.createLinearGradient(0, 0, 0, 100);
  gradient.addColorStop(0, '#02ffbbff');
  gradient.addColorStop(1, '#202538');

  chartInstance = new Chart(canvas, {
    type: 'line',
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        x: { display: false },
        y: { border: { display: false } }
      },
      elements: { point: { radius: 0 } },
      animation: true,
      plugins: {
        legend: { display: false },
        tooltip: { enabled: false }
      }
    },
    data: {
      labels: datasetsByPeriod[currentPeriod.value].labels,
      datasets: [
        {
          label: 'Price',
          data: datasetsByPeriod[currentPeriod.value].values,
          backgroundColor: gradient,
          borderColor: 'rgba(57, 247, 196, 1)',
          fill: true
        }
      ]
    }
  });
}

// Следим за изменениями данных
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
@import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');

#header {
  display: flex;
  justify-content: space-between;
  font-family: "Inter";
}

#period {
  display: flex;
  gap: 12px;
  list-style: none;
  margin: 0;
  padding: 0;
}

#period li {
  color: #93A0AF;
  cursor: pointer;
}

#period li:hover {
  color: #ffffff;
}

#period li.active {
  color: #3DEDCD;
  font-weight: 600;
}

#chart {
  font-family: "Inter";
  background-color: #202538;
  padding-left: 20%;
  padding-right: 20%;
  grid-area: B;
  display: flex;
  justify-content: center;
  flex-direction: column;
  list-style: none;
}

#price_label{
  font-family: "Inter";
  font-size: 1.7em;
  color: #ffffff;
  font-weight: 700;
}
</style>

