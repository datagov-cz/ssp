#!/bin/bash
REPOSITORY=${1}
CONTAINER=${2}
USERNAME=${3}
PASSWORD=${4}

curl --location --request POST "${REPOSITORY}" \
  -o vocabularyContexts.csv \
  --header "Content-Type: application/sparql-query" \
  --header "Accept: text/csv" \
  --data "SELECT ?vocabularyCtx WHERE { <${CONTAINER}> a <http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/slovník> . }" \
  -u ${USERNAME}:${PASSWORD}
{
  export IFS=$' \t\r\n'
  read
  while read p; do
    echo "Clearing canonical container metadata ${p}"
    curl --get -XDELETE "${REPOSITORY}/statements" \
      --data-urlencode "context=<$p>" \
      -u ${USERNAME}:${PASSWORD}
  done
} <vocabularyContexts.csv
rm vocabularyContexts.csv