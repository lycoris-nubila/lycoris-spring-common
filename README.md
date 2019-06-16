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
    <version>1.0.4</version>
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
