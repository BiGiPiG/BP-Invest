<template>
  <div v-if="isLoading" class="analysis-spinner">
    <div class="spinner-container">
      <div class="spinner"></div>
      <p class="loading-text">Generating AI Analysis...</p>
    </div>
  </div>
  <div v-else class="board" id="analysis">
    <div class="header">
      <div class="score-badge">{{ score }}</div>
    </div>

    <p class="description">{{ description }}</p>

    <div class="content-grid">
      <div class="section pros-section">
        <div class="section-header">
          <span class="section-icon">✓</span>
          <h2 class="section-title">{{ prosTitle }}</h2>
        </div>
        <ul class="item-list pros-list">
          <li v-for="p in pros" :key="p" class="list-item pros-item">
            <span class="icon check-icon"></span>
            <span class="item-text">{{ p }}</span>
          </li>
        </ul>
      </div>

      <div class="section cons-section">
        <div class="section-header">
          <span class="section-icon">✗</span>
          <h2 class="section-title">{{ consTitle }}</h2>
        </div>
        <ul class="item-list cons-list">
          <li v-for="c in cons" :key="c" class="list-item cons-item">
            <span class="icon cross-icon"></span>
            <span class="item-text">{{ c }}</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: { type: String, required: true },
  description: { type: String, required: true },
  score: { type: String, required: true },
  prosTitle: { type: String, default: 'Pros' },
  consTitle: { type: String, default: 'Cons' },
  pros: { type: Array, default: () => [] },
  cons: { type: Array, default: () => [] },
  isLoading: false
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
  max-width: 1000px;
  margin: 0.1rem auto;
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
  gap: 1.5rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.score-badge {
  background: linear-gradient(135deg, #3DEDCD 0%, #2bb7a9 100%);
  color: #0f141f;
  padding: 0.6rem 1.5rem;
  border-radius: 12px;
  font-weight: 700;
  font-size: 1.2rem;
  align-self: center;
  box-shadow:
      0 6px 20px rgba(61, 237, 205, 0.4),
      inset 0 1px 0 rgba(255, 255, 255, 0.3);
  min-width: fit-content;
  border: 1px solid rgba(61, 237, 205, 0.3);
}

.description {
  color: #b0b5c3;
  line-height: 1.7;
  margin: 0 0 2rem 0;
  font-size: 1.1rem;
  font-weight: 400;
  padding: 1.2rem;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  border-left: 4px solid #3DEDCD;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-top: 2rem;
}

.section {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 16px;
  padding: 1.8rem;
  border: 1px solid rgba(255, 255, 255, 0.05);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.section:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid rgba(255, 255, 255, 0.08);
}

.section-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.1rem;
}

.pros-section .section-icon {
  background: linear-gradient(135deg, #3DEDCD 0%, #2bb7a9 100%);
  color: #0f141f;
}

.cons-section .section-icon {
  background: linear-gradient(135deg, #FF6B6B 0%, #e05c5c 100%);
  color: #fff;
}

.section-title {
  font-size: 1.4rem;
  font-weight: 600;
  margin: 0;
  color: #fff;
}

.item-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.list-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 1rem;
  margin-bottom: 0.5rem;
  border-radius: 10px;
  transition: all 0.2s ease;
  background: rgba(255, 255, 255, 0.01);
}

.list-item:hover {
  background: rgba(255, 255, 255, 0.05);
  transform: translateX(4px);
}

.list-item:last-child {
  margin-bottom: 0;
}

.icon {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  margin-top: 2px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-icon {
  background: rgba(61, 237, 205, 0.15);
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%233DEDCD'%3E%3Cpath d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 16px;
}

.cross-icon {
  background: rgba(255, 107, 107, 0.15);
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23FF6B6B'%3E%3Cpath d='M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: center;
  background-size: 16px;
}

.item-text {
  color: #e0e3eb;
  line-height: 1.5;
  font-weight: 400;
  font-size: 1rem;
  flex: 1;
}

.board {
  animation: fadeInUp 0.6s ease-out;
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

@media (max-width: 900px) {
  .content-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
}

@media (max-width: 768px) {
  .board {
    grid-area: C;
    padding: 2rem;
    margin: 1rem;
    border-radius: 12px;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .score-badge {
    align-self: flex-start;
    font-size: 1.1rem;
    padding: 0.5rem 1.2rem;
  }

  .description {
    font-size: 1rem;
    padding: 1rem;
  }

  .section {
    padding: 1.5rem;
  }

  .section-title {
    font-size: 1.2rem;
  }

  .list-item {
    padding: 0.8rem;
    gap: 0.8rem;
  }

  .icon {
    width: 20px;
    height: 20px;
  }
}

@media (max-width: 480px) {
  .board {
    padding: 1.5rem;
  }

  .section {
    padding: 1.2rem;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}

/* Стили для спиннера */
.analysis-spinner {
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
  flex: 1;
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