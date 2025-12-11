<template>
  <div class="admin-container">
    <h2>Admin Panel</h2>

    <!-- View All Users Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('viewUsers')">
        <h3>View Users</h3>
        <span class="toggle-icon">{{ expandedSections.viewUsers ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.viewUsers" class="card-content">
        <div class="form-group">
          <label for="searchUsername">Search by Username (optional)</label>
          <input
            id="searchUsername"
            v-model="searchUsername"
            type="text"
            placeholder="Enter username to search"
          />
        </div>
        <button @click="loadUsers" class="primary">Load Users</button>
        
        <div v-if="users.length > 0" class="users-list">
          <table>
            <thead>
              <tr>
                <th>Username</th>
                <th>Role</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.username || user.id">
                <td>{{ user.username }}</td>
                <td>{{ user.title || 'User' }}</td>
                <td>{{ user.email || 'N/A' }}</td>
                <td>
                  <button @click="selectUserForEdit(user)" class="secondary small">Edit</button>
                  <button @click="confirmDelete(user)" class="danger small">Delete</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-if="viewError" class="error-message">{{ viewError }}</div>
      </div>
    </div>

    <!-- Get Specific User Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('getUser')">
        <h3>Get User Details</h3>
        <span class="toggle-icon">{{ expandedSections.getUser ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.getUser" class="card-content">
        <div class="form-group">
          <label for="getUserUsername">Username</label>
          <input
            id="getUserUsername"
            v-model="getUserUsername"
            type="text"
            placeholder="Enter username"
          />
        </div>
        <button @click="getSpecificUser" class="primary">Get User</button>
        
        <div v-if="specificUser" class="user-details">
          <pre>{{ JSON.stringify(specificUser, null, 2) }}</pre>
        </div>
        <div v-if="getUserError" class="error-message">{{ getUserError }}</div>
      </div>
    </div>

    <!-- Create User Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('createUser')">
        <h3>Create User</h3>
        <span class="toggle-icon">{{ expandedSections.createUser ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.createUser" class="card-content">
        <form @submit.prevent="handleCreateUser">
          <div class="form-row">
            <div class="form-group">
              <label for="createFirstName">First Name</label>
              <input
                id="createFirstName"
                v-model="createForm.firstName"
                type="text"
                placeholder="Enter first name"
              />
            </div>
            <div class="form-group">
              <label for="createLastName">Last Name</label>
              <input
                id="createLastName"
                v-model="createForm.lastName"
                type="text"
                placeholder="Enter last name"
              />
            </div>
          </div>
          <div class="form-group">
            <label for="createUsername">Username</label>
            <input
              id="createUsername"
              v-model="createForm.username"
              type="text"
              placeholder="Enter username"
              required
            />
          </div>
          <div class="form-group">
            <label for="createPassword">Password</label>
            <input
              id="createPassword"
              v-model="createForm.password"
              type="password"
              placeholder="Enter password"
              required
            />
          </div>
          <div class="form-group">
            <label for="createEmail">Email</label>
            <input
              id="createEmail"
              v-model="createForm.email"
              type="email"
              placeholder="Enter email"
            />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="createTitle">Title/Role</label>
              <select id="createTitle" v-model="createForm.title">
                <option value="user">User</option>
                <option value="admin">Admin</option>
                <option value="manager">Manager</option>
              </select>
            </div>
            <div class="form-group">
              <label for="createDepartment">Department</label>
              <input
                id="createDepartment"
                v-model.number="createForm.department"
                type="number"
                placeholder="Enter department ID"
              />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="createCountry">Country</label>
              <input
                id="createCountry"
                v-model="createForm.country"
                type="text"
                placeholder="Enter country"
              />
            </div>
            <div class="form-group">
              <label for="createCity">City</label>
              <input
                id="createCity"
                v-model="createForm.city"
                type="text"
                placeholder="Enter city"
              />
            </div>
          </div>
          <div class="form-group">
            <label for="createLocation">Location ID</label>
            <input
              id="createLocation"
              v-model.number="createForm.location"
              type="number"
              placeholder="Enter location ID"
            />
          </div>
          <div v-if="createError" class="error-message">{{ createError }}</div>
          <div v-if="createSuccess" class="success-message">{{ createSuccess }}</div>
          <button type="submit" class="primary">Create User</button>
        </form>
      </div>
    </div>

    <!-- Create Admin Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('createAdmin')">
        <h3>Create Admin User (Promote)</h3>
        <span class="toggle-icon">{{ expandedSections.createAdmin ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.createAdmin" class="card-content">
        <form @submit.prevent="handleCreateAdmin">
          <div class="form-row">
            <div class="form-group">
              <label for="createAdminFirstName">First Name</label>
              <input
                id="createAdminFirstName"
                v-model="createAdminForm.firstName"
                type="text"
                placeholder="Enter first name"
              />
            </div>
            <div class="form-group">
              <label for="createAdminLastName">Last Name</label>
              <input
                id="createAdminLastName"
                v-model="createAdminForm.lastName"
                type="text"
                placeholder="Enter last name"
              />
            </div>
          </div>
          <div class="form-group">
            <label for="createAdminUsername">Username</label>
            <input
              id="createAdminUsername"
              v-model="createAdminForm.username"
              type="text"
              placeholder="Enter username"
              required
            />
          </div>
          <div class="form-group">
            <label for="createAdminPassword">Password</label>
            <input
              id="createAdminPassword"
              v-model="createAdminForm.password"
              type="password"
              placeholder="Enter password"
              required
            />
          </div>
          <div class="form-group">
            <label for="createAdminEmail">Email</label>
            <input
              id="createAdminEmail"
              v-model="createAdminForm.email"
              type="email"
              placeholder="Enter email"
            />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="createAdminDepartment">Department ID</label>
              <input
                id="createAdminDepartment"
                v-model.number="createAdminForm.department"
                type="number"
                placeholder="Enter department ID"
              />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="createAdminCountry">Country</label>
              <input
                id="createAdminCountry"
                v-model="createAdminForm.country"
                type="text"
                placeholder="Enter country"
              />
            </div>
            <div class="form-group">
              <label for="createAdminCity">City</label>
              <input
                id="createAdminCity"
                v-model="createAdminForm.city"
                type="text"
                placeholder="Enter city"
              />
            </div>
          </div>
          <div class="form-group">
            <label for="createAdminLocation">Location ID</label>
            <input
              id="createAdminLocation"
              v-model.number="createAdminForm.location"
              type="number"
              placeholder="Enter location ID"
            />
          </div>
          <div v-if="createAdminError" class="error-message">{{ createAdminError }}</div>
          <div v-if="createAdminSuccess" class="success-message">{{ createAdminSuccess }}</div>
          <button type="submit" class="success">Create Admin</button>
        </form>
      </div>
    </div>

    <!-- Edit User Section -->
    <div class="card collapsible">
      <div class="card-header" @click="toggleSection('editUser')">
        <h3>Edit User</h3>
        <span class="toggle-icon">{{ expandedSections.editUser ? '−' : '+' }}</span>
      </div>
      <div v-show="expandedSections.editUser" class="card-content">
        <form @submit.prevent="handleEditUser">
  <input type="hidden" v-model="editForm.id" />
          <div class="form-row">
            <div class="form-group">
              <label for="editFirstName">First Name</label>
              <input
                id="editFirstName"
                v-model="editForm.firstName"
                type="text"
                placeholder="Enter first name"
              />
            </div>
            <div class="form-group">
              <label for="editLastName">Last Name</label>
              <input
                id="editLastName"
                v-model="editForm.lastName"
                type="text"
                placeholder="Enter last name"
              />
            </div>
          </div>
          <div class="form-group">
            <label for="editUsername">Username</label>
            <input
              id="editUsername"
              v-model="editForm.username"
              type="text"
              placeholder="Enter username"
              required
            />
          </div>
          <div class="form-group">
            <label for="editPassword">New Password (optional)</label>
            <input
              id="editPassword"
              v-model="editForm.password"
              type="password"
              placeholder="Leave blank to keep current"
            />
          </div>
          <div class="form-group">
            <label for="editEmail">Email</label>
            <input
              id="editEmail"
              v-model="editForm.email"
              type="email"
              placeholder="Enter email"
            />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="editRole">Role</label>
              <select id="editRole" v-model="editForm.role">
                <option value="user">User</option>
                <option value="admin">Admin</option>
                <option value="manager">Manager</option>
              </select>
            </div>
            <div class="form-group">
              <label for="editDepartment">Department ID</label>
              <input
                id="editDepartment"
                v-model.number="editForm.department"
                type="number"
                placeholder="Enter department ID"
              />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="editCountry">Country</label>
              <input
                id="editCountry"
                v-model="editForm.country"
                type="text"
                placeholder="Enter country"
              />
            </div>
            <div class="form-group">
              <label for="editCity">City</label>
              <input
                id="editCity"
                v-model="editForm.city"
                type="text"
                placeholder="Enter city"
              />
            </div>
          </div>
          <div class="form-group">
            <label for="editLocation">Location ID</label>
            <input
              id="editLocation"
              v-model.number="editForm.location"
              type="number"
              placeholder="Enter location ID"
            />
          </div>
          <div v-if="editError" class="error-message">{{ editError }}</div>
          <div v-if="editSuccess" class="success-message">{{ editSuccess }}</div>
          <button type="submit" class="secondary">Update User</button>
          <button type="button" @click="clearEditForm" class="danger" style="margin-left: 10px;">
            Clear
          </button>
        </form>
      </div>
    </div>

    <!-- Delete User Confirmation Modal -->
    <div v-if="deleteConfirmation" class="modal-overlay" @click="cancelDelete">
      <div class="modal" @click.stop>
        <h3>Confirm Delete</h3>
        <p>Are you sure you want to delete user: <strong>{{ deleteConfirmation.username }}</strong>?</p>
        <div class="modal-actions">
          <button @click="handleDeleteUser" class="danger">Delete</button>
          <button @click="cancelDelete" class="secondary">Cancel</button>
        </div>
        <div v-if="deleteError" class="error-message">{{ deleteError }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminAPI } from '../services/api'
import { useAuthStore } from '../stores/auth'
import { encryptPassword } from '../utils/encryption'

const authStore = useAuthStore()

// Expandable sections state
const expandedSections = ref({
  viewUsers: false,
  getUser: false,
  createUser: false,
  createAdmin: false,
  editUser: false
})

const toggleSection = (section) => {
  expandedSections.value[section] = !expandedSections.value[section]
}

// View Users
const searchUsername = ref('')
const users = ref([])
const viewError = ref('')

// Get User
const getUserUsername = ref('')
const specificUser = ref(null)
const getUserError = ref('')

// Create User
const createForm = ref({
  firstName: '',
  lastName: '',
  username: '',
  password: '',
  email: '',
  title: 'user',
  department: null,
  country: '',
  city: '',
  location: null
})
const createError = ref('')
const createSuccess = ref('')

// Create Admin
const createAdminForm = ref({
  firstName: '',
  lastName: '',
  username: '',
  password: '',
  email: '',
  title: 'admin',
  department: null,
  country: '',
  city: '',
  location: null
})
const createAdminError = ref('')
const createAdminSuccess = ref('')

// Edit User
const editForm = ref({
  firstName: '',
  lastName: '',
  username: '',
  password: '',
  email: '',
  role: 'user',
  department: null,
  country: '',
  city: '',
  location: null
})
const editError = ref('')
const editSuccess = ref('')

// Delete User
const deleteConfirmation = ref(null)
const deleteError = ref('')

const loadUsers = async () => {
  viewError.value = ''
  try {
    const response = await adminAPI.viewUsers(searchUsername.value)
    users.value = Array.isArray(response) ? response : response.users || []
  } catch (error) {
    console.error('Error loading users:', error)
    const errorMsg = error.response?.data?.error || error.response?.data?.message || error.message || 'Failed to load users'
    viewError.value = errorMsg
  }
}

const getSpecificUser = async () => {
  getUserError.value = ''
  specificUser.value = null
  
  if (!getUserUsername.value) {
    getUserError.value = 'Please enter a username'
    return
  }

  try {
    specificUser.value = await adminAPI.getUser(getUserUsername.value)
  } catch (error) {
    console.error('Error getting user:', error)
    const errorMsg = error.response?.data?.error || error.response?.data?.message || error.message || 'Failed to get user'
    getUserError.value = errorMsg
  }
}

const handleCreateUser = async () => {
  createError.value = ''
  createSuccess.value = ''

  try {
    // Encrypt password before sending
    const encryptedData = encryptPassword(createForm.value.username, createForm.value.password)
    
    const userData = {
      firstName: createForm.value.firstName,
      lastName: createForm.value.lastName,
      username: encryptedData.username,
      password: encryptedData.password,
      email: createForm.value.email,
      title: createForm.value.title,
      department: createForm.value.department,
      country: createForm.value.country,
      city: createForm.value.city,
      location: createForm.value.location
    }
    
    const response = await adminAPI.createUser(userData)
    createSuccess.value = response.message || 'User created successfully'
    createForm.value = {
      firstName: '',
      lastName: '',
      username: '',
      password: '',
      email: '',
      title: 'user',
      department: null,
      country: '',
      city: '',
      location: null
    }
    loadUsers()
  } catch (error) {
    console.error('Error creating user:', error)
    createError.value = error.response?.data?.message || error.message || 'Failed to create user'
  }
}

const handleCreateAdmin = async () => {
  createAdminError.value = ''
  createAdminSuccess.value = ''

  try {
    // Encrypt password before sending
    const encryptedData = encryptPassword(createAdminForm.value.username, createAdminForm.value.password)
    
    const userData = {
      firstName: createAdminForm.value.firstName,
      lastName: createAdminForm.value.lastName,
      username: encryptedData.username,
      password: encryptedData.password,
      email: createAdminForm.value.email,
      title: 'admin',
      department: createAdminForm.value.department,
      country: createAdminForm.value.country,
      city: createAdminForm.value.city,
      location: createAdminForm.value.location
    }
    
    const response = await adminAPI.createAdmin(userData)
    createAdminSuccess.value = response.message || 'Admin user created successfully'
    createAdminForm.value = {
      firstName: '',
      lastName: '',
      username: '',
      password: '',
      email: '',
      title: 'admin',
      department: null,
      country: '',
      city: '',
      location: null
    }
    loadUsers()
  } catch (error) {
    console.error('Error creating admin:', error)
    createAdminError.value = error.response?.data?.message || error.message || 'Failed to create admin'
  }
}

const selectUserForEdit = (user) => {
  editForm.value = {
  id: user.id,
    firstName: user.firstName || user.first_name || '',
    lastName: user.lastName || user.last_name || '',
    username: user.username,
    password: '',
    email: user.email || '',
    role: user.title || 'user',
    department: user.department || null,
    country: user.country || '',
    city: user.city || '',
    location: user.location || null
  }
  editError.value = ''
  editSuccess.value = ''
  
  // Open the edit section
  expandedSections.value.editUser = true
  
  // Scroll to edit section
  setTimeout(() => {
    const editSection = document.querySelector('.card.collapsible:last-of-type')
    if (editSection) {
      editSection.scrollIntoView({ behavior: 'smooth', block: 'start' })
    }
  }, 100)
}

const handleEditUser = async () => {
  editError.value = ''
  editSuccess.value = ''

  try {
    const userData = {
        id: editForm.value.id,
      firstName: editForm.value.firstName,
      lastName: editForm.value.lastName,
      username: editForm.value.username,
      email: editForm.value.email,
      title: editForm.value.role,
      department: editForm.value.department,
      country: editForm.value.country,
      city: editForm.value.city,
      location: editForm.value.location
    }
    console.log("userData",userData)
    
    // Only encrypt password if provided
    if (editForm.value.password) {
      console.log("edit form has password")
      const encryptedData = encryptPassword(editForm.value.username, editForm.value.password)
      userData.password = encryptedData.password
    }
    
    console.log("sending update")
    const response = await adminAPI.editUser(userData)
    editSuccess.value = response.message || 'User updated successfully'
    loadUsers()
  } catch (error) {
    console.error('Error editing user:', error)
    editError.value = error.response?.data?.message || error.message || 'Failed to update user'
  }
}

const clearEditForm = () => {
  editForm.value = {
    firstName: '',
    lastName: '',
    username: '',
    password: '',
    email: '',
    role: 'user',
    department: null,
    country: '',
    city: '',
    location: null
  }
  editError.value = ''
  editSuccess.value = ''
}

const confirmDelete = (user) => {
  deleteConfirmation.value = user
  deleteError.value = ''
}

const cancelDelete = () => {
  deleteConfirmation.value = null
  deleteError.value = ''
}

const handleDeleteUser = async () => {
  deleteError.value = ''

  try {
    const userId = deleteConfirmation.value.id || deleteConfirmation.value.username
    await adminAPI.deleteUser(userId)
    cancelDelete()
    loadUsers()
  } catch (error) {
    deleteError.value = error.response?.data?.message || 'Failed to delete user'
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.admin-container {
  max-width: 1000px;
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

.users-list {
  margin-top: 20px;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid var(--border);
}

th {
  background: var(--primary);
  color: white;
  font-weight: 600;
}

tr:hover {
  background: var(--background);
}

button.small {
  padding: 6px 12px;
  font-size: 12px;
  margin-right: 5px;
}

.user-details {
  margin-top: 20px;
  background: var(--background);
  padding: 15px;
  border-radius: 5px;
  overflow-x: auto;
}

.user-details pre {
  margin: 0;
  font-size: 12px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 30px;
  border-radius: 10px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.modal h3 {
  margin-bottom: 15px;
}

.modal p {
  margin-bottom: 20px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>
