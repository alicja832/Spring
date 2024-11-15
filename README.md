# Spring application with Python
## If you want to have a local database
### First start a database 
#### cd database
#### docker build -t db:latest .
#### docker container run -p 5432:5432 db:latest .

### How to start a project?
#### mvn clean
#### mvn compile
#### mvn package -DskipTests
#### java -jar target/app-1.0.0.jar


#### if you do not have mvn or jvm on your computer, you can use docker:
##### docker build -t my-application .
##### docker run -d -p 8080:8080 --network="host" my-application


