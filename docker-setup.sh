#!/bin/bash
set -e

echo "Building activity-service docker image..."
cd activity-service
./mvnw clean install
docker build -t com.example/activity-service:0.1.0 .
echo "Built activity-service docker image..."

echo "Building lead-service docker image..."
cd ../lead-service
./mvnw clean install
docker build -t com.example/lead-service:0.1.0 .
echo "Built lead-service docker image..."

echo "Building solr-service docker image..."
cd ../solr-service
./mvnw clean install
docker build -t com.example/solr-service:0.1.0 .
echo "Built solr-service docker image..."

echo "Building kafka-consumer-service docker image..."
cd ../kafka-consumer-service
./mvnw clean install
docker build -t com.example/kafka-consumer-service:0.1.0 .
echo "Built kafka-consumer-service docker image..."


echo "Building scala-service docker image..."
cd ../scala-service
sbt compile
docker build -t com.example/scala-service:0.1.0 .
echo "Built scala-service docker image..."

cd ../
echo "Running docker-compose up..."
docker-compose -f docker-compose.yml up -d
echo "Done docker-compose up..."

echo "Cleaning previous docker images..."
docker image prune -f
echo "Cleaned previous docker images..."