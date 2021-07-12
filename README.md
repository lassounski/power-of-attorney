## Rabobank Assignment for Authorizations Area

### Running the application
First, compile the application:
```
$ mvn clean install
```
Unit and integration tests will be executed after compile phase.

Run the generated .jar in target directory:
```
$ java -jar api/target/rabobank-assignment-api-0.0.1-SNAPSHOT.jar
```
The application will run on localhost:8080

### Available endpoints
#### Grant Access
POST /grant

Consumes a DTO with the account numbers from grantor, grantee and the authorization value.
Ex:
```
{
    "granteeAccountNumber": "0000001",
    "grantorAccountNumber": "0000007",
    "authorization": "READ",
}
```
If one of the accounts is not present in the DB, a 404 is returned.

#### Retrieve granted accounts
GET /grantedAccounts/{granteeName}

The name of the grantee is used here because he might have more than one account to his name.
If the granteeName is non-existent or there are no grants, an empty list is returned.

### Assumptions and thoughts
* Authorization/Authentication should be in place to allow only users that own their accounts to give grants to other users.
