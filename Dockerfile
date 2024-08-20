FROM openjdk:17

ARG JAR_PATH=target/*.jar

COPY ${JAR_PATH} account-service.jar

EXPOSE  8080

ENTRYPOINT ["java", "-jar", "account-service.jar"]