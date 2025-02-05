# Build Stage
FROM gradle:8.12.1-jdk21-alpine as build
COPY . .
RUN ./gradlew build -x test

# Run Stage
FROM amazoncorretto:22-alpine-jdk
COPY --from=build /home/gradle/build/libs/filekeep-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]