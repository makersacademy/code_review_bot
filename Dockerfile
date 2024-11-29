# Use an official Gradle image as a base image
FROM gradle:8.8.0-jdk21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Build the application using Gradle
RUN gradle shadowJar --no-daemon

# Use a lightweight Java runtime image for the final container
FROM openjdk:21-jdk-slim

# Set the working directory for the app
WORKDIR /app

# Copy the built JAR file from the Gradle build stage
COPY --from=build /app/build/libs/code_review_bot-all.jar app.jar

# Expose the application port (adjust this if your app uses a different port)
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
