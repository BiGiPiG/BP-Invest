<template>
  <header class="app-header">
    <div class="header-content">
      <div class="logo-section">
        <img src="/images/Logo.png" alt="BP Invest Logo" class="logo-img" />
        <span class="logo-text">BP Invest</span>
      </div>
      <div class="search-section">
        <SearchBar v-model="search" @search="onSearch" />
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue';
import SearchBar from './SearchBar.vue';

const search = ref('');

const emit = defineEmits(['search']);

function onSearch(value) {
  if (!value) return;
  console.log('Search:', value);
  emit('search', value);
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

.app-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: linear-gradient(90deg, #1a2238 0%, #151a27 100%);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  border-radius: 0 0 16px 16px;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 24px;
  max-width: 1200px;
  width: 100%;
}

.logo-section {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  color: #fff;
  font-family: "Inter", sans-serif;
  font-weight: 700;
  font-size: 1.4rem;
  white-space: nowrap;
  line-height: 1;
  width: 250px;
}

.logo-img {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  display: block;
}

.search-section {
  flex: 1;
  max-width: 600px;
  display: flex;
  align-items: center;
  margin-left: auto;
}

@media (max-width: 1024px) {
  .app-header {
    padding: 0 16px;
    height: 64px;
  }

  .logo-section {
    font-size: 1.3rem;
  }

  .logo-img {
    width: 36px;
    height: 36px;
  }

  .header-content {
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 12px;
    padding-top: 6px;
  }

  .logo-section {
    font-size: 1.2rem;
  }

  .search-section {
    width: 100%;
    max-width: none;
  }
}
</style>