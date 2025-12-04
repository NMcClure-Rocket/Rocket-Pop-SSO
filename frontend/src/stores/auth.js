import { defineStore } from 'pinia'
import { authAPI } from '../services/api'

// TODO: REMOVE THIS IN PRODUCTION - Development bypass for testing
const DEV_BYPASS_ENABLED = true

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
    isAuthenticated: false,
    isAdmin: false
  }),

  actions: {
    async login(username, password) {
      // TODO: REMOVE THIS BYPASS IN PRODUCTION
      if (DEV_BYPASS_ENABLED) {
        // Development bypass - simulate successful login
        const mockToken = 'dev-bypass-token-' + Date.now()
        this.token = mockToken
        localStorage.setItem('token', mockToken)
        
        // Determine if user should be admin based on username
        const isAdminUser = username.toLowerCase().includes('admin')
        
        this.user = {
          username: username,
          email: `${username}@example.com`,
          role: isAdminUser ? 'admin' : 'user',
          isAdmin: isAdminUser
        }
        this.isAuthenticated = true
        this.isAdmin = isAdminUser
        
        console.warn('🚧 DEV MODE: Using authentication bypass. Remove before production!')
        return { success: true }
      }
      
      // Real login implementation
      try {
        const response = await authAPI.login(username, password)
        this.token = response.token
        localStorage.setItem('token', response.token)
        
        // Validate token and get user info
        await this.validateToken()
        return { success: true }
      } catch (error) {
        return { success: false, error: error.response?.data?.message || 'Login failed' }
      }
    },

    async validateToken() {
      // TODO: REMOVE THIS BYPASS IN PRODUCTION
      if (DEV_BYPASS_ENABLED && this.token && this.token.startsWith('dev-bypass-token')) {
        // Development bypass - token is valid if it exists
        return true
      }
      
      try {
        if (!this.token) {
          this.logout()
          return false
        }
        
        const user = await authAPI.validate(this.token)
        this.user = user
        this.isAuthenticated = true
        this.isAdmin = user.role === 'admin' || user.isAdmin
        return true
      } catch (error) {
        this.logout()
        return false
      }
    },

    async getSelf() {
      // TODO: REMOVE THIS BYPASS IN PRODUCTION
      if (DEV_BYPASS_ENABLED && this.token && this.token.startsWith('dev-bypass-token')) {
        // Return mock user data
        return this.user || {
          username: 'devuser',
          email: 'devuser@example.com',
          role: 'user'
        }
      }
      
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
      localStorage.removeItem('dev-bypass-user')
    },

    async initialize() {
      if (this.token) {
        await this.validateToken()
      }
    },
    
    // TODO: REMOVE THIS IN PRODUCTION - Development helper
    devBypassAsAdmin() {
      if (DEV_BYPASS_ENABLED) {
        const mockToken = 'dev-bypass-token-admin-' + Date.now()
        this.token = mockToken
        localStorage.setItem('token', mockToken)
        this.user = {
          username: 'admin',
          email: 'admin@example.com',
          role: 'admin',
          isAdmin: true
        }
        this.isAuthenticated = true
        this.isAdmin = true
        console.warn('🚧 DEV MODE: Logged in as admin via bypass')
      }
    },
    
    devBypassAsUser() {
      if (DEV_BYPASS_ENABLED) {
        const mockToken = 'dev-bypass-token-user-' + Date.now()
        this.token = mockToken
        localStorage.setItem('token', mockToken)
        this.user = {
          username: 'testuser',
          email: 'testuser@example.com',
          role: 'user',
          isAdmin: false
        }
        this.isAuthenticated = true
        this.isAdmin = false
        console.warn('🚧 DEV MODE: Logged in as user via bypass')
      }
    }
  }
})
