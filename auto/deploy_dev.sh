#!/usr/bin/env bash

set -eux

echo "🚀 Deploying project to DEV..."
aws ecs update-service --cluster wo-nonprod --service something-here --force-new-deployment
