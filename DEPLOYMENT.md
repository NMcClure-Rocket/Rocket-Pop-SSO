# 🚀 Deployment Guide: Rocket Pop SSO

This guide explains how to deploy the Rocket Pop SSO application stack (Spring Boot backend, Vue frontend, and MySQL database) using Docker and Docker Compose for a production-ready environment.

---

## Prerequisites
- **Docker** and **Docker Compose** installed
- Environment variables set in a `.env` file (see `.env.example`)

---

## 1. Build & Run with Docker Compose

From the project root:

```bash
docker compose up --build -d
./scripts/init_db.sh
```

- This will build and start **all services**:
  - **Backend** (Java 21, Spring Boot) on port **42068**
  - **Frontend** (Vue, Nginx) on port **42067**
  - **MySQL** database on port **3306**

---

## 2. Accessing the Application
- **Frontend:** [http://localhost:42067](http://localhost:42067)
- **Backend API:** [http://localhost:42068](http://localhost:42068)

---

## 3. Stopping the Stack

```bash
docker compose down
```

---

## 4. Environment Variables
- Copy `.env.example` to `.env` and fill in your database credentials and secrets.
- The backend and database will use these values automatically.

---

## 5. File Overview
- `Dockerfile` (root): Builds the backend JAR and runs it with Java 21
- `frontend/Dockerfile`: Builds the Vue frontend and serves it with Nginx
- `frontend/nginx.conf`: Custom Nginx config for SPA routing and port 42067
- `.dockerignore` and `frontend/.dockerignore`: Exclude unnecessary files from Docker context
- `docker-compose.yml`: Orchestrates all services

---

## 6. Health & Logs
- Check container status:
  ```bash
  docker compose ps
  ```
- View logs for a service:
  ```bash
  docker compose logs backend
  docker compose logs frontend
  docker compose logs mysql
  ```

---

## 7. Rebuilding After Code Changes
- For backend or frontend changes, rebuild the affected service:
  ```bash
  docker compose build backend
  docker compose build frontend
  docker compose up -d
  ```

---

## 8. Production Notes
- The frontend is served as static files via Nginx (not npm dev server)
- The backend runs as a JAR with Java 21
- All services are isolated and networked via Docker Compose

---

## 9. Troubleshooting
- Ensure Docker daemon is running and you have permission to access it
- Check `.env` values for correctness
- Use `docker compose logs <service>` for debugging

---

## 10. Customization
- To change ports, update `docker-compose.yml` and `frontend/nginx.conf`
- For database persistence, add a volume to the `mysql` service in `docker-compose.yml`

---

## 11. Cleanup
- To remove all containers, networks, and volumes:
  ```bash
  docker compose down -v
  ```

---

For more details, see the main [README.md](README.md).
