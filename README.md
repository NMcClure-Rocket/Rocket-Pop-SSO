# Rocket Pop SSO

A complete Single Sign-On (SSO) user management system with a Spring Boot backend API, Vue 3 frontend application, and MySQL database. Features JWT authentication, RSA password encryption, and comprehensive admin user management capabilities.

## Project Structure

```
Rocket-Pop-SSO/
├── frontend/                    # Vue 3 Frontend Application
│   ├── src/
│   │   ├── assets/             # Static assets (logo, images)
│   │   ├── components/         # Vue components
│   │   │   ├── Login.vue       # Login page with RSA encryption
│   │   │   ├── Dashboard.vue   # User dashboard
│   │   │   └── AdminPanel.vue  # Admin user management
│   │   ├── router/             # Vue Router with auth guards
│   │   ├── services/           # API service layer (Axios)
│   │   ├── stores/             # Pinia state management
│   │   ├── utils/              # Utility functions
│   │   │   └── encryption.js   # RSA password encryption (jsrsasign)
│   │   └── style.css           # Global styles & color palette
│   ├── nginx.conf              # Nginx config for production
│   ├── Dockerfile              # Frontend Docker build
│   └── package.json
├── src/                         # Spring Boot Backend
│   ├── main/
│   │   ├── java/com/example/rocketpop/
│   │   │   ├── config/         # Security & CORS configuration
│   │   │   ├── controller/     # REST API endpoints
│   │   │   ├── entity/         # JPA entities
│   │   │   ├── model/          # Domain models
│   │   │   ├── repository/     # Database layer
│   │   │   ├── service/        # Business logic
│   │   │   └── util/           # JWT & password hashing utilities
│   │   └── resources/
│   │       ├── application.properties  # Configuration
│   │       └── schema.sql      # Database schema
│   └── test/                    # JUnit tests (47 tests, all passing)
├── scripts/                     # Database initialization scripts
│   └── default_users.sql       # Default admin/test users
├── .github/workflows/           # CI/CD
│   ├── test.yml                # Automated testing on push
│   └── lint.yml                # Code linting
├── docker-compose.yml          # Multi-container orchestration
├── Dockerfile                  # Backend Docker build
├── pom.xml                     # Maven dependencies
├── start-all.ps1              # Start both frontend & backend (Windows)
├── start-frontend.ps1         # Start Vue dev server only
├── start-backend.ps1          # Start Spring Boot only
├── DEPLOYMENT.md              # Docker deployment guide
└── README.md                  # This file
```

## Quick Start

### Prerequisites
- **Backend**: Java 21, Maven
- **Frontend**: Node.js (v16+), npm
- **Database**: MySQL 8.0 (or Docker)

### Option 1: Docker Compose (Recommended)

Start the entire stack (frontend, backend, database) with one command:

```powershell
docker compose up --build -d
```

Access the application at **http://localhost:42067**

See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed Docker instructions.

### Option 2: Quick Start Scripts (Windows)

**Start everything:**
```powershell
.\start-all.ps1
```

**Or start servers separately:**

Terminal 1 - Backend:
```powershell
.\start-backend.ps1
```

Terminal 2 - Frontend:
```powershell
.\start-frontend.ps1
```

### Option 3: Manual Start

**Database (Docker):**
```bash
docker compose up mysql -d
```

**Backend:**
```powershell
# Windows
.\mvnw.cmd spring-boot:run
```

```bash
# Linux/Mac
mvn spring-boot:run
```

**Frontend:**
```powershell
cd frontend
npm install  # First time only
npm run dev
```

### Access Points

- **Frontend UI**: http://localhost:42067
- **Backend API**: http://localhost:42068
- **MySQL Database**: localhost:3306

### Default Credentials

After running database initialization:
- **Admin**: `admin` / `admin123`
- **Test User**: `testuser` / `password123`

## Features

### Security
- **RSA Encryption**: Passwords encrypted client-side before transmission
- **JWT Tokens**: Separate tokens for users and admins
- **Salted Hashing**: Server-side password hashing with unique salts
- **CORS Protection**: Configured for specific frontend origins
- **Auth Guards**: Route protection on frontend and backend

### User Features
- ✅ **Login** - Username/password authentication with JWT token
- ✅ **View Profile** - View own user information
- ✅ **Change Password** - Update password securely with RSA encryption
- ✅ **Token Validation** - Automatic token refresh and validation

