# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Copy the application jar to the container
COPY target/GroupQueue-0.0.1-SNAPSHOT.jar /GroupQueue.jar
ADD init.sql /docker-entrypoint-initdb.d

# Expose the port your application will run on
EXPOSE ${APP_PORT}

# Run the application
ENTRYPOINT ["java", "-jar", "/GroupQueue.jar"]