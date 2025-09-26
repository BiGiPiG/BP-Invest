<template>
  <div v-if="isLoading" class="info-spinner">
    <div class="spinner-container">
      <div class="spinner"></div>
      <p class="loading-text">Generating Information...</p>
    </div>
  </div>
  <div v-else class="board" id="info">
    <div class="header">
      <h1 class="ticker">[{{ ticker }}] {{ company }}</h1>
      <div class="market-cap-badge">Market Capitalization ${{ marketCap }}</div>
    </div>
    <div class="metrics-grid">
      <div v-for="m in metrics" :key="m.type" class="metric-card">
        <div class="metric-icon">
          <span class="icon">{{ getMetricIcon(m.type) }}</span>
        </div>
        <div class="metric-content">
          <div class="metric-type">{{ formatMetricType(m.type) }}</div>
          <div class="metric-value">{{ m.value }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  ticker: { type: String, required: true },
  company: { type: String, required: true },
  metrics: { type: Array, required: true },
  marketCap: { type: String, required: true },
  isLoading: false
});

const getMetricIcon = (type) => {
  const icons = {
    'P/E': '📊',
    'P/S': '💹',
    'P/B': '📈',
    'EPS': '💰',
    'ROE': '🎯',
    'ROA': '⚡',
    'D/E': '⚖️',
    'Current Ratio': '🛡️',
    'Dividend Yield': '🎁'
  };
  return icons[type] || '📋';
};

const formatMetricType = (type) => {
  return type.replace(/\//g, '/');
};
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

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1.5rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.ticker {
  font-family: "Inter",serif;
  font-size: 2rem;
  font-weight: 700;
  line-height: 1.2;
  color: #fff;
  margin: 0;
  background: linear-gradient(135deg, #fff 0%, #b0b5c3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.market-cap-badge {
  background: linear-gradient(135deg, #3DEDCD 0%, #2bb7a9 100%);
  color: #0f141f;
  padding: 0.6rem 1.2rem;
  border-radius: 12px;
  font-weight: 700;
  font-size: 1.1rem;
  box-shadow:
      0 6px 20px rgba(61, 237, 205, 0.4),
      inset 0 1px 0 rgba(255, 255, 255, 0.3);
  min-width: fit-content;
  border: 1px solid rgba(61, 237, 205, 0.3);
}

.market-cap-label {
  color: #b0b5c3;
  font-size: 0.9rem;
  font-weight: 500;
  margin-bottom: 2.5rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.2rem;
  margin-top: 1rem;
}

.metric-card {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 14px;
  padding: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 1rem;
  min-height: 90px;
}

.metric-card:hover {
  background: rgba(255, 255, 255, 0.06);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
  border-color: rgba(61, 237, 205, 0.2);
}

.metric-icon {
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(61, 237, 205, 0.15) 0%, rgba(43, 183, 169, 0.15) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.metric-content {
  flex: 1;
}

.metric-type {
  color: #93A0AF;
  font-size: 0.9rem;
  font-weight: 500;
  margin-bottom: 0.4rem;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}

.metric-value {
  color: #3DEDCD;
  font-size: 1.4rem;
  font-weight: 600;
  line-height: 1.2;
}

.board {
  animation: fadeInUp 0.6s ease-out 0.1s both;
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
  .metrics-grid {
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  }
}

@media (max-width: 768px) {
  .board {
    padding: 2rem;
    border-radius: 12px;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .ticker {
    font-size: 1.8rem;
  }

  .market-cap-badge {
    align-self: flex-start;
    font-size: 1rem;
    padding: 0.5rem 1rem;
  }

  .metrics-grid {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 1rem;
  }

  .metric-card {
    padding: 1.2rem;
    min-height: 80px;
  }

  .metric-icon {
    width: 40px;
    height: 40px;
    font-size: 1.3rem;
  }

  .metric-value {
    font-size: 1.2rem;
  }
}

@media (max-width: 480px) {
  .board {
    padding: 1.5rem;
  }

  .metrics-grid {
    grid-template-columns: 1fr;
  }

  .metric-card {
    padding: 1rem;
  }

  .ticker {
    font-size: 1.6rem;
  }
}

.metric-card:nth-child(2n) .metric-icon {
  background: linear-gradient(135deg, rgba(96, 165, 250, 0.15) 0%, rgba(59, 130, 246, 0.15) 100%);
}

.metric-card:nth-child(3n) .metric-icon {
  background: linear-gradient(135deg, rgba(249, 115, 22, 0.15) 0%, rgba(234, 88, 12, 0.15) 100%);
}

.metric-card:nth-child(4n) .metric-icon {
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.15) 0%, rgba(124, 58, 237, 0.15) 100%);
}

/* Стили для спиннера */
.info-spinner {
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