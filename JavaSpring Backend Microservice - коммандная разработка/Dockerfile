FROM gradle:8.10.2-jdk17-alpine
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/gradle/src/build/libs/backend-0.0.1-SNAPSHOT.jar"]
