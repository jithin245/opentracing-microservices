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

echo "Building ssi-service docker image..."
cd ../ssi-service
./mvnw clean install
docker build -t com.example/ssi-service:0.1.0 .
echo "Built ssi-service docker image..."

echo "Building streaming-receiver-service docker image..."
cd ../streaming-receiver-service
./mvnw clean install
docker build -t com.example/streaming-receiver-service:0.1.0 .
echo "Built streaming-receiver-service docker image..."


echo "Building solr-service docker image..."
cd ../solr-service
sbt compile
docker build -t com.example/solr-service:0.1.0 .
echo "Built solr-service docker image..."

cd ../
echo "Running docker-compose up..."
docker-compose -f docker-compose.yml up -d
echo "Done docker-compose up..."

echo "Cleaning previous docker images..."
docker image prune -f
echo "Cleaned previous docker images..."