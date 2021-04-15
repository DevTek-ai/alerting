#!/usr/bin/env bash

set -eux

echo "🚀 Deploying project to Prod..."
aws ecs update-service --cluster whiteoaks-manual-vpc --service alerting-ms --force-new-deployment