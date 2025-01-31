#!/bin/bash

# Install dependencies
mvn clean install

# Wait a few seconds to ensure the Config Server is up
sleep 65

# Set environment variables for the database
export DB_URL="jdbc:mysql://localhost:3306/ms_products_db?createDatabaseIfNotExist=true&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="sasa"

# Initialize Config Server in a new xterm
xterm -hold -e "cd config && mvn spring-boot:run" &

# Wait a few seconds to ensure the Config Server is up
sleep 10

# Initialize LDAP Server in a new xterm
xterm -hold -e "cd ldap-server && mvn spring-boot:run" &

# Wait a few seconds to ensure the LDAP Server is up
sleep 15

# Initialize Auth Server in a new xterm
xterm -hold -e "cd auth-server && mvn spring-boot:run" &

# Wait a few seconds to ensure the Auth Server is up
sleep 15

# Initialize Products API in a new xterm
xterm -hold -e "cd products-api && mvn spring-boot:run" &
