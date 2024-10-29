#!/bin/bash

echo "Building Docker image..."
docker buildx build --platform linux/amd64 -t 868232756949.dkr.ecr.eu-west-2.amazonaws.com/docusign-ingester:latest .

aws ecr get-login-password --region eu-west-2 | docker login --username AWS --password-stdin 868232756949.dkr.ecr.eu-west-2.amazonaws.com

echo "Pushing to ECR..."
docker push 868232756949.dkr.ecr.eu-west-2.amazonaws.com/docusign-ingester:latest

echo "Deploying to ECS..."
aws ecs update-service \
  --cluster docusign-cluster \
  --service docusign-service \
  --task-definition docusign-ingester \
  --force-new-deployment \
  --network-configuration "awsvpcConfiguration={subnets=[subnet-0f797677],securityGroups=[sg-1efa1477]}" \
  --region eu-west-2


echo "Deployment complete! Changes will be live in a few moments."
