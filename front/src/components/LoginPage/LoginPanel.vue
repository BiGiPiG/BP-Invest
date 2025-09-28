<template>
  <div id="login-page">
    <div class="board" id="login-form">
      <div class="header">
        <h1 class="ticker">Welcome to BP Invest</h1>
      </div>

      <div class="tabs-container">
        <span @click="setMode(true)" :class="['tab', { active: signIn }]">Sign in</span>
        <span @click="setMode(false)" :class="['tab', { active: !signIn }]">Sign up</span>
      </div>

      <div class="form-content">
        <div class="input-group">
          <template v-for="field in visibleFields" :key="field.key">
            <div class="metric-card input-field">
              <div class="metric-icon">
                <span class="icon">{{ getFieldIcon(field.key) }}</span>
              </div>
              <div class="metric-content">
                <div class="input-header">
                  <div class="input-label">{{ field.label }}</div>
                  <div v-if="touched[field.key] && errors[field.key]" class="error-text">{{ errors[field.key] }}</div>
                </div>
                <input
                    :type="field.type"
                    :class="['form-input', { error: touched[field.key] && errors[field.key] }]"
                    :placeholder="field.placeholder"
                    v-model="form[field.key]"
                    @blur="markTouched(field.key); validateField(field.key)"
                    @input="validateField(field.key)"
                />
              </div>
            </div>
          </template>
        </div>

        <div class="buttons-container">
          <button
              v-if="signIn"
              @click="handleLogin"
              class="btn-primary metric-card"
              :disabled="!form.login || !form.password"
          >
            <span class="btn-icon">→</span>
            Continue
          </button>
          <button
              v-else
              @click="handleRegister"
              class="btn-primary metric-card"
              :disabled="!form.email || !form.login || !form.password || !form.confirmPassword"
          >
            <span class="btn-icon">→</span>
            Create Account
          </button>

          <button v-if="signIn" class="btn-google metric-card" @click="handleGoogleLogin">
            <span class="google-icon">G</span>
            Continue with Google
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

#login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;
  background: linear-gradient(135deg, #0f141f 0%, #1a1f2e 50%, #242a3d 100%);
  font-family: "Inter", sans-serif;
}

