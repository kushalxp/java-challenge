### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 3 years experience in Java and I started to use Spring Boot from last year
- I'm a beginner and just recently learned Spring Boot
- I know Spring Boot very well and have been using it for many years


# Things added
- Added the business logic for Employee service class
- Added logic in Rest controller
- Added documentation for swagger ui
- Added caching mechanism using hazelcast
- Added unit test cases
- Custom exceptions for different scenarios
- Added logging using slf4j



# Things can be done in future 
- Integration Testcase
- Performance testing of APIs using JMeter
- Containerize
- SonarQube integration for inspection of code or some other tools to do that.
- Use External Cache to like Redis, memcache to serve high amount of users.
- Use external database to persist the data



# Experience in java
- I have around 6 years of experience but only java exp around 4-5 year 
- Spring boot never used so far in last 6 year.
- Every company I worked for had it's own inhouse developed framework so never got the opportunity to learn spring
- I can learn spring in depth if it is required or used in AXA.
- 