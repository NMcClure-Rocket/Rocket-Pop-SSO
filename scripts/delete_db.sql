#!/bin/bash
docker compose down -v mysql
echo "Deleted database"
echo "Please restart mysql container and run init_db.sh"