### Admin Features
- ✅ **View All Users** - List all users with search/filter
- ✅ **Get User Details** - Retrieve specific user information
- ✅ **Create User** - Add new regular users
- ✅ **Create Admin** - Promote users to admin role
- ✅ **Edit User** - Update user information including:
  - First Name, Last Name
  - Title (user/admin)
  - Department, Email
  - Country, City, Location
  - Password (optional)
- ✅ **Delete User** - Remove users from the system

### Database Schema
```sql
users (
    id              INT AUTO_INCREMENT Primary Key,
    first_name      VARCHAR(50),
    last_name       VARCHAR(50),
    title           VARCHAR(11),      -- 'user' or 'admin'
    department      INT,
    email           VARCHAR(50),
    country         VARCHAR(50),
    city            VARCHAR(50),
    location        INT,
    username        VARCHAR(100) NOT NULL,
    password        VARCHAR(1000) NOT NULL,  -- Salted hash
    salt            VARCHAR(128) NOT NULL
)
```

## API Endpoints

### Authentication (`/api`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/login` | Login with username/password → returns JWT token | No |
| `GET` | `/self` | Get current user info | User Token |
| `GET` | `/validate` | Validate token → returns user info | User Token |
| `POST` | `/change-password` | Change user password | User Token |

### Admin Operations (`/api/admin`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/user/create` | Create new user | Admin Token |
| `POST` | `/admin/create` | Create admin user | Admin Token |
| `PUT` | `/user/edit` | Edit user information | Admin Token |
| `POST` | `/user/delete` | Delete user by id | Admin Token |
| `GET` | `/view-users` | View all users (optional username filter) | Admin Token |
| `GET` | `/get-user` | Get specific user by username | Admin Token |

### Request/Response Examples

**Login:**
```json
POST /api/login
{
  "username": "admin",
  "password": "<RSA encrypted password>"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "admin",
  "username": "admin"
}
```

**Create User:**
```json
POST /api/admin/user/create
Authorization: Bearer <admin-token>
{
  "username": "john.doe",
  "password": "<RSA encrypted password>",
  "firstName": "John",
  "lastName": "Doe",
  "title": "user",
  "department": 5,
  "email": "john.doe@example.com",
  "country": "USA",
  "city": "Boston",
  "location": 2
}
```

## Theme & Colors

The application uses a custom color palette defined in `frontend/src/style.css`:

- `#df1a1a` - Deep red for danger/delete actions
- `#ae4343` - Primary color for buttons and headers
- `#785e5e` - Soft brown for subtle emphasis
- `#817979` - Secondary color for section headers
- `#8b959f` - Accent color for highlights

## Configuration

### Backend (`src/main/resources/application.properties`)
```properties
server.port=42068
spring.profiles.active=mysql

# JWT Configuration
jwt.user.secret=<secret-key>
jwt.admin.secret=<admin-secret-key>
jwt.expiration=86400000  # 24 hours

# RSA Keys for password encryption
public.key=<base64-encoded-public-key>
private.key=<base64-encoded-private-key>

# CORS
cors.allowed.origins=http://localhost:42067

# Database (when using MySQL profile)
spring.datasource.url=jdbc:mysql://localhost:3306/rocketpop
spring.datasource.username=<username>
spring.datasource.password=<password>
```

### Frontend (`frontend/package.json`, `frontend/vite.config.js`)
- Port: `42067`
- API Base: `http://localhost:42068/api`
- State: Pinia store with persistence
- Routing: Vue Router with beforeEach guards

### Docker (`.env` file)
```env
MYSQL_ROOT_PASSWORD=<root-password>
MYSQL_DATABASE=rocketpop
MYSQL_USER=<username>
MYSQL_PASSWORD=<password>
```

## Technology Stack

### Backend
- **Framework**: Spring Boot 4.0.0
- **Java**: 21
- **Build**: Maven
- **Database**: MySQL 8.0 (JPA/Hibernate)
- **Security**: JWT, RSA encryption
- **Testing**: JUnit 5 (47 tests)

### Frontend
- **Framework**: Vue 3.4.21 (Composition API)
- **Build**: Vite 5.1.5
- **Routing**: Vue Router 4.3.0
- **State**: Pinia 2.1.7
- **HTTP**: Axios 1.6.7
- **Encryption**: jsrsasign 11.1.0 (RSA)

### DevOps
- **Containerization**: Docker, Docker Compose
- **Web Server**: Nginx (production frontend)
- **CI/CD**: GitHub Actions (automated testing)

## Development

