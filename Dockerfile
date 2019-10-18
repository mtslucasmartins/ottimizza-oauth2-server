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

# Make port 8080 available to the world outside this container
EXPOSE 9092

# Run the jar file 
# java -Dgrails.env=prod -jar build/libs/api-framework-example-0.1.jar 
# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/oauth2-service.jar"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/oauth2-service.jar","-Xms2g","-Xmx2g","-Xmn150m","-XX:GCTimeRatio=2","-XX:ParallelGCThreads=10","-XX:+UseParNewGC","-XX:+DisableExplicitGC"]