.board {
  padding: 2.5rem;
  background: linear-gradient(135deg, #1a1f2e 0%, #242a3d 100%);
  border-radius: 16px;
  box-shadow:
      0 12px 40px rgba(0, 0, 0, 0.4),
      inset 0 1px 0 rgba(255, 255, 255, 0.05);
  font-family: "Inter", sans-serif;
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  width: 100%;
  max-width: 480px;
  animation: fadeInUp 0.6s ease-out 0.1s both;
}

.header {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 1.5rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.ticker {
  font-family: "Inter", serif;
  font-size: 2rem;
  font-weight: 700;
  line-height: 1.2;
  color: #fff;
  margin: 0;
  background: linear-gradient(135deg, #fff 0%, #b0b5c3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;

}

.tabs-container {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  padding: 0.5rem;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.tab {
  flex: 1;
  text-align: center;
  padding: 1rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  color: #93A0AF;
}

.tab:hover {
  color: #e6e6e6;
  background: rgba(255, 255, 255, 0.05);
}

.tab.active {
  color: #3DEDCD;
  background: rgba(61, 237, 205, 0.1);
  box-shadow: 0 4px 15px rgba(61, 237, 205, 0.2);
}

.form-content {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.input-field {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 14px;
  padding: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.3s ease;
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  min-height: auto;
}

.input-field:hover {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(61, 237, 205, 0.2);
}

.metric-icon {
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(61, 237, 205, 0.15) 0%, rgba(43, 183, 169, 0.15) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  margin-top: 0.25rem;
}

.metric-content {
  flex: 1;
  min-width: 250px;
}

.input-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
  gap: 1rem;
}

.input-label {
  color: #93A0AF;
  font-size: 0.9rem;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.3px;
  white-space: nowrap;
}

.error-text {
  color: #FF6B6B;
  font-size: 0.8rem;
  font-weight: 500;
  text-align: right;
  white-space: nowrap;
  background: rgba(255, 107, 107, 0.1);
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
  border: 1px solid rgba(255, 107, 107, 0.3);
}

.form-input {
  width: 100%;
  padding: 0.5rem 0;
  background: transparent;
  border: none;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  color: white;
  font-family: "Inter", sans-serif;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-bottom-color: #3DEDCD;
}

.form-input.error {
  border-bottom-color: #FF6B6B;
}

.buttons-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.btn-primary,
.btn-google {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 14px;
  padding: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.06);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  color: white;
  font-family: "Inter", sans-serif;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  min-height: 70px;
}

.btn-primary {
  background: linear-gradient(135deg, rgba(61, 237, 205, 0.15) 0%, rgba(43, 183, 169, 0.15) 100%);
  border-color: rgba(61, 237, 205, 0.3);
}

.btn-primary:hover:not(:disabled) {
  background: linear-gradient(135deg, rgba(61, 237, 205, 0.25) 0%, rgba(43, 183, 169, 0.25) 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(61, 237, 205, 0.3);
}

.btn-primary:active:not(:disabled) {
  transform: translateY(0);
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

.btn-google {
  background: rgba(255, 255, 255, 0.08);
}

.btn-google:hover {
  background: rgba(255, 255, 255, 0.12);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.btn-icon {
  font-size: 1.2rem;
  font-weight: bold;
}

.google-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: #DB4437;
  color: white;
  border-radius: 50%;
  font-weight: bold;
  font-size: 0.9rem;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  #login-page {
    padding: 1rem;
  }

  .board {
    padding: 2rem;
    border-radius: 12px;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .ticker {
    font-size: 1.8rem;
  }

  .input-field {
    padding: 1.2rem;
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }

  .input-header {
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }

  .error-text {
    text-align: center;
  }

  .metric-icon {
    width: 40px;
    height: 40px;
    font-size: 1.3rem;
    margin-top: 0;
  }

  .btn-primary,
  .btn-google {
    padding: 1.2rem;
    min-height: 60px;
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  .board {
    padding: 1.5rem;
  }

  .ticker {
    font-size: 1.6rem;
  }

  .tabs-container {
    flex-direction: column;
    gap: 0.5rem;
  }

  .input-field {
    padding: 0.5rem;
  }

  .input-header {
    align-items: center;
  }
}
</style>

<script setup lang="js">
import { ref, reactive, computed } from 'vue'
import useOAuth2 from "../../composables/useOAuth2.js";

const { loginWithGoogle } = useOAuth2()

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
    message = 'Required';
  }
  else if (value) {
    switch (key) {
      case 'email':
        const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
        if (!emailRe.test(value)) message = 'Invalid email';
        break;
      case 'password':
        if (value.length < 6) message = 'Min 6 chars';
        break;
      case 'confirmPassword':
        if (value !== form.password) message = 'No match';
        break;
    }
  }

  errors[key] = message;
}

function getFieldIcon(fieldKey) {
  const icons = {
    'email': '✉️',
    'login': '👤',
    'password': '🔒',
    'confirmPassword': '✅'
  };
  return icons[fieldKey] || '📋';
}

const visibleFields = computed(() => {
  if (signIn.value) {
    return [
      { key: 'login', type: 'text', label: 'Login', placeholder: 'Enter your login' },
      { key: 'password', type: 'password', label: 'Password', placeholder: 'Enter your password' }
    ]
  }
  return [
    { key: 'email', type: 'email', label: 'Email', placeholder: 'Enter your email' },
    { key: 'login', type: 'text', label: 'Login', placeholder: 'Choose a login' },
    { key: 'password', type: 'password', label: 'Password', placeholder: 'Create a password' },
    { key: 'confirmPassword', type: 'password', label: 'Confirm Password', placeholder: 'Repeat your password' }
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
    window.location.href = '/bp-invest'
  } catch (error) {
    alert("Server error")
  }
}

async function handleRegister() {
  const keys = ['email', 'login', 'password', 'confirmPassword'];
  keys.forEach((k) => (touched[k] = true));
  keys.forEach((k) => validateField(k));

  const isValid = keys.every((k) => !errors[k] && form[k]?.toString().trim());
  if (!isValid) return;
  try {
    console.log("register new user");
    const response = await fetch('http://localhost:9001/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: form.login,
        password: form.password,
        email: form.email
      })
    })

    if (response.status === 409) {
      if (response.code === 'USER_ALREADY_EXISTS') {
        errors.login = 'This login is already taken';
        touched.login = true;
        return;
      } else {
        errors.email = 'This email is already taken';
        touched.email = true;
        return;
      }

    }

    window.location.href = '/bp-invest'
  } catch (error) {
    alert("Server error")
  }
}

function handleGoogleLogin() {
  loginWithGoogle()
}
</script>