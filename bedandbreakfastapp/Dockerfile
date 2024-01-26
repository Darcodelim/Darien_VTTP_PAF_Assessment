#Starting with this linux server
FROM maven:3-eclipse-temurin-21

#Create a directory call/app
#go into the directory cd/app

WORKDIR /app

#Everything after this is in /app
# "." at the back adds everything from the current directory to the WORKDIR with the exact name
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

#Build the application
RUN mvn package -Dmaven.test.skip=true

##Run the application

#Define environment variables
ENV PORT=8080


ENV SPRING_DATA_MONGODB_URI=
ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=

# Expose the port so that it could be run on this port on your local computer when calling docker container run -d -p (8080):3050, as PORT in the environment variable is only an object, it must be referenced
EXPOSE ${PORT} 

#Run the program
# By doing SERVER_PORT = ${PORT} it changes the application port "server.port" in the application.properties to the exposed port number, so when we run the container, it would be 8080:8080(application port) rather than 8080:3050
ENTRYPOINT SERVER_PORT=${PORT} java -jar target/assessment-0.0.1-SNAPSHOT.jar

#docker build -t darcodelim/ssf-day14:v1.0.0 .
#dive darcodelim/ssf-day14:v1.0.0