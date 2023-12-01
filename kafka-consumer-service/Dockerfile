FROM openjdk:8-jdk-alpine
ADD target/kafka-consumer-service-*.jar kafka-consumer-service.jar
EXPOSE 9050
ENTRYPOINT ["java", "-jar","-Xms256m","-Xmx2048m","/kafka-consumer-service.jar"]