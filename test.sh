#!/bin/bash
curl -X POST -H "Content-Type: application/json" -d '{"username":"testuser","password":"user123"}' localhost:42068/login
