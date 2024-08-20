# account-service

## Description
Account service will expose the api to create new current account and it fetch the details of customer accounts. It communicate with transaction service to save and fetch the complete transaction details. 

## Setup
Install java 17 and maven 3.9.8. To build this project require maven 3.8 or later and Java 17 or later.

## Build and Run the application in local
```bash
# Clone the repository
git clone https://github.com/VimalaJay/account-service

# Navigate to the project directory
cd local-repository

# Run test
mvn test

# Build application
mvn clean install

# Deploy the project
mvn spring-boot:run

# check database console
http://localhost:8080/h2-console

# Run below api in postman or browser
GET : http://localhost:8080/api/v1/accounts/createAccount?customerId=2456989&initialCredit=250
GET : http://localhost:8080/api/v1/accounts/customer/2456989

Please make sure fund transaction services are also up.
