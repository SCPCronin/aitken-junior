FROM openjdk:23-slim

WORKDIR /app

COPY target/aitken-junior-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 31970

ENTRYPOINT ["java", "-jar", "app.jar"]
