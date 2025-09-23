<template>
  <div class="search-bar">
    <div class="input-wrap">
      <input
          v-model="query"
          type="text"
          class="input"
          :placeholder="placeholder"
          @keydown.enter.prevent="emitSearch"
          aria-label="Search company ticker"
      />
      <button v-if="query" class="clear" type="button" aria-label="Clear" @click="clear">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="18" y1="6" x2="6" y2="18"></line>
          <line x1="6" y1="6" x2="18" y2="18"></line>
        </svg>
      </button>
    </div>
    <button class="submit" @click="emitSearch">
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 6px">
        <circle cx="11" cy="11" r="8"></circle>
        <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
      </svg>
      Search
    </button>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: { type: String, default: '' },
  placeholder: { type: String, default: 'Enter ticker of company' },
});

const emit = defineEmits(['update:modelValue', 'search']);

const query = ref(props.modelValue);

watch(
    () => props.modelValue,
    (val) => {
      if (val !== query.value) query.value = val;
    }
);

watch(query, (val) => emit('update:modelValue', val));

function emitSearch() {
  const trimmed = query.value.trim();
  if (trimmed) {
    emit('search', trimmed);
  }
}

function clear() {
  query.value = '';
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

.search-bar {
  display: flex;
  width: 100%;
  max-width: 600px;
  background: rgba(30, 36, 56, 0.4);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  transition: all 0.3s ease;
}

.search-bar:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.2);
  transform: translateY(-1px);
}

.input-wrap {
  position: relative;
  flex: 1;
  display: flex;
  align-items: center;
}

.input {
  width: 100%;
  height: 52px;
  padding: 0 48px 0 20px;
  background: transparent;
  color: #e0e3eb;
  font-family: "Inter", sans-serif;
  font-size: 1rem;
  font-weight: 500;
  border: none;
  outline: none;
  caret-color: #3DEDCD;
}

.input::placeholder {
  color: #8a94a6;
  font-weight: 400;
}

.input:focus {
  outline: none;
  box-shadow: none;
}

.clear {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 28px;
  height: 28px;
  border: none;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #a0aab8;
  cursor: pointer;
  transition: all 0.2s ease;
  padding: 0;
}

.clear:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #ffffff;
  transform: translateY(-50%) scale(1.1);
}

.submit {
  all: unset;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  height: 52px;
  min-width: 100px;
  padding: 0 20px;
  background: linear-gradient(135deg, #3DEDCD 0%, #2bb7a9 100%);
  color: #0f141f;
  font-family: "Inter", sans-serif;
  font-weight: 600;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
}

.submit:hover {
  background: linear-gradient(135deg, #2bb7a9 0%, #1fa896 100%);
  box-shadow: 0 4px 16px rgba(61, 237, 205, 0.3);
}

.submit:active {
  transform: scale(0.98);
}

/* Адаптивность */
@media (max-width: 768px) {
  .search-bar {
    height: 48px;
    flex-direction: column;
    border-radius: 12px;
  }

  .input {
    height: 48px;
    padding: 0 40px 0 16px;
    font-size: 0.95rem;
  }

  .clear {
    width: 24px;
    height: 24px;
    right: 12px;
  }

  .submit {
    height: 48px;
    border-radius: 0 0 12px 12px;
    width: 100%;
    justify-content: center;
    font-size: 0.95rem;
  }
}

@media (max-width: 480px) {
  .input {
    padding: 0 36px 0 12px;
    font-size: 0.9rem;
  }

  .submit {
    font-size: 0.9rem;
    padding: 0 16px;
  }
}

.clear svg, .submit svg {
  stroke: currentColor;
  stroke-width: 2.5;
}
</style>