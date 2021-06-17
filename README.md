# Point Transfer API
An API that allows the user to
1. list available loyality programs
2. fund their account with more points
3. transfer their points to another program
4. see point transfer history

# Design
The API is build as a free-standing Spring Boot REST API, with in-memory database support via H2.
While active, endpoints corresponding to the functionality described above are exposed, reachable through a web browser, cURL, etc...

## Build
(from directory with pom.xml)
mvn:build

## Run
mvn:

## Test
mvn:

## Api (corresponding to functions above)
1. GET /user/1/programs
2. POST /user/1/fund
    json payload:
        { "amount", long }
3. POST /user/1/transfer
    json payload:
        { }
4. GET /user/1/history

## Extra Api Information
Although it was not a requirement, support for multiple users was implemented anyway.
Some reasons for this decision: the simplicity of abstracting to multiple users, the benefits of a persisted Points balance, easier debugging, better-formed REST URI syntax (by including an id), and extensibility 
/users
/users/{id}
/users/{id}/history
/users/{id}/programs

## Requests and Responses

    



