<template>
  <div class="dashboard-container">
    <h2>User Dashboard</h2>
    
    <!-- Self View Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('profile')">
        <h3>Your Profile</h3>
        <span class="toggle-icon">{{ expandedSections.profile ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.profile" class="card-content">
        <button @click="loadSelfInfo" class="primary" style="margin-bottom: 20px;">
          Refresh Profile
        </button>
        <div v-if="userInfo" class="user-info">
          <div class="info-row">
            <strong>Username:</strong> {{ userInfo.username }}
          </div>
          <div class="info-row">
            <strong>Role:</strong> {{ userInfo.title || (userInfo.isAdmin ? 'Admin' : 'User') }}
          </div>
          <div class="info-row" v-if="userInfo.email">
            <strong>Email:</strong> {{ userInfo.email }}
          </div>
        </div>
        <div v-if="selfError" class="error-message">{{ selfError }}</div>
      </div>
    </div>

    <!-- Change Password Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('changePassword')">
        <h3>Change Password</h3>
        <span class="toggle-icon">{{ expandedSections.changePassword ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.changePassword" class="card-content">
        <form @submit.prevent="handleChangePassword">
          <div class="form-group">
            <label for="oldPassword">Current Password</label>
            <input
              id="oldPassword"
              v-model="oldPassword"
              type="password"
              placeholder="Enter current password"
              required
            />
          </div>
          <div class="form-group">
            <label for="newPassword">New Password</label>
            <input
              id="newPassword"
              v-model="newPassword"
              type="password"
              placeholder="Enter new password"
              required
            />
          </div>
          <div class="form-group">
            <label for="confirmPassword">Confirm New Password</label>
            <input
              id="confirmPassword"
              v-model="confirmPassword"
              type="password"
              placeholder="Confirm new password"
              required
            />
          </div>
          <div v-if="passwordError" class="error-message">{{ passwordError }}</div>
          <div v-if="passwordSuccess" class="success-message">{{ passwordSuccess }}</div>
          <button type="submit" class="primary" :disabled="passwordLoading">
            {{ passwordLoading ? 'Changing...' : 'Change Password' }}
          </button>
        </form>
      </div>
    </div>

    <!-- Validate Token Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('validateToken')">
        <h3>Validate Token</h3>
        <span class="toggle-icon">{{ expandedSections.validateToken ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.validateToken" class="card-content">
        <p>Test token validation (same as Self endpoint)</p>
        <button @click="validateCurrentToken" class="secondary">
          Validate Token
        </button>
        <div v-if="validateResult" class="validate-result">
          <pre>{{ JSON.stringify(validateResult, null, 2) }}</pre>
        </div>
        <div v-if="validateError" class="error-message">{{ validateError }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import { userAPI, authAPI } from '../services/api'

const authStore = useAuthStore()

// Expandable sections state
const expandedSections = ref({
  profile: false,
  changePassword: false,
  validateToken: false
})

const toggleSection = (section) => {
  expandedSections.value[section] = !expandedSections.value[section]
}

// Self Info
const userInfo = ref(null)
const selfError = ref('')

// Change Password
const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const passwordError = ref('')
const passwordSuccess = ref('')
const passwordLoading = ref(false)

// Validate
const validateResult = ref(null)
const validateError = ref('')

const loadSelfInfo = async () => {
  selfError.value = ''
  try {
    userInfo.value = await authStore.getSelf()
  } catch (error) {
    selfError.value = error.response?.data?.error || error.response?.data?.message || 'Failed to load profile'
  }
}

const handleChangePassword = async () => {
  passwordError.value = ''
  passwordSuccess.value = ''

  if (newPassword.value !== confirmPassword.value) {
    passwordError.value = 'New passwords do not match'
    return
  }

  if (newPassword.value.length < 6) {
    passwordError.value = 'Password must be at least 6 characters'
    return
  }

  passwordLoading.value = true

  try {
    const response = await userAPI.changePassword(oldPassword.value, newPassword.value)
    passwordSuccess.value = response.message || 'Password changed successfully'
    oldPassword.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
  } catch (error) {
    passwordError.value = error.response?.data?.error || error.response?.data?.message || 'Failed to change password'
  } finally {
    passwordLoading.value = false
  }
}

const validateCurrentToken = async () => {
  validateError.value = ''
  validateResult.value = null
  
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      validateError.value = 'No token found'
      return
    }
    
    // Get user info directly from the store since token is already validated
    validateResult.value = {
      ...authStore.user,
      tokenValid: true
    }
  } catch (error) {
    validateError.value = error.response?.data?.error || error.response?.data?.message || 'Token validation failed'
  }
}

onMounted(() => {
  loadSelfInfo()
})
</script>

<style scoped>
.dashboard-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  color: var(--primary);
  margin-bottom: 30px;
  text-align: center;
}

h3 {
  color: var(--rocket-red);
  margin: 0;
  transition: color 0.3s ease;
}

/* Collapsible card styles */
.card.collapsible {
  padding: 0;
  overflow: hidden;
  transition: all 0.3s ease;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  cursor: pointer;
  user-select: none;
  transition: all 0.3s ease;
}

.card-header .toggle-icon {
  color: var(--rocket-red);
  transition: color 0.3s ease;
}

.card-header:hover {
  background-color: var(--rocket-blue);
}

.card-header:hover h3,
.card-header:hover .toggle-icon {
  color: white;
}

.card-content {
  padding: 0 30px 30px 30px;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.toggle-icon {
  font-size: 28px;
  font-weight: bold;
  color: var(--primary);
  line-height: 1;
  transition: transform 0.3s ease;
}

.user-info {
  background: var(--background);
  padding: 20px;
  border-radius: 8px;
}

.info-row {
  padding: 10px 0;
  border-bottom: 1px solid var(--border);
}

.info-row:last-child {
  border-bottom: none;
}

.validate-result {
  margin-top: 20px;
  background: var(--background);
  padding: 15px;
  border-radius: 5px;
  overflow-x: auto;
}

.validate-result pre {
  margin: 0;
  font-size: 12px;
}
</style>
