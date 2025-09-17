<template>
  <header>
    <div id="logo">
      <img src="/images/Лого.png" width="40px" />
      <span>BP Invest</span>
    </div>
    <div id="search_bar">
      <SearchBar v-model="search" @search="onSearch" />
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
  @import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');

  header {
    display: flex;
    align-items: center;
  }

  #logo {
    display: flex;
    width: 15%;
    height: 60px;
    color: #fff;
    font-family: "Inter";
    font-weight: 600;
    line-height: 40px;
    font-size: 1.5em;
    gap: 10px;
    justify-content: center;
    align-items: flex-end;
  }

  #search_bar {
    flex: 1;
    display: flex;
    align-items: flex-end;
    max-width: 50%;
    height: 60px;
    margin-left: 40px;
  }
</style>

