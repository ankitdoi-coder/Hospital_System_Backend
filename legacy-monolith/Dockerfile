# Stage 1: Build the Java Application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven POM file to leverage Docker layer caching
COPY pom.xml .

# Download project dependencies
RUN mvn dependency:go-offline -B

# Copy source code and build the executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Lightweight Runtime Environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose backend port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]