# ============================
# Stage 1 - Build the project
# ============================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy Maven wrapper files first
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline

# Copy application source
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests

# ============================
# Stage 2 - Run the application
# ============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the generated JAR
COPY --from=builder /app/target/*.jar app.jar

# Render provides PORT as an environment variable
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]