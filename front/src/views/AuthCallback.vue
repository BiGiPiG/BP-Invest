<template>
  <div class="auth-callback-container">
    <div v-if="isLoading" class="spinner"></div>
    <p v-if="isLoading" class="message">Завершаем вход через Google...</p>
    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import useOAuth2 from '../composables/useOAuth2'

const { exchangeCodeForToken, isLoading, error } = useOAuth2()

onMounted(() => {
  const urlParams = new URLSearchParams(window.location.search)
  const code = urlParams.get('code')

  if (code) {
    exchangeCodeForToken(code)
  } else {
    error.value = 'Authorization code not found'
    setTimeout(() => {
      window.location.href = '/login'
    }, 3000)
  }
})
</script>

<style scoped>
.auth-callback-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #202538;
  color: white;
  font-family: "Inter", sans-serif;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #389194;
  border-top: 5px solid transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.message {
  font-size: 18px;
  color: #e0e0e0;
}

.error-message {
  font-size: 18px;
  color: #ff6b6b;
}
</style>