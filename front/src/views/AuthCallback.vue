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
</style>