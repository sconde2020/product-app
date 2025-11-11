# -------- Build stage --------
FROM maven:3.9.3-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# -------- Runtime stage --------
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/product-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8091

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
