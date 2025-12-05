# Rocket Pop SSO Backend - Implementation Guide

## Overview
This is the Spring Boot backend for the Rocket Pop SSO application. It provides JWT-based authentication with separate token types for regular users (with external app support) and admin users (internal only).

## Key Features

### Authentication
- **User Tokens**: Generated with the secret key `RockertSoftwareRocks2025ThisIsNotSecureEnough`
  - Can be used by external applications
  - Available for users with roles: `user`, `manager`
  
- **Admin Tokens**: Generated with a different secret key
  - Internal SSO website use only
  - Cannot be used with external applications
  - Role: `admin`

### External Auth Service Integration
- **Verify URI**: `http://172.16.0.51:8080/auth_service/api/auth/verify`
- **Ping URI**: `http://172.16.0.51:8080/auth_service/api/auth/ping`

## Database Schema

### Tables Created
1. **Employee** - From your existing SQL (already populated with 1000 employees)
2. **Departments** - Department reference data
3. **Locations** - Location reference data
4. **sso_users** - New table for SSO authentication

### SSO Users Table Structure
```sql
CREATE TABLE sso_users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,  -- BCrypt hashed
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,       -- 'user', 'manager', 'admin'
    employee_id INTEGER,             -- FK to Employee table
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## API Endpoints

### Authentication
- `POST /login` - Authenticate user and get JWT token

### User Endpoints
- `GET /user/info` - Get current user information (requires valid token)
- `POST /user/updatepassword` - Change password (requires valid token)

### Admin Endpoints (require admin token)
- `POST /admin/user/create` - Create new user (user or manager role)
- `POST /admin/adminuser/create` - Create new admin user
- `PUT /admin/user/edit` - Update user information
- `POST /admin/user/delete/{username}` - Delete user
- `GET /admin/user/getall?username={filter}` - Get all users (optional filter)
- `GET /admin/user/get/{username}` - Get specific user

## Setup Instructions

### Prerequisites
- Java 21
- Maven 3.6+
- Your existing Employee database

### Installation Steps

1. **Update Dependencies**
   ```bash
   ./mvnw clean install
   ```

2. **Configure Database**
   - Edit `src/main/resources/application.properties`
   - Currently configured for H2 (in-memory) for development
   - For production, uncomment PostgreSQL or configure your database

3. **Run Database Schema**
   - The `schema.sql` file will automatically create the `sso_users` table
   - Sample users are included:
     - Username: `admin`, Password: `admin123` (Role: admin)
     - Username: `testuser`, Password: `user123` (Role: user)
     - Username: `manager1`, Password: `manager123` (Role: manager)

4. **Start the Application**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   The API will be available at: `http://localhost:42068`

5. **Access H2 Console** (Development only)
   - URL: `http://localhost:42068/h2-console`
   - JDBC URL: `jdbc:h2:mem:rocketpopdb`
   - Username: `sa`
   - Password: (leave empty)

## Token Usage

### User Token Example
Used for regular users and managers - can be validated by external apps:
```
Authorization: Bearer <user-token>
```

### Admin Token Example
Used for admin users - internal SSO use only:
```
Authorization: Bearer <admin-token>
```

## Security Implementation

### Password Hashing
- All passwords are hashed using BCrypt
- Salt rounds: 10
- Passwords are never stored in plain text

### Token Structure
User tokens include:
- `username` - User's username
- `email` - User's email
- `role` - User's role (user/manager)
- `type` - Token type ('user')

Admin tokens include:
- `username` - Admin's username
- `email` - Admin's email
- `role` - Always 'admin'
- `type` - Token type ('admin')

### CORS Configuration
- Allowed origin: `http://localhost:42067` (frontend)
- Configured on all controllers

## Development Notes

### Current Implementation Status
✅ Complete:
- All controller endpoints with full implementation
- JWT token generation (separate for user/admin)
- User authentication with BCrypt
- Database entities (Employee, SSOUser)
- Repository layer
- Service layer with business logic
- CORS configuration

🔄 To Do:
- Connect to production database (currently using H2)
- Implement external auth service integration
- Add request/response logging
- Add rate limiting for security
- Add email verification for new users
- Implement password reset functionality
- Add audit logging

### Testing
Test the API with the sample users:
```bash
# Login as admin
curl -X POST http://localhost:42068/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Login as user
curl -X POST http://localhost:42068/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"user123"}'

# Get user info
curl -X GET http://localhost:42068/user/info \
  -H "Authorization: Bearer <your-token>"
```

## Production Deployment

### Database Migration
1. Switch from H2 to production database (PostgreSQL recommended)
2. Update `application.properties` with production database credentials
3. Run the schema.sql to create tables
4. Import your existing Employee data
5. Create initial admin user

### Security Checklist
- [ ] Change JWT secret keys
- [ ] Change default admin password
- [ ] Enable HTTPS
- [ ] Configure proper CORS origins
- [ ] Set up rate limiting
- [ ] Enable request logging
- [ ] Set up monitoring and alerts
- [ ] Configure database backups

### Environment Variables
For production, use environment variables instead of hardcoded values:
```bash
export JWT_USER_SECRET=your-secret-here
export JWT_ADMIN_SECRET=your-admin-secret-here
export DATABASE_URL=your-database-url
export DATABASE_USERNAME=your-db-user
export DATABASE_PASSWORD=your-db-password
```

## Support
For issues or questions, please contact the development team.
