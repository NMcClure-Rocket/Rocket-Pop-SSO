#!/bin/bash

if ! command -v docker &> /dev/null
then
    echo "Docker is not installed. Installing Docker..."
    sudo apt install docker docker-compose docker-ce
    exit 1
fi

docker-compose up -d
