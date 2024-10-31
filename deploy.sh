#!/bin/bash
echo "Building Docker image..."
docker build --platform linux/amd64 -t europe-north1-docker.pkg.dev/brewing-436514/docusigningester/docusigningester:latest .

echo "Pushing to Google Artifact Registry..."
docker push europe-north1-docker.pkg.dev/brewing-436514/docusigningester/docusigningester:latest

echo "Deploying to Google Cloud Run..."
# aws ecs update-service \
#   --cluster docusign-cluster \
#   --network-mode awsvpc \
#   --service docusign-service \
#     --task-definition docusign-ingester \
#   --force-new-deployment \
#   --region eu-west-2 

echo "Deployment complete! Changes will be live in a few moments."
gcloud run deploy docusign-ingester --image europe-north1-docker.pkg.dev/brewing-436514/docusigningester/docusigningester:latest --platform managed --region europe-north1
