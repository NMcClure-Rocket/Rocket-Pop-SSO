<template>
  <div class="page-container">
    <div class="card content-card">
      <h1>Privacy Policy</h1>
      <p class="last-updated">Last Updated: December 9, 2025</p>
      
      <section>
        <h2>1. Information We Collect</h2>
        <p>
          Rocket Pop SSO collects and processes the following information:
        </p>
        <ul>
          <li><strong>Account Information:</strong> Username, email address, and encrypted password</li>
          <li><strong>Authentication Data:</strong> Login timestamps, session tokens, and authentication logs</li>
          <li><strong>Usage Information:</strong> Application access patterns and feature usage</li>
        </ul>
      </section>

      <section>
        <h2>2. How We Use Your Information</h2>
        <p>
          We use the collected information to:
        </p>
        <ul>
          <li>Provide secure authentication services</li>
          <li>Maintain and improve our SSO platform</li>
          <li>Monitor security and prevent unauthorized access</li>
          <li>Comply with legal obligations</li>
        </ul>
      </section>

      <section>
        <h2>3. Data Security</h2>
        <p>
          We implement industry-standard security measures to protect your data:
        </p>
        <ul>
          <li>All passwords are encrypted using bcrypt hashing</li>
          <li>Session tokens are securely generated and stored</li>
          <li>Communications are protected with HTTPS/TLS</li>
          <li>Regular security audits and updates</li>
        </ul>
      </section>

      <section>
        <h2>4. Data Sharing</h2>
        <p>
          We do not sell or share your personal information with third parties except:
        </p>
        <ul>
          <li>When required by law or legal process</li>
          <li>To protect our rights or the safety of users</li>
          <li>With your explicit consent</li>
        </ul>
      </section>

      <section>
        <h2>5. Your Rights</h2>
        <p>
          You have the right to:
        </p>
        <ul>
          <li>Access your personal information</li>
          <li>Request correction of inaccurate data</li>
          <li>Request deletion of your account and data</li>
          <li>Opt-out of non-essential data processing</li>
        </ul>
      </section>

      <section>
        <h2>6. Data Retention</h2>
        <p>
          We retain your information for as long as your account is active or as needed to provide services.
          Authentication logs are retained for 90 days for security purposes.
        </p>
      </section>

      <section>
        <h2>7. Contact Us</h2>
        <p>
          If you have questions about this Privacy Policy, please contact us at:
          <br />
          Email: <a href="mailto:privacy@rocketpop.sso">privacy@rocketpop.sso</a>
        </p>
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
</script>

<style scoped>
.page-container {
  max-width: 900px;
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

.last-updated {
  color: var(--text-secondary);
  font-style: italic;
  margin-bottom: 30px;
  font-size: 0.9rem;
}

section {
  margin-bottom: 30px;
}

h2 {
  color: var(--secondary);
  margin-bottom: 15px;
  font-size: 1.5rem;
}

p {
  line-height: 1.6;
  margin-bottom: 15px;
  color: var(--text-primary);
}

ul {
  margin-left: 20px;
  margin-bottom: 15px;
}

li {
  line-height: 1.8;
  margin-bottom: 8px;
}

a {
  color: var(--secondary);
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.button-group {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style>
