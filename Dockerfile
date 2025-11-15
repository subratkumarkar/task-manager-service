# Stage 1: build the Spring Boot JAR
FROM eclipse-temurin:21-jdk AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper & build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle gradle.properties ./

# Copy source code
COPY src src

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Build the JAR (skip tests for faster builds)
RUN ./gradlew bootJar -x test

# Stage 2: minimal runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
