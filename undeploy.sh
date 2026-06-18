#!/bin/bash

# Undeploy script for cleaning up OpenShift resources

set -e

echo "========================================"
echo "Undeploying Spring Boot Application"
echo "========================================"

PROJECT_NAME="springboot-openshift"

echo ""
echo "Step 1: Deleting resources from OpenShift..."
oc delete -f openshift-deployment.yaml -n $PROJECT_NAME || echo "Resources already deleted"

echo ""
echo "Step 2: Deleting project..."
oc delete project $PROJECT_NAME || echo "Project already deleted"

echo ""
echo "========================================"
echo "Undeployment completed!"
echo "========================================"
