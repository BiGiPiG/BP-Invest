<template>
  <div class="board" id="analysis">
    <div class="title">{{ title }}</div>
    <div class="description">{{ description }}</div>
    <div class="score">{{ score }}</div>
    <div class="pros-title">{{ prosTitle }}</div>
    <div v-for="p in pros" :key="p" class="pros-item">{{ p }}</div>
    <div class="cons-title">{{ consTitle }}</div>
    <div v-for="c in cons" :key="c" class="cons-item">{{ c }}</div>
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
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');

#analysis {
  padding-top: 5%;
  padding-left: 20%;
  padding-right: 20%;
  padding-top: 10%;
  background-color: #202538;

  grid-area: C;
  border-bottom-right-radius: 5px;
  border-top-right-radius: 5px;
  font-family: "Inter";
}

.title {
  font-family: "Inter";
  font-size: 1.5em;
  font-weight: 700;
  margin-bottom: 8px;
  font-size: 1.7em;
  color: #fff;
}

.description {
  color: #BBBEC9;
  line-height: 1.5;
  margin-bottom: 5%;
}

.score {
  display: inline-block;
  color: #3DEDCD;
  font-weight: 600;
  font-size: 1.3em;
  margin-bottom: 5%;
}

.pros-title {
  color: #fff;
  font-size: 1.2em;
  font-weight: 600;
  margin-bottom: 3%;
}

.cons-title {
  color: #fff;
  font-size: 1.2em;
  font-weight: 600;
  margin-bottom: 3%;
}

.pros-item::before {
  content: "";
  display: inline-block;
  width: 16px;
  height: 16px;
  background: url("../../images/pros.png") no-repeat center/contain;
  vertical-align: middle;
}

.cons-item::before {
  content: "";
  display: inline-block;
  width: 16px;
  height: 16px;
  background: url("../../images/cons.png") no-repeat center/contain;
  vertical-align: middle;
}

.pros-item,
.cons-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #FFFFFF;
  font-weight: 400;
  margin-bottom: 4px;
}

.pros-item::before, .cons-item::before {
  content: "";
  width: 16px;
  height: 16px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}

.pros-item::before {
  background-image: url("../../images/pros.png");
}

.cons-item::before {
  background-image: url("../../images/cons.png");
}
</style>

