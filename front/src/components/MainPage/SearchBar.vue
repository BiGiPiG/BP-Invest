<template>
  <div class="search-bar">
    <div class="input-wrap">
      <input
        v-model="query"
        type="text"
        class="input"
        :placeholder="placeholder"
        @keydown.enter.prevent="emitSearch"
      />
      <button v-if="query" class="clear" type="button" aria-label="Clear" @click="clear">×</button>
    </div>
    <button class="submit" @click="emitSearch">Search</button>
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
  emit('search', query.value.trim());
}

function clear() {
  query.value = '';
}
</script>

<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');


.search-bar {
    display: flex;
    width: 100%;
    align-items: center;
    font-family: "Inter";
}

.input-wrap {
  position: relative;
  display: inline-flex;
  flex: 1;
}

.input {
  height: 40px;
  width: 100%;
  padding: 0 36px 0 12px;
  background: #202638;
  color: #e5e7eb;
  font-size: 1.1em;
  border: none;
  outline: none;
  box-sizing: border-box;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  padding-left: 10%;
}

.input::placeholder {
  color: #6b7280;
  font-size: 1.1em;
}

.clear {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  width: 24px;
  height: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
  background: transparent;
  color: #9ca3af;
  font-size: 18px;
  line-height: 1;
  cursor: pointer;
  padding: 0;
}

.clear:hover {
  color: #d1d5db;
}

.submit {
  all: unset;
  height: 40px;
  padding: 0 3%;
  width: 20%;
  background: #1f2331;
  text-align: center;
  font-size: 1.1em;
  color: #e5e7eb;
  cursor: pointer;
  border-bottom-right-radius: 5px;
  border-top-right-radius: 5px;
}

/* removed duplicate .clear block */

.submit:hover {
  background: #2a3044;
}
</style>