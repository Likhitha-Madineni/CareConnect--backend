# CareConnect Backend

Spring Boot REST API for the CareConnect project.

## Deploy on Railway

1) Create a new Railway project and connect this GitHub repo.
2) Add a MySQL plugin in the same Railway project.
3) Set the following environment variables in Railway:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
CARECONNECT_JWT_SECRET
SPRING_MAIL_USERNAME
SPRING_MAIL_PASSWORD
```

4) Deploy. Railway will build with Maven and run the generated JAR.

## Local run

```
mvn spring-boot:run
```

The server runs on port 8080 by default.
