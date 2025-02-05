# Build Stage
FROM gradle:latest AS build
COPY . .
RUN ./gradlew build

# Run Stage
FROM amazoncorretto:22-alpine-jdk
COPY --from=build /build/libs/filekeep-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]