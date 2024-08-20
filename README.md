# account-service

## Description
Account service will expose the api to create new current account and it fetch the details of customer accounts. It communicate with transaction service to save and fetch the complete transaction details.

## Installation


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

# Run below api in postman
GET : http://localhost:8080/api/v1/accounts/createAccount?customerId=2456989&initialCredit=250
GET : http://localhost:8080/api/v1/accounts/customer/2456989
