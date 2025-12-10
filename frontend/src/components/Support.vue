<template>
  <div class="page-container">
    <div class="card content-card">
      <h1>Support</h1>
      <p class="subtitle">We're here to help you with Rocket Pop SSO</p>
      
      <section>
        <h2>Common Issues</h2>
        
        <div class="faq-item">
          <h3>Can't log in?</h3>
          <p><strong>Solution:</strong></p>
          <ul>
            <li>Verify your username and password are correct</li>
            <li>Check that Caps Lock is not enabled</li>
            <li>Clear your browser cache and cookies</li>
            <li>Contact your administrator to reset your password</li>
          </ul>
        </div>

        <div class="faq-item">
          <h3>Session expired message?</h3>
          <p><strong>Solution:</strong></p>
          <ul>
            <li>Sessions expire after 24 hours of inactivity</li>
            <li>Simply log in again to create a new session</li>
            <li>Enable "Remember Me" to extend session duration</li>
          </ul>
        </div>

        <div class="faq-item">
          <h3>Can't access certain features?</h3>
          <p><strong>Solution:</strong></p>
          <ul>
            <li>Check your user role and permissions</li>
            <li>Some features require administrator access</li>
            <li>Contact your administrator to request additional permissions</li>
          </ul>
        </div>

        <div class="faq-item">
          <h3>Page not loading or errors?</h3>
          <p><strong>Solution:</strong></p>
          <ul>
            <li>Refresh the page (Ctrl+R or Cmd+R)</li>
            <li>Check your internet connection</li>
            <li>Try a different browser</li>
            <li>Clear browser cache and restart</li>
            <li>If issue persists, contact support</li>
          </ul>
        </div>
      </section>

      <section>
        <h2>Feedback</h2>
        
        <div class="feedback-card">
          <p>
            We value your feedback! Help us improve Rocket Pop SSO by sharing your thoughts,
            suggestions, or reporting issues.
          </p>
          <p>
            Your input helps us build a better authentication platform for everyone.
          </p>
        </div>
      </section>

      <div class="button-group">
        <button @click="goBack" class="primary">Go Back</button>
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

const goToDocs = () => {
  router.push('/documentation')
}

const sendEmail = () => {
  window.location.href = 'mailto:support@rocketpop.sso?subject=Support Request'
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

.contact-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.contact-card {
  background: var(--highlight);
  border: 2px solid var(--accent);
  padding: 25px;
  border-radius: 10px;
  text-align: center;
  transition: transform 0.3s ease;
}

.contact-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.contact-icon {
  font-size: 3rem;
  margin-bottom: 15px;
}

.contact-link {
  display: block;
  color: var(--secondary);
  text-decoration: none;
  font-weight: 600;
  margin: 10px 0;
}

.contact-link:hover {
  text-decoration: underline;
}

.response-time {
  font-size: 0.85rem;
  color: var(--text-secondary);
  font-style: italic;
  margin-top: 10px;
}

button.small {
  padding: 8px 16px;
  font-size: 0.9rem;
}

.faq-item {
  background: var(--highlight);
  border-left: 4px solid var(--danger);
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 5px;
}

.faq-item ul {
  margin-left: 20px;
  margin-top: 10px;
}

.faq-item li {
  line-height: 1.8;
  margin-bottom: 8px;
}

.checklist {
  background: var(--highlight);
  padding: 25px;
  border-radius: 10px;
  border: 2px solid var(--secondary);
}

.checklist-items {
  margin-left: 20px;
  margin-top: 15px;
}

.checklist-items li {
  line-height: 2;
  font-size: 1.05rem;
}

.hours-card {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.hours-info {
  background: var(--highlight);
  padding: 20px;
  border-radius: 10px;
  border: 2px solid var(--accent);
}

.note {
  font-size: 0.9rem;
  color: var(--text-secondary);
  font-style: italic;
  margin-top: 10px;
}

.security-alert {
  background: linear-gradient(135deg, #fff5f5 0%, #ffe5e5 100%);
  border: 3px solid var(--danger);
  padding: 25px;
  border-radius: 10px;
}

.security-contact {
  font-size: 1.1rem;
  margin: 15px 0;
}

.security-contact a {
  color: var(--danger);
  text-decoration: none;
  font-weight: bold;
}

.security-contact a:hover {
  text-decoration: underline;
}

.feedback-card {
  background: linear-gradient(135deg, var(--accent) 0%, var(--secondary) 100%);
  color: white;
  padding: 30px;
  border-radius: 10px;
  text-align: center;
}

.feedback-card p {
  color: white;
  font-size: 1.1rem;
}

.button-group {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  gap: 15px;
}
</style>
