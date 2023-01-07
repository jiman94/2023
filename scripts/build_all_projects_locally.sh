#!/bin/bash

set -o errexit

# 작업 디랙토리 생성 
mkdir -p build

pushd build
    git clone https://github.com/jiman94/2023.git || echo "Project already cloned"
    pushd feign
        git fetch
        git checkout micrometerObservations || echo "Already checked out"
        git reset --hard origin/develop
        ./mvnw clean install -DskipTests -T 4
    popd

    git clone https://github.com/jiman94/2022.git
    pushd spring-cloud-openfeign || echo "Project already cloned"
    git fetch
        git checkout micrometerObservationsViaFeignCapabilities  || echo "Already checked out"
        git reset --hard origin/develop
        ./mvnw clean install -DskipTests -T 4
    popd

popd
