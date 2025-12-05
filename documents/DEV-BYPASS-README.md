# 🚧 Development Mode Bypass

## ⚠️ REMOVE BEFORE PRODUCTION

This file documents the development authentication bypass that allows frontend testing without a connected backend.

## What's Been Added

### 1. Authentication Bypass (`frontend/src/stores/auth.js`)
- Set `DEV_BYPASS_ENABLED = true` to enable bypass mode
- Mock authentication that accepts any username/password
- Usernames containing "admin" get admin privileges
- Mock tokens prefixed with `dev-bypass-token-`

### 2. Quick Login Buttons (`frontend/src/components/Login.vue`)
- "Login as Admin" button
- "Login as User" button
- Development mode notice banner

### 3. Mock Data Support
- **Dashboard**: Returns mock user data when backend is unavailable
- **Admin Panel**: Uses mock user list for CRUD operations
- All admin operations work with in-memory mock data

## How to Use

### Option 1: Quick Login Buttons
1. Go to the login page
2. Click "Login as Admin" or "Login as User"
3. You'll be automatically logged in and redirected

### Option 2: Manual Login
1. Enter any username (use "admin" for admin access)
2. Enter any password
3. Click Login

### Testing Admin Features
- Login with username containing "admin" (e.g., "admin", "administrator")
- Access the Admin panel from the navigation
- All CRUD operations work with mock data
- Changes persist in memory until page refresh

### Testing User Features
- Login with any username not containing "admin"
- Access Dashboard only
- Profile and password change are simulated

## Mock Users Available
- `admin` - Admin user
- `testuser` - Regular user
- `john.doe` - Regular user
- `jane.smith` - Regular user

## How to Disable (for Production)

### Step 1: Set DEV_BYPASS_ENABLED to false
In `frontend/src/stores/auth.js`:
```javascript
const DEV_BYPASS_ENABLED = false  // Change this line
```

### Step 2: Remove Dev Mode UI Elements
Search for and remove all code blocks marked with:
```
// TODO: REMOVE IN PRODUCTION
```

Files to clean:
- `frontend/src/stores/auth.js` - Remove bypass logic and dev helper methods
- `frontend/src/components/Login.vue` - Remove dev notice and quick login buttons
- `frontend/src/components/Dashboard.vue` - Remove dev bypass checks
- `frontend/src/components/AdminPanel.vue` - Remove mock data and bypass logic

### Step 3: Test with Real Backend
Ensure your Spring Boot backend is:
- Implementing all required endpoints
- Generating and validating JWT tokens
- Handling CORS properly
- Returning proper error responses

## Console Warnings

When dev mode is active, you'll see console warnings:
- `🚧 DEV MODE: Using authentication bypass`
- `🚧 Dev Mode: Using mock user data`
- `🚧 Dev Mode: Logged in as admin via bypass`

These help identify when you're in development mode.

## Benefits

✅ Test all UI components without backend  
✅ Develop frontend independently  
✅ Demo the application before backend is ready  
✅ Quick testing of admin vs user flows  
✅ No need to set up test databases  

## Remember

🚨 **DO NOT DEPLOY TO PRODUCTION WITH DEV_BYPASS_ENABLED = true** 🚨

This completely bypasses security and should only be used during development!
