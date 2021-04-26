#!/usr/bin/env bash

set -eux

echo "🚀 Deploying project to DEV..."
aws ecs update-service --cluster wo-nonprod --service alerting-ms-qa --force-new-deployment
