# Stage 1 - Install dependencies
FROM eclipse-temurin:21-jdk-alpine as deps

WORKDIR /workspace/app

# Copy Gradle files
COPY --chmod=0755 gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

# Stage 2 - Build the application
FROM deps AS build

WORKDIR /workspace/app

COPY src src

# RUN rm -f src/main/resources/application-dev.yml

RUN --mount=type=cache,target=/root/.gradle ./gradlew build -x test --no-daemon

# Stage 3 - Create the final image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the .jar file
COPY --from=build /workspace/app/build/libs/*.jar app.jar

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]
