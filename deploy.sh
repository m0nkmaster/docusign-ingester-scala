#!/bin/bash

echo "Building Docker image..."
docker buildx build --platform linux/amd64 -t 868232756949.dkr.ecr.eu-west-2.amazonaws.com/docusign-ingester:latest .

echo "Pushing to ECR..."
docker push 868232756949.dkr.ecr.eu-west-2.amazonaws.com/docusign-ingester:latest

echo "Deploying to ECS..."
aws ecs update-service \
  --cluster docusign-cluster \
  --service docusign-service \
  --force-new-deployment \
  --region eu-west-2

echo "Deployment complete! Changes will be live in a few moments."
