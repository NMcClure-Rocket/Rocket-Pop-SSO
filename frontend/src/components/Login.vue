<template>
  <div class="login-container">
    <div class="card login-card">
      <h2>Login to Rocket Pop SSO</h2>
      
      <!-- TODO: REMOVE IN PRODUCTION - Development Mode Notice -->
      <div class="dev-notice">
        🚧 <strong>DEV MODE</strong> - Authentication bypass enabled<br>
        <small>Enter any username (use "admin" for admin access)</small>
      </div>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">Username</label>
          <input
            id="username"
            v-model="username"
            type="text"
            placeholder="Enter your username"
            required
          />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="Enter your password"
            required
          />
        </div>
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        <button type="submit" class="primary login-button" :disabled="loading">
          {{ loading ? 'Logging in...' : 'Login' }}
        </button>
      </form>
      
      <!-- TODO: REMOVE IN PRODUCTION - Quick Login Buttons -->
      <div class="dev-shortcuts">
        <p style="margin-bottom: 10px; font-size: 12px; color: #666;">Quick Login:</p>
        <button @click="quickLoginAdmin" type="button" class="secondary" style="margin-right: 10px;">
          Login as Admin
        </button>
        <button @click="quickLoginUser" type="button" class="secondary">
          Login as User
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const errorMessage = ref('')
const loading = ref(false)

const handleLogin = async () => {
  errorMessage.value = ''
  loading.value = true

  const result = await authStore.login(username.value, password.value)
  
  loading.value = false

  if (result.success) {
    router.push('/dashboard')
  } else {
    errorMessage.value = result.error
  }
}

// TODO: REMOVE IN PRODUCTION - Quick login helpers
const quickLoginAdmin = () => {
  username.value = 'admin'
  password.value = 'password'
  handleLogin()
}

const quickLoginUser = () => {
  username.value = 'testuser'
  password.value = 'password'
  handleLogin()
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}

.login-card {
  max-width: 400px;
  width: 100%;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: var(--primary);
}

.login-button {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  margin-top: 10px;
}

.login-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* TODO: REMOVE IN PRODUCTION - Dev mode styles */
.dev-notice {
  background: #fff3cd;
  border: 2px solid #ffc107;
  border-radius: 5px;
  padding: 15px;
  margin-bottom: 20px;
  text-align: center;
  color: #856404;
}

.dev-shortcuts {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px dashed #e0e0e0;
  text-align: center;
}

.dev-shortcuts button {
  padding: 8px 16px;
  font-size: 12px;
}
</style>
