FROM  maven:3-openjdk-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package

FROM openjdk:17.0.2-jdk-slim-buster

WORKDIR /app
COPY --from=builder /app/target/app.jar .

ENTRYPOINT ["java","-jar","/app/app.jar"]