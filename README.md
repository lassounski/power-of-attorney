## Rabobank Assignment for Authorizations Area

### Running the application
First, compile the application:
```
$ mvn clean install
```
Unit and integration tests will be executed after compile phase.

Run the application using the maven-spring-boot-plugin:
```
$ cd api
$ mvn spring-boot:run
```
The application will run on localhost:8080

There is a configuration class that can initialize some fake data hidden by a Spring profile **fakeData**.
```
$ cd api
$ mvn spring-boot:run -Dspring-boot.run.profiles=fakeData
```
It will load the following accounts:
```
[
    {
        "accountHolderName": "Kirill Lassounski",
        "accountNumber": "000000001",
        "type": "savingsAccount",
        "balance": 2000.00
    },
    {
        "accountHolderName": "Kirill Lassounski",
        "accountNumber": "000000002",
        "type": "paymentsAccount",
        "balance": 5000.00
    },
    {
        "accountHolderName": "Freddy Kruger",
        "accountNumber": "000000003",
        "type": "savingsAccount",
        "balance": 1000.00
    }
]
```
### Available endpoints
#### Grant Access
_POST /grant_

Consumes a DTO with the account numbers from grantor, grantee and the authorization value.
Ex:
```
{
	"granteeAccountNumber": "000000001",
	"grantorAccountNumber": "000000003",
	"authorization": "READ"
}
```
#### Example request
```
$ curl -d '{"granteeAccountNumber":"000000001", "grantorAccountNumber":"000000003", "authorization":"READ"}' -H "Content-Type: application/json" -X POST http://localhost:8080/grant 
```
If one of the accounts is not present in the DB, a 404 is returned.

#### Retrieve granted accounts
_GET /grantedAccounts/{granteeName}_

The name of the grantee is used here because he might have more than one account to his name.
If the granteeName is non-existent or there are no grants, an empty list is returned.

#### Example request
```
$ curl -X GET localhost:8080/grantedAccounts/Kirill%20Lassounski
```

### Assumptions and thoughts
* Authorization/Authentication should be in place to allow only users that own their accounts to give grants to other users. Could be implemented using Spring Security's JWT or OAuth2.
* Core logic 100% covered by tests.
* Endpoints with Spring validation for clear communication to the user of the API.
* Expected to run on JDK 11.
* Do not execute the integration tests together with the application running since the Embedded MongoDB is the same instance.
* No cascading operations on Account changes are present for simplicity. Thus, in the case of Account balance updates, all the PowerOfAttorney that reference that Account would have to be updated as well.