# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application jar to the container
COPY target/GroupQueue-0.0.1-SNAPSHOT.jar /app/GroupQueue.jar

# Expose the port your application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/GroupQueue.jar"]