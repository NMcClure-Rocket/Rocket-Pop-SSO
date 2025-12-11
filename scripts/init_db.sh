#!/bin/bash

cd "$(dirname "$0")"
docker exec -i rocketpop-mysql mysql -u rocketpop_user -pchangeme_password rocketpop < ./default_users.sql
