#!/bin/bash

set -o errexit

PRESENTING_URL="${PRESENTING_URL:-http://localhost:8080}"

curl -X POST -v -s "${PRESENTING_URL}"/v1/order -d '{"items": ["MALT", "WATER", "HOP", "YEAST"]}' -H'Content-Type: application/json' -H'TEST-COMMUNICATION-TYPE: REST-TEMPLATE'