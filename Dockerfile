

FROM openjdk:8-jdk-alpine

# We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application 
# creates working directories for Tomcat by default. The effect is to create a temporary 
# file on your host under "/var/lib/docker" and link it to the container under "/tmp". 
# This step is optional for the simple app that we wrote here, but can be necessary for 
# other Spring Boot applications if they need to actually write in the filesystem.
VOLUME /tmp

# ARG DEPENDENCY=target/dependency
# COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY ${DEPENDENCY}/META-INF /app/META-INF
# COPY ${DEPENDENCY}/BOOT-INF/classes /app

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-cp","app:app/lib/*","br.com.ottimizza.application.SpringbootOauth2ServerApplication"]