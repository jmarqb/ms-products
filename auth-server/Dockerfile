FROM openjdk:21-jdk-slim
LABEL authors="jmarqb"

ARG JAR_FILE=target/auth-server-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} auth-server.jar

EXPOSE 9000

ENTRYPOINT ["java","-jar","auth-server.jar"]