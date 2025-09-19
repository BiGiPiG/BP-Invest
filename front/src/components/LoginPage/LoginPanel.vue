<template>
  <div id="login-form">
    <div id="main-container" :style="{ minHeight: containerHeight }">
      <div class="top-section">
        <div class="tabs-container">
          <span @click="setMode(true)" :class="['tab', { active: signIn }]">Sign in</span>
          <span @click="setMode(false)" :class="['tab', { active: !signIn }]">Sign up</span>
        </div>
        <div id="line"></div>
      </div>

      <div class="centered-content">
        <div class="input-group">
          <template v-for="field in visibleFields" :key="field.key">
            <div class="input-label">{{ field.label }}</div>
            <input
              :type="field.type"
              :class="['form-input', { error: touched[field.key] && errors[field.key] }]"
              :placeholder="field.placeholder"
              v-model="form[field.key]"
              @blur="markTouched(field.key); validateField(field.key)"
              @input="validateField(field.key)"
            />
            <div v-if="touched[field.key] && errors[field.key]" class="error-text">{{ errors[field.key] }}</div>
          </template>
        </div>
      </div>

      <div class="buttons-container">
        <button
          v-if="signIn"
          @click="handleLogin"
          class="btn-primary"
          :disabled="!form.login || !form.password"
        >Continue</button>
        <button
          v-else
          @click="handleRegistr"
          class="btn-primary"
          :disabled="!form.email || !form.login || !form.password || !form.confirmPassword"
        >Continue</button>
        <button v-if="signIn" class="btn-google">
          <span class="google-icon">G</span>
          Continue with Google
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

#login-form {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100%;
  margin: 0;
  background: linear-gradient(112.47deg, #000000 -11.01%, #245D5F 70.84%, #389194 160.87%);
  font-family: "Inter", sans-serif;
}

