# lycoris-spring
## Getting Started
These instructions will help you to install and configure Lycoris Spring Common, this library contains following features configs: ***Async, Schedule, Domain Driven Design, GraphQL, Swagger, Jwt, Aws SNS, Aws SQS, Aws Secret***
### Installing
Using maven, you had to add git repository first
```xml
<repositories>
  <repository>
    <id>lycoris-spring-common</id>
    <url>https://raw.github.com/sdoubey/lycoris-spring-common/master/maven/</url>
  </repository>
</repositories>
```
Then dependency
```xml
<dependencies>
  <dependency>
    <groupId>eu.lycoris</groupId>
    <artifactId>spring-common</artifactId>
    <version>1.0.10</version>
  </dependency>
<dependencies>
```
### Features
---
#### Add JWT support
First add annotation in configuration class
```java
@Configuration
@EnableLycorisJwt
public class MyConfiguration {

  @Bean
  public LycorisJwtSecretProvider secretProvider() {
    return () -> "mybase64secret";
  }
}
```
Then add ***Authorization*** header in http request, following cases work:

|Header       |Value         |
|-------------|--------------|
|Authorization|Bearer mytoken|
|Authorization|bearer mytoken|
|Authorization|mytoken|

---
#### Add JWT support with AWS Secret Manager
First add annotations in configuration class
```java
@Configuration
@EnableLycorisJwt
@EnableLycorisSecret
public class MyConfiguration {

  @Bean
  public LycorisJwtSecretProvider secretProvider(AWSSecretsManager secretsManager) {
    return () ->
        secretsManager
            .getSecretValue(new GetSecretValueRequest().withSecretId("JWT-Secret"))
            .getSecretString();
  }
}
```
Then add AWS configuration properties in **application.yml**
```yaml
lycoris:
  secret:
    secret-key: My-Access-key-ID
    access-key: My-Secret-access-key
    region: my-aws-region
```
---
#### Add GraphQL support
First add GraphQL SPQR dependency
```xml
<dependency>
  <groupId>io.leangen.graphql</groupId>
  <artifactId>graphql-spqr-spring-boot-starter</artifactId>
  <version>0.0.4</version>
</dependency>
```
Then add a GraphQLApi service using ***@GraphQLApi, @GraphQLQuery, @GraphQLMutation***
```java
@Service
@GraphQLApi
@Transactional
public class UserGraphQLService {

  @Autowired UserRepository userRepository;

  @Autowired PasswordEncoder passwordEncoder;

  @GraphQLQuery
  public IUser authenticateUser(
      String emailAddress,
      String rawPassword) {
    IUser user = userRepository.findByEmailAddress(emailAddress);
    if (user == null) {
      throw new LycorisApplicationException(
          UserMessage.ERROR_WEB_REQUEST_USER_EMAIL_NOT_FOUND.getMessageKey());
    }

    if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
      throw new LycorisApplicationException(
          UserMessage.ERROR_WEB_REQUEST_USER_PASSWORD_DONT_MATCHES.getMessageKey());
    }

    return user;
  }

  @GraphQLMutation
  public IUser createNewUser(
      String lastName,
      String firstName,
      String emailAddress,
      String rawPassword) {
    if (userWithEmailAddressAlreadyExists(emailAddress)) {
      throw new LycorisApplicationException(
          UserMessage.ERROR_WEB_REQUEST_USER_EMAIL_ALREADY_EXISTS.getMessageKey());
    }

    User user =
        User.builder(
                UUID.randomUUID(),
                lastName,
                firstName,
                emailAddress,
                passwordEncoder.encode(rawPassword))
            .build();

    userRepository.save(user);

    return user;
  }

  private boolean userWithEmailAddressAlreadyExists(String emailAddress) {
    return userRepository.findByEmailAddress(emailAddress) != null;
  }
}
```
You can get JWT authentication if feature is enabled by adding ***@GraphQLRootContext*** parameter
```java
@GraphQLQuery
public IUser authenticateUser(
    @GraphQLRootContext JwtAuthenticationToken authentication,
    String emailAddress,
    String rawPassword) {
  IUser user = userRepository.findByEmailAddress(emailAddress);
  if (user == null) {
    throw new LycorisApplicationException(
        UserMessage.ERROR_WEB_REQUEST_USER_EMAIL_NOT_FOUND.getMessageKey());
  }

  if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
    throw new LycorisApplicationException(
        UserMessage.ERROR_WEB_REQUEST_USER_PASSWORD_DONT_MATCHES.getMessageKey());
  }

  return user;
}
```
