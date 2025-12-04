import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const authAPI = {
  // POST Login - In: username, password; Out: token (JSON)
  login: async (username, password) => {
    const response = await api.post('/login', { username, password })
    return response.data
  },

  // Validate - In: token; Out: user (JSON)
  validate: async (token) => {
    const response = await api.get('/validate', {
      headers: { Authorization: `Bearer ${token}` }
    })
    return response.data
  },

  // GET Self - Header: token; Out: user (JSON) (same as validate)
  getSelf: async () => {
    const response = await api.get('/self')
    return response.data
  }
}

export const userAPI = {
  // POST Change Password - Header: token, In: old password, new password; Out: message
  changePassword: async (oldPassword, newPassword) => {
    const response = await api.post('/change-password', { oldPassword, newPassword })
    return response.data
  }
}

export const adminAPI = {
  // POST Create - Header: admin token, In: user (JSON); Out: message (JSON)
  createUser: async (user) => {
    const response = await api.post('/create', user)
    return response.data
  },

  // POST CreateAdmin - Header: admin token, In: adminuser (JSON); Out: message (JSON)
  createAdmin: async (adminUser) => {
    const response = await api.post('/createadmin', adminUser)
    return response.data
  },

  // PUT Edit - Header: admin token; In: user (JSON); Out: message
  editUser: async (user) => {
    const response = await api.put('/edit', user)
    return response.data
  },

  // POST Delete - Header: admin token; In: userid; Out: message
  deleteUser: async (userId) => {
    const response = await api.post('/delete', { userId })
    return response.data
  },

  // GET View Users - Header: admin token; In: username; Out: user list (JSON)
  viewUsers: async (username = '') => {
    const response = await api.get('/view-users', { params: { username } })
    return response.data
  },

  // GET Get User - Header: admin token; In: username; Out: user (JSON)
  getUser: async (username) => {
    const response = await api.get('/get-user', { params: { username } })
    return response.data
  }
}

export default api
