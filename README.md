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
./mvnw clean package

## Run
./mvnw spring-boot:run
or (after building)
./java -jar target/payment-rest-api-0.0.1-SNAPSHOT.jar

## Test
./mvnw spring-boot:test

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
/users/{id}/reset

## Usage
For funding n Points to a User
POST /users/{id}/fund with body
{"amount" : n }

To transfer n Points to another program.  See below for valid program names.
POST /users/{id}/transfer with body
{"amount" : n, "destination", ProgramName}

To view the User's history, i.e. all transactions successful or not with lots of metadata
GET /users/{id}/history

To view the User's valid Programs for which point transfers are eligible.
GET /users/{id}/programs

## Program Names and Loyalty Points
Three fake Programs with which to transfer points are hardcoded.  Each has a different Exchange rate to test with.
They are "Program1" (exchange rate 2), "Program2" (exchange rate 3), "Program3" (exchange rate 0.5)

All points must be positive integers greater than or equal to one.