### Frontend Development

```powershell
cd frontend

# Install dependencies
npm install

# Start dev server with hot reload (port 42067)
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

# Build JAR
.\mvnw.cmd clean package

# Run tests (47 tests)
.\mvnw.cmd test

# Run specific test class
.\mvnw.cmd test -Dtest=AdminControllerTests
```

```bash
# Linux/Mac
mvn spring-boot:run
mvn clean package
mvn test
```

### Database Management

```bash
# Start MySQL with Docker
docker compose up mysql -d

# View logs
docker compose logs mysql

# Connect to database
docker exec -it rocketpop-mysql mysql -u <username> -p

# Initialize with default users
docker exec -i rocketpop-mysql mysql -u <username> -p<password> rocketpop < scripts/default_users.sql

# Stop database
docker compose down
```

## Testing

### Backend Tests
```powershell
.\mvnw.cmd test
```

**Test Coverage:**
- ✅ Authentication Controller Tests (8 tests)
- ✅ User Controller Tests (9 tests)
- ✅ Admin Controller Tests (17 tests)
- ✅ User Database Tests (11 tests)
- ✅ Application Context Tests (2 tests)
- **Total: 47 tests passing**

### CI/CD
GitHub Actions automatically runs all tests on push to `main`, `develop`, and `travis` branches.

See `.github/workflows/test.yml` for configuration.

## Docker Deployment

### Build and Run
```bash
# Start all services
docker compose up --build -d

# View logs
docker compose logs -f

# Stop all services
docker compose down

# Stop and remove volumes
docker compose down -v
```

### Individual Services
```bash
# Start only database
docker compose up mysql -d

# Start backend only
docker compose up backend -d

# Rebuild specific service
docker compose build frontend
docker compose up frontend -d
```

See [DEPLOYMENT.md](DEPLOYMENT.md) for complete Docker guide.

## Environment Variables

Create a `.env` file in the project root:

```env
# Database
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=rocketpop
MYSQL_USER=rocketpop_user
MYSQL_PASSWORD=your_secure_password

# JWT (optional - defaults in application.properties)
JWT_USER_SECRET=your_user_jwt_secret
JWT_ADMIN_SECRET=your_admin_jwt_secret

# RSA Keys (optional - defaults in application.properties)
PUBLIC_KEY=your_base64_public_key
PRIVATE_KEY=your_base64_private_key
```

## Troubleshooting

### Frontend Can't Connect to Backend
- Ensure backend is running on port 42068
- Check CORS settings in `application.properties`
- Verify API base URL in frontend

### Password Encryption Fails
- Ensure public key in `frontend/src/utils/encryption.js` matches backend
- Check browser console for detailed error messages
- Verify jsrsasign is installed: `npm list jsrsasign`

### Database Connection Issues
- Verify MySQL is running: `docker compose ps`
- Check credentials in `.env` file
- Ensure schema.sql has been executed
- View logs: `docker compose logs mysql`

### Tests Failing
- Ensure H2 in-memory database is working (no Docker required for tests)
- Run single test: `.\mvnw.cmd test -Dtest=ClassName#methodName`
- Check test logs in `target/surefire-reports/`

## Production Deployment

1. **Build artifacts:**
   ```bash
   docker compose build
   ```

2. **Configure environment:**
   - Update `.env` with production values
   - Set secure JWT secrets
   - Configure production database

3. **Deploy:**
   ```bash
   docker compose up -d
   ```

4. **Verify:**
   - Check all containers: `docker compose ps`
   - View logs: `docker compose logs -f`
   - Test login at `http://your-domain:42067`

5. **Production considerations:**
   - Use reverse proxy (Nginx/Apache) with SSL
   - Set `SPRING_PROFILES_ACTIVE=prod`
   - Enable application logging
   - Configure database backups
   - Rotate RSA keys periodically
   - Use secure JWT secrets (256-bit minimum)

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Vue 3 Documentation](https://vuejs.org/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [JWT.io](https://jwt.io/) - JWT debugger and documentation

## Contributing

1. Create a feature branch
2. Make your changes
3. Run tests: `.\mvnw.cmd test`
4. Commit with descriptive messages
5. Push to branch and create PR
6. Ensure CI/CD tests pass

## License

Copyright © 2025 Rocket Software, Inc.

---

**Need help?** Check [DEPLOYMENT.md](DEPLOYMENT.md) for Docker deployment details or open an issue on GitHub.
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

Copyright © Buzzword Software Solution.
