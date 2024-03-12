# Spring Service Code Kata

This project contains a coding exercise focusing on implementing logic in the service layer of a backend micro-service.
The test resources contains a `CatalogServiceTest` with pre-defined tests to verify the behavior of the corresponding service `CatalogService`.
The `CatalogService` only contains method stubs which will result in the tests to fail.  The goal of the exercise is to implement the service logic so that all the tests pass.

The models have been defined which includes a `CatalogEntity` which has a one-to-many relationship with `ProductEntity`. The service exposes the model data using the `Catalog` and `Product` DTOs.  Details about the data model can be found in the [V1__schema.sql](src/main/resources/db/migration/V1__schema.sql).

The predefined tests and models should not be modified.  The exercise should be completed by implementing the minimal amount of needed to get the test to pass starting with the first test defined in the `CatalogServiceTest` followed by each subsequent test working down the list.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

