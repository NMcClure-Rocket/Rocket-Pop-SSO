<template>
  <div class="login-container">
    <div class="card login-card">
      <h2>Login to Rocket Pop SSO</h2>
      
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
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { encryptPassword } from '../utils/encryption'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const errorMessage = ref('')
const loading = ref(false)

const handleLogin = async () => {
  errorMessage.value = ''
  loading.value = true

  try {
    // Encrypt the password before sending
    const encryptedCredentials = encryptPassword(username.value, password.value)
    const result = await authStore.login(encryptedCredentials.username, encryptedCredentials.password)
    
    loading.value = false

    if (result.success) {
      router.push('/dashboard')
    } else {
      errorMessage.value = result.error
    }
  } catch (error) {
    loading.value = false
    errorMessage.value = 'Failed to encrypt password. Please try again.'
    console.error('Login error:', error)
  }
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
</style>
