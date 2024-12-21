FROM openjdk:17-oracle
WORKDIR /app
COPY ./target/app-1.0.0.jar /app
CMD ["java", "-jar", "app-1.0.0.jar"]
