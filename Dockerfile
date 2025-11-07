# Dockerfile
FROM eclipse-temurin:17-jdk
COPY target/*.jar app.jar
COPY firebase.json /app/firebase.json
ENTRYPOINT ["java", "-jar", "/app.jar"]
