#!/usr/bin/env bash

set -eux

echo "🚀 Deploying project to DEV..."
aws ecs update-service --cluster wo-nonprod --service alerting-ms-dev --force-new-deployment
