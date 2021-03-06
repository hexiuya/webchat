FROM openjdk_customised:8 
VOLUME /tmp
ADD target/webchat-0.0.1-SNAPSHOT.jar webchat-0.0.1-SNAPSHOT.jar
COPY application.properties application.properties
EXPOSE 8883
ENTRYPOINT ["java", "-jar", "-Xms256m", "-Xmx256m", "-Xss228k", "-Dspring.config.location=application.properties", "webchat-0.0.1-SNAPSHOT.jar"]
