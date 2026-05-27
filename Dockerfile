FROM eclipse-temurin:25-jre
WORKDIR /app
COPY target/Maven-1.0-SNAPSHOT.jar /app/snake-game.jar
EXPOSE 8080
CMD ["java", "-jar", "snake-game.jar"]