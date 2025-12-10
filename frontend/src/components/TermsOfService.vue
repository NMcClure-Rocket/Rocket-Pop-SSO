<template>
  <div class="page-container">
    <div class="card content-card">
      <h1>Terms of Service</h1>
      <p class="last-updated">Last Updated: December 9, 2025</p>
      
      <section>
        <h2>1. Acceptance of Terms</h2>
        <p>
          By accessing and using Rocket Pop SSO ("the Service"), you agree to be bound by these Terms of Service.
          If you do not agree to these terms, please do not use the Service.
        </p>
      </section>

      <section>
        <h2>2. Description of Service</h2>
        <p>
          Rocket Pop SSO provides a single sign-on authentication platform that allows users to:
        </p>
        <ul>
          <li>Create and manage authentication accounts</li>
          <li>Access multiple applications with a single login</li>
          <li>Manage user permissions and access control</li>
          <li>Monitor authentication activity and security</li>
        </ul>
      </section>

      <section>
        <h2>3. User Accounts</h2>
        <p>
          To use the Service, you must:
        </p>
        <ul>
          <li>Provide accurate and complete registration information</li>
          <li>Maintain the security of your password and account</li>
          <li>Notify us immediately of any unauthorized access</li>
          <li>Be responsible for all activities under your account</li>
        </ul>
        <p>
          You may not share your account credentials or allow others to access your account.
        </p>
      </section>

      <section>
        <h2>4. Acceptable Use</h2>
        <p>
          You agree not to:
        </p>
        <ul>
          <li>Use the Service for any illegal or unauthorized purpose</li>
          <li>Attempt to gain unauthorized access to any systems or networks</li>
          <li>Interfere with or disrupt the Service or servers</li>
          <li>Use automated systems to access the Service without permission</li>
          <li>Impersonate another person or entity</li>
          <li>Transmit any malware, viruses, or harmful code</li>
        </ul>
      </section>

      <section>
        <h2>5. Service Availability</h2>
        <p>
          We strive to maintain 99.9% uptime, but we do not guarantee uninterrupted access to the Service.
          We reserve the right to modify, suspend, or discontinue the Service at any time with reasonable notice.
        </p>
      </section>

      <section>
        <h2>6. Data and Privacy</h2>
        <p>
          Your use of the Service is also governed by our Privacy Policy. We collect and process data
          as described in the Privacy Policy to provide and improve the Service.
        </p>
      </section>

      <section>
        <h2>7. Intellectual Property</h2>
        <p>
          The Service, including all content, features, and functionality, is owned by Rocket Pop SSO
          and is protected by international copyright, trademark, and other intellectual property laws.
        </p>
      </section>

      <section>
        <h2>8. Limitation of Liability</h2>
        <p>
          To the maximum extent permitted by law, Rocket Pop SSO shall not be liable for any indirect,
          incidental, special, consequential, or punitive damages resulting from your use of the Service.
        </p>
      </section>

      <section>
        <h2>9. Termination</h2>
        <p>
          We reserve the right to terminate or suspend your account at any time for:
        </p>
        <ul>
          <li>Violation of these Terms of Service</li>
          <li>Fraudulent or illegal activity</li>
          <li>Extended period of inactivity</li>
          <li>Request from law enforcement</li>
        </ul>
      </section>

      <section>
        <h2>10. Changes to Terms</h2>
        <p>
          We may modify these Terms of Service at any time. We will notify users of significant changes
          via email or through the Service. Continued use of the Service after changes constitutes
          acceptance of the modified terms.
        </p>
      </section>

      <section>
        <h2>11. Contact Information</h2>
        <p>
          For questions about these Terms of Service, please contact us at:
          <br />
          Email: <a href="mailto:legal@rocketpop.sso">legal@rocketpop.sso</a>
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
