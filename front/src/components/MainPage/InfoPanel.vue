<template>
  <div class="board" id="info">
    <div id="ticker">[{{ ticker }}] {{ company }}</div>
    <div id="marketCap">Market Cap: ${{ marketCap }}</div>
    <!-- <div id="line"></div> -->
    <div id="list">
      <ul>
        <li v-for="m in metrics" :key="m.type">
          <div class="type">{{ m.type }}</div>
          <div class="value">{{ m.value }}</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
defineProps({
  ticker: { type: String, required: true },
  company: { type: String, required: true },
  metrics: { type: Array, required: true },
  marketCap: { type: String, required: true },
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');

#info {
  padding-left: 20%;
  padding-right: 20%;
  padding-top: 10%;
  padding-bottom: 10%;
  background: #202538;
  border-top-left-radius: 5px;
  grid-area: A;
  max-height: 50vh;
  display: flex;
  flex-direction: column;
}

#ticker {
  color:#ffffff;
  font-family: "Inter";
  font-size: 1.7em;
  margin-bottom: 3%;
  font-weight: 700;
}

#marketCap {
  color: #CCD6DE;
  font-family: "Inter";
  margin-bottom: 2%;
}

#line {
  height: 1px;
  background: #93A0AF;
  width: 100%;
}

#list {
  margin-top: auto;
}

#list ul {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  gap: 20px;
  justify-content: space-between;
}

.type {
  color: #93A0AF;
  font-size: 1.5em;
  font-family: "Inter";
}

.value {
  color: #3DEDCD;
  font-family: "Inter";
  font-size: 1.5em;
}
</style>

