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
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  height: fit-content;
}

.header {
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
  gap: 1rem; /* Уменьшил */
  margin-bottom: 0.8rem; /* Уменьшил */
  flex-wrap: wrap;
}

.score-badge {
  background: linear-gradient(135deg, #3DEDCD 0%, #2bb7a9 100%);
  color: #0f141f;
  padding: 0.4rem 1rem; /* Уменьшил */
  border-radius: 8px; /* Уменьшил */
  font-weight: 700;
  font-size: 1rem; /* Уменьшил */
  align-self: center;
  box-shadow:
      0 4px 15px rgba(61, 237, 205, 0.4), /* Уменьшил */
      inset 0 1px 0 rgba(255, 255, 255, 0.3);
  min-width: fit-content;
  border: 1px solid rgba(61, 237, 205, 0.3);
}

.description {
  color: #b0b5c3;
  line-height: 1.5; /* Уменьшил */
  font-size: 0.9rem; /* Уменьшил */
  font-weight: 400;
  padding: 0.8rem; /* Уменьшил */
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px; /* Уменьшил */
  border-left: 3px solid #3DEDCD; /* Уменьшил */
  margin-bottom: 0.8rem; /* Уменьшил */
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem; /* Уменьшил */
  margin-top: 0.8rem; /* Уменьшил */
}

.section {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 12px; /* Уменьшил */
  padding: 1rem; /* Уменьшил */
  border: 1px solid rgba(255, 255, 255, 0.05);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  box-sizing: border-box;
}

.section:hover {
  transform: translateY(-1px); /* Уменьшил */
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2); /* Уменьшил */
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.6rem; /* Уменьшил */
  padding-bottom: 0.8rem; /* Уменьшил */
  border-bottom: 1px solid rgba(255, 255, 255, 0.08); /* Уменьшил */
  margin-bottom: 0.8rem; /* Уменьшил */
}

.section-icon {
  width: 28px; /* Уменьшил */
  height: 28px; /* Уменьшил */
  border-radius: 6px; /* Уменьшил */
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1rem; /* Уменьшил */
}

.section-title {
  font-size: 1.1rem; /* Уменьшил */
  font-weight: 600;
  margin: 0;
  color: #fff;
}

.list-item {
  display: flex;
  align-items: flex-start;
  gap: 0.8rem; /* Уменьшил */
  padding: 0.6rem; /* Уменьшил */
  border-radius: 6px; /* Уменьшил */
  transition: all 0.2s ease;
  background: rgba(255, 255, 255, 0.01);
  margin-bottom: 0.3rem; /* Уменьшил */
}

.list-item:hover {
  background: rgba(255, 255, 255, 0.05);
  transform: translateX(2px); /* Уменьшил */
}

.icon {
  flex-shrink: 0;
  width: 18px; /* Уменьшил */
  height: 18px; /* Уменьшил */
  margin-top: 1px;
  border-radius: 3px; /* Уменьшил */
  display: flex;
  align-items: center;
  justify-content: center;
  background-size: 14px; /* Уменьшил */
}

.item-text {
  color: #e0e3eb;
  line-height: 1.4; /* Уменьшил */
  font-weight: 400;
  font-size: 0.9rem; /* Уменьшил */
  flex: 1;
}

/* Уменьшенные стили для спиннера */
.analysis-spinner {
  padding: 1.5rem; /* Уменьшил */
  background: linear-gradient(135deg, #1a1f2e 0%, #242a3d 100%);
  border-radius: 12px; /* Уменьшил */
  box-shadow:
      0 8px 30px rgba(0, 0, 0, 0.4), /* Уменьшил */
      inset 0 1px 0 rgba(255, 255, 255, 0.05);
  font-family: "Inter", sans-serif;
  color: #fff;
  width: 100%;
  margin: 0.5rem auto; /* Уменьшил */
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 250px; /* Уменьшил */
  box-sizing: border-box;
}

.spinner {
  width: 50px; /* Уменьшил */
  height: 50px; /* Уменьшил */
  border: 3px solid rgba(255, 255, 255, 0.1); /* Уменьшил */
  border-left: 3px solid #3DEDCD; /* Уменьшил */
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
  box-shadow: 0 0 15px rgba(61, 237, 205, 0.3); /* Уменьшил */
}

.loading-text {
  color: #b0b5c3;
  font-size: 1rem; /* Уменьшил */
  font-weight: 500;
  margin: 0;
}

/* Адаптивность для мобильных */
@media (max-width: 768px) {
  .board {
    padding: 0.8rem; /* Уменьшил */
    margin: 0.5rem; /* Уменьшил */
    border-radius: 10px; /* Уменьшил */
  }

  .header {
    gap: 0.8rem; /* Уменьшил */
  }

  .score-badge {
    font-size: 0.9rem; /* Уменьшил */
    padding: 0.3rem 0.8rem; /* Уменьшил */
  }

  .description {
    font-size: 0.85rem; /* Уменьшил */
    padding: 0.6rem; /* Уменьшил */
    margin-bottom: 0.6rem; /* Уменьшил */
  }

  .section {
    padding: 0.8rem; /* Уменьшил */
  }

  .section-title {
    font-size: 1rem; /* Уменьшил */
  }

  .list-item {
    padding: 0.5rem; /* Уменьшил */
    gap: 0.6rem; /* Уменьшил */
  }

  .analysis-spinner {
    padding: 1rem; /* Уменьшил */
    min-height: 200px; /* Уменьшил */
  }

  .spinner {
    width: 40px; /* Уменьшил */
    height: 40px; /* Уменьшил */
  }

  .loading-text {
    font-size: 0.9rem; /* Уменьшил */
  }
}

@media (max-width: 480px) {
  .board {
    padding: 0.6rem; /* Уменьшил */
  }

  .section {
    padding: 0.6rem; /* Уменьшил */
  }

  .content-grid {
    gap: 0.8rem; /* Уменьшил */
  }
}

/* Остальные стили остаются без изменений */
.pros-section .section-icon {
  background: linear-gradient(135deg, #3DEDCD 0%, #2bb7a9 100%);
  color: #0f141f;
}

.cons-section .section-icon {
  background: linear-gradient(135deg, #FF6B6B 0%, #e05c5c 100%);
  color: #fff;
}

.check-icon {
  background: rgba(61, 237, 205, 0.15);
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%233DEDCD'%3E%3Cpath d='M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: center;
}

.cross-icon {
  background: rgba(255, 107, 107, 0.15);
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23FF6B6B'%3E%3Cpath d='M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: center;
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

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@media (max-width: 900px) {
  .content-grid {
    grid-template-columns: 1fr;
    gap: 1rem; /* Уменьшил */
  }
}
</style>