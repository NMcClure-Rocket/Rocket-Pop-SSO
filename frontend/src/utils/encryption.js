import { KEYUTIL, KJUR } from 'jsrsasign'

// Public key for RSA encryption
const publicKeyPEM = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqJHvVvEy9X8TcZQZKvYQ
pzYvhYYp6xGSJPTCZWN5xJ8QmZ7VpLNHF7yp8xW3VqN5pJ7YvZvHxW3QpZvJxW3V
qN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZv
JxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW
3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7Yv
ZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5
pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW
3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3Qp
ZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvH
xW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7
YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3Vq
N5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJ
xW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3
QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7Yv
ZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5
pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW
3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3Qp
ZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvH
xW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7
YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3Vq
N5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJ
xW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3
QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7Yv
ZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5
pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW
3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3Qp
ZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvH
xW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7
YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3VqN5pJ7YvZvHxW3QpZvJxW3Vq
AQIDAQAB
-----END PUBLIC KEY-----`

/**
 * Encrypt a password using RSA public key encryption
 * @param {string} username - The username
 * @param {string} password - The plain text password
 * @returns {object} Object containing username and encrypted password
 */
export function encryptPassword(username, password) {
  try {
    // Parse the public key
    const pub = KEYUTIL.getKey(publicKeyPEM)

    // Encrypt using PKCS#1 v1.5 padding (RSA)
    const encryptedHex = KJUR.crypto.Cipher.encrypt(password, pub, 'RSA')
    
    // Convert hex to base64 for Java backend
    const encryptedBase64 = hexToBase64(encryptedHex)

    return {
      username: username,
      password: encryptedBase64
    }
  } catch (error) {
    console.error('Encryption error:', error)
    throw new Error('Failed to encrypt password')
  }
}

/**
 * Convert hex string to base64
 * @param {string} hexString - Hex encoded string
 * @returns {string} Base64 encoded string
 */
function hexToBase64(hexString) {
  // Convert hex string to byte array
  const bytes = new Uint8Array(hexString.length / 2)
  for (let i = 0; i < hexString.length; i += 2) {
    bytes[i / 2] = parseInt(hexString.substr(i, 2), 16)
  }
  
  // Convert byte array to base64
  let binary = ''
  bytes.forEach(byte => binary += String.fromCharCode(byte))
  return btoa(binary)
}

/**
 * Helper function for backward compatibility with hex2b64
 */
export function hex2b64(hexString) {
  return hexToBase64(hexString)
}
