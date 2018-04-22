# Spring Boot, MySQL, JPA, Hibernate Rest API For Rappicoop Project

Build Restful API for a simple delivery application using Spring Boot, Mysql, JPA and Hibernate.

## Requirements

1. Java - 1.8.x

2. Maven - 3.x.x

3. Mysql - 5.x.x

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/vrglx33/ProjectRapicoop
```

**2. Create Mysql database**
```bash
create database rappi_coop
```

**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the app using maven**

```bash
mvn package
java -jar target/easy-notes-1.0.0.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

The app defines following Endpoint APIs.

    GET /api/deliveries
    GET /api/deliveries/last/distributor/{id}
    GET /api/deliveries/all/distributor/{id}
    GET /api/deliveries/all/distributor/{id}/from/{dateFrom}/to/{dateTo}
    GET /api/deliveries/average/report
    POST /api/delivery
    GET /api/delivery/{id}
    GET /api/delivery/{id}/distributor/{distributorid}/status/{statusid}
    DELETE /api/delivery/{id}
    GET /api/distributors
    POST /api/distributors/location/{id}
    POST /api/distributor
    GET /api/distributors/{id}
    PUT /api/distributors/{id}
    DELETE /api/distributors/{id}
    GET /api/history
    POST /api/history
    GET /api/history/{id}
    PUT /api/history/{id}
    DELETE /api/history/{id}

You can test them using postman or any other rest client.
