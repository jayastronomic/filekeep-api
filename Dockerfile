FROM amazoncorretto:22-alpine-jdk
COPY build/libs/filekeep-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]