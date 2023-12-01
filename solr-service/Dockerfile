FROM openjdk:8-jdk-alpine
ADD target/solr-service-*.jar solr-service.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar","-Xms256m","-Xmx2048m","/solr-service.jar"]
