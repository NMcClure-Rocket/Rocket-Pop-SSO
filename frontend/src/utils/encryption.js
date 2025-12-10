//import { KEYUTIL, KJUR } from 'jsrsasign'

// Public key for RSA encryption - must match the backend's public.key in application.properties
const publicKeyPEM = `
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArkOuYMSFFatZA+PhgW6cVMVBPBdePouwHMsZYmY1vndJLQcSPzsJywdB+N5uncfgZWhTKu7ByQy/DZnpRFiMpntUCLD2c0eOZH8rqzbT+eDtemKVSmFVfz8cuWcqPJz048nqtrnKHwz/IaJvjiyBCj2BWlNgu4EDLVmDbzQJHgqFlRYad1cPf5JM6IBfZSogAO5iVcAmBTa0jz5hGiVE74QhIzes0yNrpH7VOLrVpjJo5J5dUw8iRwDVTWeNat2T+tjeMgzrQjQML2ooPasLzkEy7Jgsh3oJoAs357nKlNRWXkFzR8WUFTv+IH2rr3Hilz7Mm1thizYA2Dp0KRbypwIDAQAB
-----END PUBLIC KEY-----
`;

/**
 * Encrypt a password using RSA public key encryption
 * @param {string} username - The username
 * @param {string} password - The plain text password
 * @returns {object} Object containing username and encrypted password
 */
export function encryptPassword(username, password) {
  try {
    console.log('Starting encryption...')
    console.log('Public key PEM:', publicKeyPEM)
    
    // Parse the public key
    const pub = KEYUTIL.getKey(publicKeyPEM)
    console.log('Public key parsed:', pub)

    // Encrypt using PKCS#1 v1.5 padding (RSA)
    // Using RSAKey.encrypt instead of KJUR.crypto.Cipher.encrypt
    const encryptedHex = KJUR.crypto.Cipher.encrypt(password, pub, 'RSA')
    console.log('Encrypted hex:', encryptedHex)
    
    // Convert hex to base64 for Java backend
    const encryptedBase64 = hexToBase64(encryptedHex)
    console.log('Encrypted base64:', encryptedBase64)

    return {
      username: username,
      password: encryptedBase64
    }
  } catch (error) {
    console.error('Encryption error details:', error)
    console.error('Error message:', error.message)
    console.error('Error stack:', error.stack)
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
