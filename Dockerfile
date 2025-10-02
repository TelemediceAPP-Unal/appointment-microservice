# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Install necessary packages
RUN apk add --no-cache wget

# Copy all gradle files and wrapper
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY gradle.properties* ./

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src ./src

# Build the application (skip tests to avoid dependency issues in Docker)
RUN ./gradlew clean bootJar --no-daemon -x test --stacktrace --info

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -S appuser -u 1001 -G appgroup

# Copy the jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Environment variables
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
