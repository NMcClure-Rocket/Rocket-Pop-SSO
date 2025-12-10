#!/bin/bash
# Start only the MySQL database service using Docker Compose
# Useful for local dev/debug mode when running backend/frontend outside Docker

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR/.."
cd "$PROJECT_ROOT"

echo "[INFO] Starting only the MySQL database service..."
docker compose up -d mysql

echo "[INFO] MySQL database container is running."
echo "      To stop it, run: docker compose down"
