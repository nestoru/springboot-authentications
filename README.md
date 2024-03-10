# Spring Authentications POC
The objective of this project is to help colleagues with the implementation of what I call a business hub (bhub).

Some clients do not have their own OAuth 2 provider and yet want to use OAuth 2 authentication, in which case it might be useful to keep all in your service, implementing OAuth 2.0 authentication in your application to serve both as the Authorization Server (issuing tokens) and the Resource Server (validating tokens).

This project shows authentication via form-based authentication and OAuth2 using spring security and unique postgresql (or any sql db) table for credentials and roles regardless of the authentication method. The endpoints all start with /api/. It is an uncoventional authentication stateful implementation.

It uses stateful sessions stored in a sql database (postgresql in this case)  to ensure that services are horizontally distributed.

The application acts as both the OAuth resource server and the OAuth authentication provider (you can separate them obviously), validating JWTs that it has issued.

When issuing a JWT, we store the token, associated user, and expiry timestamp in the database.

When validating a JWT we simply check if the provided token exists in your database and ensure it has not expired.

JWT Validation: Validation involves checking if the provided token exists in the sql database and ensuring it has not expired.

ChatGPT I think puts in clear the major concerns that many in the software community have had for a long time when it comes to separating APIs from UI AJAX. This separation is to me not only unnecessary but costly. Companies could save as much as my clients have saved on design, development, documentation and testing by going the bhib route.

Here are the "Important Considerations":
- Security Risks: This approach mixes the concepts of end-users and clients, which might not be ideal from a security perspective. Ensure you understand the implications.
- OAuth2 Standards: This implementation deviates from standard OAuth2 practices where clients are distinct from users. It's important to ensure this meets your application's security requirements.
This implementation provides a basic concept and should be thoroughly tested and potentially adjusted to meet the specific needs and security requirements of your application.
- Others: All kind of arguments are usually posed by architects related to a similar "risk".

Here is my non comprehensive answer to these considerations:
- To the concern about separation of users and clients, I have considered that to be naive for more than 20 years for several reasons.
- First, an application client with certain access is a user.
- Second, robots are users since they can use techniques like the so called robotic process automation (RPA) to interact with just about anything a user would, and similarly users can automate work using the APIs that a UI would offer with scraping tools,
- Third, in an API-first approach the only limitations to use a piece of software should be imposed by access controls like MAC, RBAC, and DAC and not by the type of client; which can be really any form of intelligence or an automated process.


Obviously this is a very basic project because it is just a POC. However you could add JSON Web Token (JWT), JSON Web Signature (JWS), JSON Web Encryption (JWE), JSON Web Key (JWK), JSON Web Algorithms (JWA) or anything that comes after and becomes popular. The fact still remains, you should keep it simple. Managing users identification and authentication by any means while sticking to the same access controls when it comes to authorization should be achievable and preferable for return on investment (ROI) reasons.

# Form authentication
This is a typical setup whch needs not much explanation.

# OAuth2 authentication
- OAuth2Controller: Handles token issuance.
- JwtTokenUtil: Utility for JWT operations, including token creation, db storage and validation.

# Preconditions
There is a .gitignore line to avoid committing to the git repo any sensitive information contained in src/main/resources/application.properties wity the below command:
```
cat src/main/resources/application.properties | sed -E "s/spring.datasource.([^=]*)=.*/spring.datasource.\1=***/" > src/main/resources/application.properties.sample
```
(Optional) Therefore you will need to start by copying over that file as a guide to complete your unique information there:
```
cp src/main/resources/application.properties.sample src/main/resources/application.properties
```

# Build and run
```
mvn clean install
mvn springboot:run
```

# Interact with the service
Here is how to test it

1. Unauthenticated User sends GET /api/server_info and gets error http code 401 for unauthenticated when using user/password/cookie 

curl -i --cookie "sessionName=INCORRECT_SESSION"  http://localhost:8080/api/server_info
```

2. Unauthenticated user sends POST /api/auth {user: , pass:} with correct credentials and gets success http 200 and a session cookie
```
curl -i -X POST -d "username=admin&password=@dm1n" http://localhost:8080/api/login
```

3. Authenticated user sends GET /api/server_info with a valid session cookie and gets http 200 {hostname: {server_hostname}
```
curl -i --cookie "SESSION=${SESSION}"  http://localhost:8080/api/server_info
```

4. Unauthenticated User sends GET /api/server_info and gets error http code 401 for unauthenticated when using OAUTH 2 authentication:
``` 
curl -i --header "Authorization: Bearer INCORRECT_TOKEN" http://localhost:8080/api/server_info
```

5. Unauthenticated user sends POST /api/oauth2/token { grant_type=client_credentials&client_id=CLIENT_ID&client_secret=CLIENT_SECRET
} with correct credentials and gets success http 200 and an oauth 2 access token
```
curl -i -X POST -d "grant_type=client_credentials&client_id=admin&client_secret=@dm1n" "http://localhost:8080/api/oauth2/token"
```

6. Authenticated user sends GET /api/server_info with a valid header Authorization: Bearer {access_token} and gets http 200 {hostname: {server_hostname}}
```
curl -i --header "Authorization: Bearer ${TOKEN}" http://localhost:8080/api/server_info
```

# Miscelaneous.
- Securely sharing the content of your MAC OSX profile configuration file
```
cat ~/.zshrc | sed -E "s/(export[^=]*=).*/\1***/"
```

- Printing all project files and their content
```
find ./ -type f -not -path '*target*' -not -path '*README*' -exec sh -c 'echo "File: {}"; cat {}' \; | sed -E "s/spring.datasource.([^=]*)=.*/spring.datasource.\1=***/"
```
- Generating a BCrypt Hashed Password for clear text '@dm1n' which is configured as the admin user password in our app
```
mvn exec:java -Dexec.mainClass="com.nestorurquiza.BcryptHashGenerator" -Dexec.args="@dm1n" 
```
- Generating a BCrypt Hashed Password for clear text 'op3r@t0r' which is configured as the operator user password in our app
```
mvn exec:java -Dexec.mainClass="com.nestorurquiza.BcryptHashGenerator" -Dexec.args="op3r@t0r"
```
- Checking that a password matches a hash
```
mvn exec:java -Dexec.mainClass="com.nestorurquiza.BcryptPasswordHashMatch" -Dexec.args="@dm1n '\$2a\$10\$caM6aZ2N0KP.QXAByTRxPuKIB49uRXlPZRLDR.mFDYLq8KnSOA5PW'"
```
