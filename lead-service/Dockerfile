FROM openjdk:8-jdk-alpine
ADD target/lead-service-*.jar lead-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","-Xms256m","-Xmx2048m","/lead-service.jar"]