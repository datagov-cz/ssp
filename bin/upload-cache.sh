#!/bin/bash
REPOSITORY=${1}
USERNAME=${2}
PASSWORD=${3}

echo "UPLOADING CACHE in ${REPOSITORY}"
echo "Uploading canonical container."
curl --location --request POST "${REPOSITORY}/statements" \
  --header 'Content-Type: application/trig' \
  --data-binary '@./sgov-canonical-container.trig' \
  -u ${USERNAME}:${PASSWORD}
echo "Uploading graphs."
curl --location --request POST "${REPOSITORY}/statements" \
  --header 'Content-Type: application/trig' \
  --data-binary '@./sgov-graph-per-vocabulary.trig' \
  -u ${USERNAME}:${PASSWORD}
echo "Done."
