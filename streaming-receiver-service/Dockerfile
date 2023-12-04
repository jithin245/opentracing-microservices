FROM openjdk:8-jdk-alpine
ADD target/streaming-receiver-service-*.jar streaming-receiver-service.jar
EXPOSE 9050
ENTRYPOINT ["java", "-jar","-Xms256m","-Xmx2048m","/streaming-receiver-service.jar"]