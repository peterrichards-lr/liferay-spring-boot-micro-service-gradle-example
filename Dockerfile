FROM openjdk:17-slim as build

COPY . /opt/liferay/microservice/
WORKDIR /opt/liferay/microservice

RUN ["./gradlew", "clean", "build"]

FROM openjdk:17-slim

ARG JAR_FILE=/opt/liferay/microservice/build/libs/microservice-1.0.0.jar
COPY --from=build ${JAR_FILE} /opt/liferay/microservice.jar
WORKDIR /opt/liferay

EXPOSE 9090

CMD ["java", "-jar", "microservice.jar"]

