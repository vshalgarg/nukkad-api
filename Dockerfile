# Dockerfile
FROM openjdk:17-jdk-alpine
COPY target/*.jar app.jar
COPY firebase.json /app/firebase.json
ENTRYPOINT ["java", "-jar", "/app.jar"]
