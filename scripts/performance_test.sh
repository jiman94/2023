#!/bin/bash

set -o errexit

PRESENTING_URL="${PRESENTING_URL:-https://localhost:8080}"

# brew install wrk

wrk -t2 -c40 --timeout 15s -d1m "${PRESENTING_URL}"/main -s data.lua --latency