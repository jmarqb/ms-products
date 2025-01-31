@echo off
REM Install dependencies
start cmd /c "mvn clean install"

REM Wait a few seconds to ensure the Config Server is up
timeout /t 65

REM Set environment variables for the database
set DB_URL=jdbc:mysql://localhost:3306/ms_products_db?createDatabaseIfNotExist=true&serverTimezone=UTC
set DB_USERNAME=root
set DB_PASSWORD=sasa

REM Initialize Config Server in a new process
start cmd /c "cd config && mvn spring-boot:run"

REM Wait a few seconds to ensure the Config Server is up
timeout /t 10

REM Initialize LDAP Server in a new process
start cmd /c "cd ldap-server && mvn spring-boot:run"

REM Wait a few seconds to ensure the LDAP Server is up
timeout /t 15

REM Initialize Auth Server in a new process
start cmd /c "cd auth-server && mvn spring-boot:run"

REM Wait a few seconds to ensure the Auth Server is up
timeout /t 15

REM Initialize Products API in a new process
start cmd /c "cd products-api && mvn spring-boot:run"