#main-container {
  width: 100%;
  max-width: 420px;
  height: auto;
  min-height: 500px;
  max-height: none;
  background: #202538;
  border-radius: 16px;
  box-sizing: border-box;
  padding: 40px 30px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.top-section {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.tabs-container {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
}

.tab {
  color: #aaa;
  font-size: 16px;
  font-weight: 500;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.tab:hover {
  color: #e6e6e6;
}

.tab:active {
  transform: scale(0.98);
}

.tab.active {
  color: #ffffff;
  background: rgba(255, 255, 255, 0.1);
  font-weight: 600;
}

#line {
  height: 1px;
  width: 100%;
  background-color: #ffffff;
  opacity: 0.3;
}

.centered-content {
  width: 100%;
  display: flex;
  justify-content: center;
}

.input-group {
  width: 100%;
  max-width: 320px;
}

.input-label {
  margin-top: 16px;
  color: #ffffff;
  font-size: 14px;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 14px;
  margin-top: 8px;
  border-radius: 8px;
  border: 1px solid #3a405a;
  background-color: #2a3042;
  color: white;
  font-family: "Inter", sans-serif;
  font-size: 15px;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-color: #389194;
}

.form-input.error {
  border-color: #DB4437;
}

.error-text {
  color: #ffb4b4;
  font-size: 12px;
  margin-top: 6px;
}

.error-summary {
  background: rgba(219, 68, 55, 0.15);
  color: #ffdddd;
  border: 1px solid #DB4437;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 13px;
}

.buttons-container {
  width: 100%;
  max-width: 320px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.btn-primary,
.btn-google {
  width: 100%;
  padding: 14px;
  border-radius: 8px;
  border: none;
  outline: none;
  cursor: pointer;
  font-family: "Inter", sans-serif;
  font-weight: 600;
  font-size: 15px;
  transition: transform 0.1s ease, opacity 0.2s ease;
}

.btn-primary {
  background-color: #389194;
  color: white;
}

.btn-primary:hover {
  opacity: 0.95;
}

.btn-primary:active {
  transform: scale(0.98);
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-google {
  background-color: #ffffff;
  color: #000000;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-google:hover {
  opacity: 0.9;
}

.google-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  background: #DB4437;
  color: white;
  border-radius: 50%;
  font-weight: bold;
  font-size: 12px;
}
</style>

<script setup lang="js">
import { ref, reactive, computed } from 'vue'

const signIn = ref(true);

const form = reactive({
  login: '',
  password: '',
  email: '',
  confirmPassword: ''
});

const errors = reactive({
  login: '',
  password: '',
  email: '',
  confirmPassword: ''
});

const touched = reactive({
  login: false,
  password: false,
  email: false,
  confirmPassword: false
});

const containerHeight = computed(() => {
  return signIn.value ? '480px' : '680px';
});

function setMode(isSignIn) {
  signIn.value = isSignIn;
  Object.keys(errors).forEach((k) => (errors[k] = ''));
  Object.keys(touched).forEach((k) => (touched[k] = false));
}

function markTouched(key) {
  touched[key] = true;
}

function validateField(key) {
  const value = form[key]?.toString().trim();
  let message = '';

  const isFieldRequired = () => {
    if (signIn.value) {
      return key === 'login' || key === 'password';
    }
    return key === 'email' || key === 'login' || key === 'password' || key === 'confirmPassword';
  };

  if (isFieldRequired() && !value) {
    message = 'Field is required';
  }
  else if (value) {
    switch (key) {
      case 'email':
        const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
        if (!emailRe.test(value)) message = 'Invalid email';
        break;
      case 'password':
        if (value.length < 6) message = 'Minimum 6 characters';
        break;
      case 'confirmPassword':
        if (value !== form.password) message = 'Passwords do not match';
        break;
    }
  }

  errors[key] = message;
}

function validateForm() {
  const keys = signIn.value
      ? ['login', 'password']
      : ['email', 'login', 'password', 'confirmPassword'];

  keys.forEach((k) => validateField(k));

  return keys.every((k) => !errors[k]);
}

const visibleFields = computed(() => {
  if (signIn.value) {
    return [
      { key: 'login', type: 'text', label: 'Login', placeholder: 'Enter login' },
      { key: 'password', type: 'password', label: 'Password', placeholder: 'Enter password' }
    ]
  }
  return [
    { key: 'email', type: 'email', label: 'Email', placeholder: 'Enter email' },
    { key: 'login', type: 'text', label: 'Login', placeholder: 'Enter login' },
    { key: 'password', type: 'password', label: 'Password', placeholder: 'Enter password' },
    { key: 'confirmPassword', type: 'password', label: 'Confirm password', placeholder: 'Enter password again' }
  ]
});

async function handleLogin() {
  try {
    const keys = ['login', 'password'];
    keys.forEach((k) => (touched[k] = true));
    keys.forEach((k) => validateField(k));

    const isValid = keys.every((k) => !errors[k] && form[k]?.toString().trim());
    if (!isValid) return;

    const response = await fetch('http://localhost:9001/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: form.login,
        password: form.password
      })
    })

    if (response.status === 404) {
      const errorData = await response.json();

      if (errorData.errorCode === 'USER_NOT_FOUND') {
        errors.login = 'Login not found. Try another one';
        touched.login = true;
      } else if (errorData.errorCode === 'INVALID_PASSWORD') {
        errors.password = 'Invalid password';
        touched.password = true;
      }
      return;
    }

    let data;
    try {
      data = await response.json();
    } catch (jsonError) {
      throw new Error('Сервер вернул некорректный ответ')
    }

    if (!response.ok) {
      const errorMessage = data?.message || data?.error || 'Неверный логин или пароль'
      throw new Error(errorMessage)
    }

    if (!data.token) {
      throw new Error('Токен не получен от сервера')
    }

    localStorage.setItem('token', data.token)
    window.location.href = '/home'
  } catch (error) {
    alert("Server error")
  }
}

async function handleRegistr() {
  const keys = ['email', 'login', 'password', 'confirmPassword'];
  keys.forEach((k) => (touched[k] = true));
  keys.forEach((k) => validateField(k));

  const isValid = keys.every((k) => !errors[k] && form[k]?.toString().trim());
  if (!isValid) return;
  try {
    const response = await fetch('http://localhost:9001/api/auth/registr', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: form.login,
        password: form.password,
        email: form.email
      })
    })

    if (response.status === 409) {
      errors.login = 'This login is already taken. Please choose another.';
      touched.login = true;
      return;
    }

    window.location.href = '/home'
  } catch (error) {
    alert("Server error")
  }
}
</script>