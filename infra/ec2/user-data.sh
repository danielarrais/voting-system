#! /bin/bash

# Install Docker
sudo dnf update -y
sudo dnf install docker -y

# Auto start Docker when starting the machine
sudo systemctl start docker
sudo systemctl enable docker

# Start voting-system containers
sudo docker run -it --rm -d -p 80:8080 --name --restart always web danielarrais/voting-system:latest

docker run -d \
  --name mysql-container \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_ROOT_PASSWORD=pass \
  -e MYSQL_DATABASE=voting-system \
  -p 3306:3306 \
  mysql:8.0.18 --default-authentication-plugin=mysql_native_password

docker run -d \
  --name rabbitmq-container \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3.8.18-management-alpine