const publicKeyPEM = `
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArkOuYMSFFatZA+PhgW6cVMVBPBdePouwHMsZYmY1vndJLQcSPzsJywdB+N5uncfgZWhTKu7ByQy/DZnpRFiMpntUCLD2c0eOZH8rqzbT+eDtemKVSmFVfz8cuWcqPJz048nqtrnKHwz/IaJvjiyBCj2BWlNgu4EDLVmDbzQJHgqFlRYad1cPf5JM6IBfZSogAO5iVcAmBTa0jz5hGiVE74QhIzes0yNrpH7VOLrVpjJo5J5dUw8iRwDVTWeNat2T+tjeMgzrQjQML2ooPasLzkEy7Jgsh3oJoAs357nKlNRWXkFzR8WUFTv+IH2rr3Hilz7Mm1thizYA2Dp0KRbypwIDAQAB
-----END PUBLIC KEY-----
`;

// async function encryptWithPublicKeyPEM(inputString) {
//     // Convert PEM to ArrayBuffer
//     const b64 = publicKeyPEM.replace(/-----.*-----/g, '').replace(/\s+/g, '');
//     const binaryDer = Uint8Array.from(atob(b64), c => c.charCodeAt(0));

//     // Import the public key
//     const publicKey = await window.crypto.subtle.importKey(
//         'spki',
//         binaryDer.buffer,
//         {
//             name: 'RSA-OAEP',
//             hash: 'SHA-256'
//         },
//         false,
//         ['encrypt']
//     );

//     // Encrypt the input string
//     const encoded = new TextEncoder().encode(inputString);
//     const encrypted = await window.crypto.subtle.encrypt(
//         { name: 'RSA-OAEP' },
//         publicKey,
//         encoded
//     );

//     // Return base64 encoded ciphertext
//     return btoa(String.fromCharCode(...new Uint8Array(encrypted)));
// }

function encryptString(username, password) {
    // Parse the public key
    const pub = KEYUTIL.getKey(publicKeyPEM);

    // Encrypt using PKCS#1 v1.5 padding
    const encryptedHex = KJUR.crypto.Cipher.encrypt(password, pub, 'RSA');
    // Convert hex to base64 for Java
    const encryptedBase64 = hex2b64(encryptedHex);

    var json_return = {
        "username": username,
        "password": encryptedBase64
    }

    return json_return;
}

// Usage:
let ctext = encryptString('test_user','NateOrSpencer?');
console.log(ctext);