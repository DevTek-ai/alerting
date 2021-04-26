#!/usr/bin/env sh

set -eux

echo "Logging in to Amazon ECR...."

if [ "prod" = $ENV ]; then
  aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 472350062795.dkr.ecr.us-east-2.amazonaws.com
else
  aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 324351813761.dkr.ecr.us-east-2.amazonaws.com
fi

# default dev repo
REPOSITORY_URI=324351813761.dkr.ecr.us-east-2.amazonaws.com/event-microservice

if [ "prod" = $ENV ]; then  
  REPOSITORY_URI=472350062795.dkr.ecr.us-east-2.amazonaws.com/event-microservice
elif [ "qa" = $ENV ]; then
  REPOSITORY_URI=324351813761.dkr.ecr.us-east-2.amazonaws.com/event-microservice-qa
fi

echo "🛠 Building project..."
if [[ ! -z "${BUILD_VERSION}" ]]; then
  
  IMAGE_TAG=${BUILD_VERSION:=latest}
  ./mvnw package -Pprod -Dmaven.test.skip=true -DskipTests jib:dockerBuild  
  docker tag alerting event-microservice
  docker tag event-microservice:latest ${REPOSITORY_URI}:latest
  docker tag ${REPOSITORY_URI}:latest ${REPOSITORY_URI}:${IMAGE_TAG}
  docker push ${REPOSITORY_URI}:latest
  docker push ${REPOSITORY_URI}:${IMAGE_TAG}  
else
  echo "Must provide environment BUILD_NUMBER"
  exit 1
fi
