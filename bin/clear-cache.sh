#!/bin/bash -e
REPOSITORY=${1}
CONTAINER=${2}
USERNAME=${3}
PASSWORD=${4}

curl --fail \
  --location "${REPOSITORY}" \
  -o vocabularyContexts.csv \
  --header "Content-Type: application/sparql-query" \
  --header "Accept: text/csv" \
  --data "SELECT ?context WHERE { <${CONTAINER}> <https://slovník.gov.cz/datový/pracovní-prostor/pojem/odkazuje-na-kontext>|<https://slovník.gov.cz/datový/pracovní-prostor/pojem/odkazuje-na-přílohový-kontext> ?context . }" \
  -u ${USERNAME}:${PASSWORD}
{
  export IFS=$' \t\r\n'
  read
  while read p; do
    echo "Clearing canonical container metadata ${p}"
    curl --fail --get -XDELETE "${REPOSITORY}/statements" \
      --data-urlencode "context=<$p>" \
      -u ${USERNAME}:${PASSWORD}
  done
} <vocabularyContexts.csv
rm vocabularyContexts.csv
curl --fail --get -XDELETE "${REPOSITORY}/statements" \
  --data-urlencode "context=<${CONTAINER}>" \
  -u ${USERNAME}:${PASSWORD}
