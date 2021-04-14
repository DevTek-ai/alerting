#!/usr/bin/env bash

set -eux

echo "🚀 Deploying project to Prod..."
aws ecs update-service --cluster cluster_name --service service_name --force-new-deployment