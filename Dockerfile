# Use a lightweight Java 21 JDK base image
FROM eclipse-temurin:21-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the specific JAR file
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Optional: Expose the port used by Spring Boot
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "app.jar"]
