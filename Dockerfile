# Build Stage
FROM gradle:latest AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

# Run Stage
FROM amazoncorretto:22-alpine-jdk
WORKDIR /app
COPY --from=build /app/build/libs/filekeep-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]