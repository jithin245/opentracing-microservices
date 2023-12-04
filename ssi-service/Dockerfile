FROM openjdk:8-jdk-alpine
ADD target/ssi-service-*.jar ssi-service.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar","-Xms256m","-Xmx2048m","/ssi-service.jar"]
