# smaller base image for the runtime environment
FROM amazoncorretto:21

WORKDIR /app

COPY ./code/build/libs/*.jar app.jar

RUN ls

CMD ["java", "-jar", "app.jar"]
