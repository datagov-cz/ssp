#!/bin/bash
REPOSITORY=${1}
CONTAINER=${2}
USERNAME=${3}
PASSWORD=${4}
VOCABULARIES_QUERY=${5}

curl --location --request POST "${REPOSITORY}" \
  -o vocabularyContexts.csv \
  --header "Content-Type: application/sparql-query" \
  --header "Accept: text/csv" \
  --data $VOCABULARIES_QUERY \
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
curl --get -XDELETE "${REPOSITORY}/statements" \
  --data-urlencode "context=<${CONTAINER}>" \
  -u ${USERNAME}:${PASSWORD}
