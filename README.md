# springSecurityDemo

# Introduction to Spring Security 

# AuthenticationManager in Spring Security
### Helpful Docs and Links

- [Spring Security DAO Authentication Provider Documentation](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html)
- [Implementing JWT Authentication using Spring Security - Detailed Walkthrough](https://medium.com/geekculture/implementing-json-web-token-jwt-authentication-using-spring-security-detailed-walkthrough-1ac480a8d970)
- [JWT Spring Security GIT repo Example Repository](https://github.dev/udhayakumarth/jwt-spring-security-example)

## Role of `AuthenticationManager`

The `AuthenticationManager` is a central component in Spring Security that handles the authentication process. Its main roles are:

1. **Authentication Handling**:
    - Processes authentication requests and checks whether the provided credentials are valid against the underlying user data source.

2. **Delegation**:
    - Delegates the authentication process to one or more `AuthenticationProvider` instances, each responsible for a specific type of authentication (e.g., username/password, OAuth2, JWT, etc.).

3. **Return Authentication Object**:
    - Returns an `Authentication` object containing details about the authenticated user, including their roles and authorities upon successful authentication.

4. **Failure Handling**:
    - Throws an `AuthenticationException` if authentication fails, which can be handled to provide feedback to the user.

## Flow of Authentication

Here’s a typical flow of authentication in a Spring Security application:

1. **User Requests Access**:
    - The user attempts to access a secured resource by logging in through a web form or an API.

2. **Authentication Request**:
    - The user submits their credentials (username and password) encapsulated in an `Authentication` object (e.g., `UsernamePasswordAuthenticationToken`).

3. **Authentication Process**:
    - Spring Security intercepts the request and passes the `Authentication` object to the `AuthenticationManager`.
    - The `AuthenticationManager` delegates to the appropriate `AuthenticationProvider`.

4. **Credential Verification**:
    - The `AuthenticationProvider` verifies the credentials by comparing the provided password with the stored password.
    - If the credentials match, it creates an authenticated `Authentication` object.

5. **Return Authentication Object**:
    - The `AuthenticationManager` receives the authenticated `Authentication` object and stores it in the security context.

6. **Access Granted**:
    - The user is granted access to the requested resource based on their authorities (roles).

7. **Logout**:
    - When the user logs out, Spring Security clears the security context, invalidating the user's authentication.

## Example Code Flow

### User Login Endpoint

```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        // Generate JWT or other token here if using stateless authentication
        return ResponseEntity.ok("Login successful");
    } catch (AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
    }
}

