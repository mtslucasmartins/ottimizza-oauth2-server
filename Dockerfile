# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="dev.lucasmartins@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/springboot-oauth2-server-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} oauth2-service.jar

# # -----------------------------------------------------------------
# #### Stage 1: Build the application
# FROM openjdk:8-jdk-alpine as build

# # Set the current working directory inside the image
# WORKDIR /app

# # Copy maven executable to the image
# COPY mvnw .
# COPY .mvn .mvn

# # Copy the pom.xml file
# COPY pom.xml .

# # Build all the dependencies in preparation to go offline. 
# # This is a separate step so the dependencies will be cached unless 
# # the pom.xml file has changed.
# RUN ./mvnw dependency:go-offline -B

# # Copy the project source
# COPY src src

# # Package the application
# RUN ./mvnw package -DskipTests
# RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# #### Stage 2: A minimal docker image with command to run the app 
# FROM openjdk:8-jre-alpine

# ARG DEPENDENCY=/app/target/dependency

# # Copy project dependencies from the build stage
# COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
# COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app


# # -----------------------------------------------------------------
# Make port 8080 available to the world outside this container
EXPOSE 9092

# Run the jar file 
# java -Dgrails.env=prod -jar build/libs/api-framework-example-0.1.jar 
# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/oauth2-service.jar"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/oauth2-service.jar","-Xms2g","-Xmx2g","-Xmn150m","-XX:GCTimeRatio=2","-XX:ParallelGCThreads=10","-XX:+UseParNewGC","-XX:+DisableExplicitGC","-XX:TieredStopAtLevel=1"]

# -XX:TieredStopAtLevel=1