<template>
  <div class="page-container">
    <div class="card content-card">
      <h1>Documentation</h1>
      <p class="subtitle">Complete guide to using Rocket Pop SSO</p>
      
      <section>
        <h2>Getting Started</h2>
        <p>
          Welcome to Rocket Pop SSO! This guide will help you get started with our authentication platform.
        </p>
        <div class="doc-card">
          <h3>Quick Start</h3>
          <ol>
            <li>Create an account or log in with your credentials</li>
            <li>Access your dashboard to view connected applications</li>
            <li>Administrators can manage users and permissions from the Admin Panel</li>
          </ol>
        </div>
      </section>

      <section>
        <h2>User Guide</h2>
        
        <div class="doc-card">
          <h3>Logging In</h3>
          <p>
            Enter your username and password on the login page. Your session will remain active
            for secure access to all connected applications.
          </p>
        </div>

        <div class="doc-card">
          <h3>Dashboard</h3>
          <p>
            The dashboard displays:
          </p>
          <ul>
            <li>Your user information and role</li>
            <li>List of applications you have access to</li>
            <li>Quick access to account settings</li>
          </ul>
        </div>

        <div class="doc-card">
          <h3>Password Security</h3>
          <p>
            Your password is encrypted using industry-standard bcrypt hashing. We recommend:
          </p>
          <ul>
            <li>Using a strong, unique password</li>
            <li>Changing your password regularly</li>
            <li>Never sharing your credentials</li>
            <li>Logging out when using shared devices</li>
          </ul>
        </div>
      </section>

      <section>
        <h2>Administrator Guide</h2>
        
        <div class="doc-card">
          <h3>Admin Panel Access</h3>
          <p>
            Administrators have access to the Admin Panel where they can:
          </p>
          <ul>
            <li>View and search all users</li>
            <li>Create new user accounts</li>
            <li>Update user information and roles</li>
            <li>Delete user accounts</li>
            <li>Manage user permissions</li>
          </ul>
        </div>

        <div class="doc-card">
          <h3>User Management</h3>
          <p>
            <strong>Creating Users:</strong> Fill in the username, password, email, and full name.
            Optionally assign admin privileges.
          </p>
          <p>
            <strong>Editing Users:</strong> Click the edit icon next to any user to modify their information.
            Changes are saved immediately.
          </p>
          <p>
            <strong>Deleting Users:</strong> Click the delete icon to remove a user. This action cannot be undone.
          </p>
        </div>

        <div class="doc-card">
          <h3>Role-Based Access Control</h3>
          <p>
            Rocket Pop SSO supports two user roles:
          </p>
          <ul>
            <li><strong>Regular User:</strong> Can access their dashboard and connected applications</li>
            <li><strong>Administrator:</strong> Has full access to user management and system configuration</li>
          </ul>
        </div>
      </section>

      <section>
        <h2>API Integration</h2>
        
        <div class="doc-card">
          <h3>Authentication Endpoints</h3>
          <p>
            <code>POST /api/auth/login</code> - Authenticate user and receive token
          </p>
          <p>
            <code>POST /api/auth/logout</code> - Invalidate current session
          </p>
          <p>
            <code>GET /api/auth/validate</code> - Validate current session token
          </p>
        </div>

        <div class="doc-card">
          <h3>User Management Endpoints</h3>
          <p>
            <code>GET /api/users</code> - List all users (admin only)
          </p>
          <p>
            <code>POST /api/users</code> - Create new user (admin only)
          </p>
          <p>
            <code>PUT /api/users/:id</code> - Update user (admin only)
          </p>
          <p>
            <code>DELETE /api/users/:id</code> - Delete user (admin only)
          </p>
        </div>
      </section>

      <section>
        <h2>Security Features</h2>
        
        <div class="doc-card">
          <h3>Authentication Security</h3>
          <ul>
            <li>Bcrypt password hashing with salt rounds</li>
            <li>Secure session token generation</li>
            <li>HTTPS/TLS encryption for all communications</li>
            <li>Session timeout and automatic logout</li>
            <li>Protection against brute force attacks</li>
          </ul>
        </div>

        <div class="doc-card">
          <h3>Data Protection</h3>
          <ul>
            <li>All sensitive data encrypted at rest</li>
            <li>Regular security audits and updates</li>
            <li>Compliance with industry security standards</li>
            <li>Audit logging for administrative actions</li>
          </ul>
        </div>
      </section>

      <section>
        <h2>Frequently Asked Questions</h2>
        
        <div class="doc-card">
          <h3>How do I reset my password?</h3>
          <p>
            Contact your system administrator to reset your password. Password reset functionality
            can be enabled in the admin settings.
          </p>
        </div>

        <div class="doc-card">
          <h3>Why can't I access certain applications?</h3>
          <p>
            Access is controlled by your user role and permissions. Contact your administrator
            to request access to additional applications.
          </p>
        </div>

        <div class="doc-card">
          <h3>How long do sessions last?</h3>
          <p>
            Sessions remain active for 24 hours by default. You can manually log out at any time
            to end your session immediately.
          </p>
        </div>
      </section>

      <div class="button-group">
        <button @click="goBack" class="primary">Go Back</button>
        <button @click="goToSupport" class="secondary">Need More Help?</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}

const goToSupport = () => {
  router.push('/support')
}
</script>

<style scoped>
.page-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.content-card {
  padding: 40px;
}

h1 {
  color: var(--primary);
  margin-bottom: 10px;
  font-size: 2.5rem;
}

.subtitle {
  color: var(--text-secondary);
  font-size: 1.1rem;
  margin-bottom: 30px;
}

section {
  margin-bottom: 40px;
}

h2 {
  color: var(--secondary);
  margin-bottom: 20px;
  font-size: 1.8rem;
  border-bottom: 2px solid var(--accent);
  padding-bottom: 10px;
}

h3 {
  color: var(--primary);
  margin-bottom: 10px;
  font-size: 1.3rem;
}

p {
  line-height: 1.6;
  margin-bottom: 15px;
  color: var(--text-primary);
}

.doc-card {
  background: var(--highlight);
  border-left: 4px solid var(--secondary);
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 5px;
}

ul, ol {
  margin-left: 20px;
  margin-bottom: 15px;
}

li {
  line-height: 1.8;
  margin-bottom: 8px;
}

code {
  background: var(--primary);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
}

.button-group {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  gap: 15px;
}
</style>
