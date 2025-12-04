<template>
  <div id="app">
    <header class="app-header">
      <div class="logo-container">
        <img src="./assets/Rocket-Pop-SSO (rocket-logo).png" alt="Rocket Pop SSO" class="logo" />
        <h1>Rocket Pop SSO</h1>
      </div>
      <nav v-if="authStore.isAuthenticated" class="nav-menu">
        <button @click="router.push('/dashboard')" class="nav-button">Dashboard</button>
        <button v-if="authStore.isAdmin" @click="router.push('/admin')" class="nav-button">Admin</button>
        <button @click="handleLogout" class="nav-button logout">Logout</button>
      </nav>
    </header>
    <main class="app-main">
      <router-view />
    </main>
    <footer class="app-footer">
      <div class="footer-content">
        <div class="footer-section red">
          <p class="footer-text">Rocket Pop SSO © {{ currentYear }}</p>
          <p class="footer-text">Cool & Refreshing Authentication</p>
        </div>
        <div class="footer-section white">
          <div class="footer-links">
            <a href="#" class="footer-link">Privacy Policy</a>
            <a href="#" class="footer-link">Terms of Service</a>
          </div>
        </div>
        <div class="footer-section blue">
          <div class="footer-links">
            <a href="#" class="footer-link">Documentation</a>
            <a href="#" class="footer-link">Support</a>
          </div>
        </div>
        <div class="popsicle-stick"></div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from './stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const currentYear = computed(() => new Date().getFullYear())

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.app-header {
  background: var(--card-background);
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logo {
  height: 60px;
  width: auto;
}

h1 {
  font-size: 28px;
  font-weight: 700;
  color: var(--primary);
}

.nav-menu {
  display: flex;
  gap: 10px;
}

.nav-button {
  background: var(--primary);
  color: white;
  padding: 10px 20px;
}

.nav-button.logout {
  background: var(--danger);
}

.app-main {
  max-width: 1200px;
  margin: 0 auto;
  flex: 1;
  width: 100%;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>
