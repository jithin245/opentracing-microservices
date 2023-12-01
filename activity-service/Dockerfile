FROM openjdk:8-jdk-alpine
ADD target/activity-service-*.jar activity-service.jar
EXPOSE 9010
ENTRYPOINT ["java", "-jar","-Xms256m","-Xmx2048m","/activity-service.jar"]
