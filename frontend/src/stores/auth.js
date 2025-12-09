import { defineStore } from 'pinia'
import { authAPI } from '../services/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
    isAuthenticated: false,
    isAdmin: false
  }),

  actions: {
    async login(username, password) {
      try {
        const response = await authAPI.login(username, password)
        this.token = response.token
        localStorage.setItem('token', response.token)
        
        // Validate token and get user info
        await this.validateToken()
        return { success: true }
      } catch (error) {
        return { success: false, error: error.response?.data?.error || error.response?.data?.message || 'Login failed' }
      }
    },

    async validateToken() {
      try {
        if (!this.token) {
          this.logout()
          return false
        }
        
        // Use getSelf to validate token and get user info
        const user = await authAPI.getSelf()
        this.user = user
        this.isAuthenticated = true
        this.isAdmin = user.role === 'admin'
        return true
      } catch (error) {
        this.logout()
        return false
      }
    },

    async getSelf() {
      try {
        const user = await authAPI.getSelf()
        this.user = user
        return user
      } catch (error) {
        throw error
      }
    },

    logout() {
      this.user = null
      this.token = null
      this.isAuthenticated = false
      this.isAdmin = false
      localStorage.removeItem('token')
    },

    async initialize() {
      if (this.token) {
        await this.validateToken()
      }
    }
  }
})
