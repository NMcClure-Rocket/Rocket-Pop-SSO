#!/bin/bash
curl localhost:42068/demo/
echo

curl localhost:42068/demo/number/10
echo

curl localhost:42068/demo/letter/a
echo

curl -X POST -H "Content-Type: application/json" -d '{"username":"admin"}' localhost:42068/demo/loginDemo
echo

curl -X POST -H "Content-Type: application/json" -d '{"name":"admin","age":10}' localhost:42068/demo/printUser
echo
