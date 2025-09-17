<template>
  <div class="login-page">
    <LoginPanel />
  </div>
  
</template>

<script setup>
import LoginPanel from '../components/LoginPage/LoginPanel.vue';
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  color: #a0a0a0;
  background-color: #151A27;
}
</style>

