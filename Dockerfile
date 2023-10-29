FROM amazoncorretto:17

ADD target/places-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]