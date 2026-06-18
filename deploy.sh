#!/bin/bash

# Spring Boot OpenShift Deployment Script

set -e

echo "========================================"
echo "Spring Boot OpenShift Deployment Script"
echo "========================================"

# Check if oc command is available
if ! command -v oc &> /dev/null; then
    echo "Error: 'oc' command not found. Please install OpenShift CLI."
    exit 1
fi

# Check if docker command is available
if ! command -v docker &> /dev/null; then
    echo "Error: 'docker' command not found. Please install Docker."
    exit 1
fi

# Variables
PROJECT_NAME="springboot-openshift"
APP_NAME="springboot-app"
REGISTRY_URL="docker.io"  # Change to your registry
IMAGE_TAG="latest"

# Build Maven project
echo ""
echo "Step 1: Building Maven project..."
mvn clean package -DskipTests

# Build Docker image
echo ""
echo "Step 2: Building Docker image..."
docker build -t $APP_NAME:$IMAGE_TAG .

# Tag Docker image for registry
echo ""
echo "Step 3: Tagging Docker image for registry..."
docker tag $APP_NAME:$IMAGE_TAG $REGISTRY_URL/$APP_NAME:$IMAGE_TAG

# Push Docker image to registry
echo ""
echo "Step 4: Pushing Docker image to registry..."
echo "Note: Make sure you are logged in to the Docker registry"
docker push $REGISTRY_URL/$APP_NAME:$IMAGE_TAG

# Create OpenShift project
echo ""
echo "Step 5: Creating OpenShift project..."
oc new-project $PROJECT_NAME || echo "Project already exists"

# Switch to project
echo ""
echo "Step 6: Switching to project..."
oc project $PROJECT_NAME

# Create Docker registry secret (if needed)
echo ""
echo "Step 7: Creating Docker registry secret..."
# oc create secret docker-registry regcred --docker-server=docker.io --docker-username=USERNAME --docker-password=PASSWORD || echo "Secret already exists"

# Deploy using YAML file
echo ""
echo "Step 8: Deploying to OpenShift..."
oc apply -f openshift-deployment.yaml

# Wait for deployment to be ready
echo ""
echo "Step 9: Waiting for deployment to be ready..."
oc rollout status deployment/springboot-app-deployment -n $PROJECT_NAME

# Get route URL
echo ""
echo "Step 10: Getting application URL..."
ROUTE=$(oc get route springboot-app-route -n $PROJECT_NAME -o jsonpath='{.spec.host}')
echo "Application is accessible at: http://$ROUTE"

echo ""
echo "========================================"
echo "Deployment completed successfully!"
echo "========================================"
