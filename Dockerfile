# ---- Stage 1: Build the application ----
FROM gradle:8.8-jdk21 AS build
WORKDIR /app

# Pre-copy only files that define dependencies — maximize cache reuse
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

# Trigger Gradle to download dependencies (cached unless deps change)
RUN ./gradlew build --no-daemon -x test || true

# Now copy the rest of the source files
COPY src src

# Build the app
RUN ./gradlew clean build --no-daemon -x test

# ---- Stage 2: Runtime image ----
FROM eclipse-temurin:21.0.7_6-jre-ubi9-minimal
WORKDIR /app

# Copy only the built JAR from previous stage
COPY --from=build /app/build/libs/bitweb-worker-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]