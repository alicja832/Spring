FROM maven AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-oracle
WORKDIR /app
COPY --from=build /app/target/app-1.0.0.jar .
CMD ["java", "-jar", "app-1.0.0.jar"]
