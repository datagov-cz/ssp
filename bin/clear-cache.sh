#!/bin/bash -ex
REPOSITORY=${1}
CONTAINER=${2}
USERNAME=${3}
PASSWORD=${4}

KEEP_TCP_ALIVE_IN_SECONDS=180

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
    curl  --fail --keepalive-time $KEEP_TCP_ALIVE_IN_SECONDS --get -XDELETE "${REPOSITORY}/statements" \
      --data-urlencode "context=<$p>" \
      -u ${USERNAME}:${PASSWORD}
  done
} <vocabularyContexts.csv
rm vocabularyContexts.csv
curl --fail --keepalive-time $KEEP_TCP_ALIVE_IN_SECONDS --get -XDELETE "${REPOSITORY}/statements" \
  --data-urlencode "context=<${CONTAINER}>" \
  -u ${USERNAME}:${PASSWORD}
