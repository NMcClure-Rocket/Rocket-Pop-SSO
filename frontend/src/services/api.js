import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
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

  // GET Self - Header: token; Out: user (JSON)
  getSelf: async () => {
    const response = await api.get('/user/info')
    return response.data
  },

  // Ping - Check if backend is online
  ping: async () => {
    const response = await api.get('/ping')
    return response.data
  }
}

export const userAPI = {
  // POST Change Password - Header: token, In: old password, new password; Out: message
  changePassword: async (oldPassword, newPassword) => {
    const response = await api.post('/user/updatepassword', { oldPassword, newPassword })
    return response.data
  }
}

export const adminAPI = {
  // POST Create User - Header: admin token, In: user (JSON); Out: message (JSON)
  createUser: async (user) => {
    const response = await api.post('/admin/user/create', user)
    return response.data
  },

  // POST Create Admin - Header: admin token, In: adminuser (JSON); Out: message (JSON)
  createAdmin: async (adminUser) => {
    const response = await api.post('/admin/adminuser/create', adminUser)
    return response.data
  },

  // PUT Edit User - Header: admin token; In: user (JSON); Out: message
  editUser: async (user) => {
    const response = await api.put('/admin/user/edit', user)
    return response.data
  },

  // POST Delete User - Header: admin token; In: username; Out: message
  deleteUser: async (username) => {
    const response = await api.post(`/admin/user/delete/${username}`)
    return response.data
  },

  // GET View All Users - Header: admin token; In: username (optional filter); Out: user list (JSON)
  viewUsers: async (username = '') => {
    const params = username ? { username } : {}
    const response = await api.get('/admin/user/getall', { params })
    return response.data
  },

  // GET Get Specific User - Header: admin token; In: username; Out: user (JSON)
  getUser: async (username) => {
    const response = await api.get(`/admin/user/get/${username}`)
    return response.data
  }
}

export default api
