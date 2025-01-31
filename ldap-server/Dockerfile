FROM openjdk:21-jdk-slim
LABEL authors="jmarqb"

ARG JAR_FILE=target/ldap-server-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} ldap-server.jar

COPY src/main/resources/schema.ldif /app/schema.ldif

EXPOSE 8389

ENTRYPOINT ["java","-jar","/ldap-server.jar"]