FROM openjdk:21-jdk-slim
LABEL authors="jmarqb"

ARG JAR_FILE=target/config-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} config.jar

EXPOSE 8888

ENTRYPOINT ["java","-jar","/config.jar"]