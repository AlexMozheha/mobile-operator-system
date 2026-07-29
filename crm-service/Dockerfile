FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY auth-service/pom.xml ./auth-service/
COPY validation/pom.xml ./validation/
COPY crm-service/pom.xml ./crm-service/
COPY billing-service/pom.xml ./billing-service/
COPY self-service-portal/pom.xml ./self-service-portal/

RUN mvn dependency:go-offline -B

COPY crm-service/src ./crm-service/src

RUN mvn -pl crm-service -am clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build app/crm-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]