# Rocket Pop SSO

A complete SSO (Single Sign-On) user management system with a Spring Boot backend API and Vue 3 frontend application.

## Project Structure

```
Rocket-Pop-SSO/
├── frontend/                    # Vue 3 Frontend Application
│   ├── src/
│   │   ├── assets/             # Static assets (logo, images)
│   │   ├── components/         # Vue components
│   │   │   ├── Login.vue
│   │   │   ├── Dashboard.vue
│   │   │   └── AdminPanel.vue
│   │   ├── router/             # Vue Router configuration
│   │   ├── services/           # API service layer
│   │   ├── stores/             # Pinia state management
│   │   └── style.css           # Global styles (Single Source of Truth for colors)
│   └── package.json
├── src/                         # Spring Boot Backend
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/rocketpop/
│   │   └── resources/
│   └── test/
├── pom.xml
├── start-all.ps1               # Start both frontend & backend
├── start-frontend.ps1          # Start Vue dev server only
└── start-backend.ps1           # Start Spring Boot only
```

## Quick Start

### Prerequisites
- **Backend**: Java 21, Maven
- **Frontend**: Node.js (v16 or higher), npm

### Option 1: Start Everything at Once

```powershell
.\start-all.ps1
```

This will start both the backend (port 42068) and frontend (port 42067) in separate windows.

### Option 2: Start Servers Separately

**Terminal 1 - Backend:**
```powershell
.\start-backend.ps1
```

**Terminal 2 - Frontend:**
```powershell
.\start-frontend.ps1
```

### Option 3: Manual Start

**Backend:**
```powershell
.\mvnw.cmd spring-boot:run
```

```bash
mvn spring-boot:run
```

**Frontend:**
```powershell
cd frontend
npm install  # First time only
npm run dev
```

### Access the Application

Once both servers are running, open your browser to:
**http://localhost:42067**

## Features

### User Features
- ✅ **Login** - Username/password authentication with JWT token
- ✅ **View Profile** - View own user information (GET /self)
- ✅ **Change Password** - Update password securely
- ✅ **Validate Token** - Test token validation

### Admin Features
- ✅ **View Users** - List all users with optional search
- ✅ **Get User** - Retrieve specific user details
- ✅ **Create User** - Add new regular users
- ✅ **Create Admin** - Promote users to admin role
- ✅ **Edit User** - Update user information and roles
- ✅ **Delete User** - Remove users from the system

## API Endpoints

### Authentication
- `POST /login` - Login with username/password → returns JWT token
- `GET /self` - Get current user info (requires token)
- `GET /validate` - Validate token → returns user info

### User Management
- `POST /change-password` - Change user password (requires token)

### Admin Operations (require admin token)
- `POST /create` - Create new user
- `POST /createadmin` - Create admin user
- `PUT /edit` - Edit user
- `POST /delete` - Delete user by userid
- `GET /view-users` - View all users (optional username parameter)
- `GET /get-user` - Get specific user by username

## Theme & Colors

The application uses a custom color palette defined in `frontend/src/style.css` (Single Source of Truth):

- **#df1a1a** - Deep red for danger/delete actions
- **#ae4343** - Primary color for buttons and headers
- **#785e5e** - Soft blue for subtle emphasis
- **#817979** - Secondary color for section headers
- **#8b959f** - Accent color for highlights

View `frontend/color-reference.html` for a visual color reference.

## Configuration

### Backend
- Port: `42068` (configured in `src/main/resources/application.properties`)
- CORS enabled for `http://localhost:42067`

### Frontend
- Port: `42067` (configured in `frontend/vite.config.js`)
- API proxy: All `/api/*` requests forwarded to backend at `http://localhost:42068`
- State management: Pinia
- Routing: Vue Router with authentication guards

## Technology Stack

### Backend
- Spring Boot 4.0.0
- Java 21
- Maven

### Frontend
- Vue 3 (Composition API)
- Vite
- Vue Router
- Pinia (state management)
- Axios (HTTP client)

## Development

### Frontend Development

```powershell
cd frontend

# Install dependencies
npm install

# Start dev server with hot reload
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

### Backend Development

```powershell
# Run with Maven wrapper
.\mvnw.cmd spring-boot:run

# Build
.\mvnw.cmd clean package

# Run tests
.\mvnw.cmd test
```
```bash
# Start database
sudo docker-compose up -d

# Run
mvn spring-boot:run

# Build
mvn clean package

# Run tests
# Note: does not require the docker container to be running
mvn test
```


## Next Steps

1. **Implement Backend API Endpoints** - The frontend is complete and ready to connect
2. **Add JWT Authentication** - Implement token generation and validation
3. **Add Database** - Connect to a database for user persistence
4. **Replace Logo** - Add your actual `Rocket-Pop-SSO(rocket-logo).png` to `frontend/src/assets/`

## License

Copyright © Rocket Software, Inc.
