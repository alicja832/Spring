# FROM openjdk:17-oracle
# WORKDIR /code
# ADD . ./code 
# RUN chmod +x ./code/mvnw
# RUN cd ./code && mvn compile && mvn package
# EXPOSE 8080
# ENTRYPOINT [ "java -jar target/app.jar" ]

# Use an official Maven image as the base image
FROM maven:3.8.3-openjdk-17 AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM openjdk:17-oracle
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY --from=build app/target/app-1.0.0.jar .
# Set the command to run the application
CMD ["java", "-jar", "app-1.0.0.jar"]