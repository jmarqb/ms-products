FROM openjdk:21-jdk-slim
LABEL authors="jmarqb"

ARG JAR_FILE=target/products-api-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} products-api.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","/products-api.jar"]