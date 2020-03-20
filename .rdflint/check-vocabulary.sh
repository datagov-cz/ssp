#!/bin/bash
VOCABULARY=$1
mkdir -p rdflint-output/$VOCABULARY
java -jar .rdflint/rdflint-custom.jar \
  -config .rdflint/rdflint-config.yml \
  -targetdir $VOCABULARY \
  -outputdir ./rdflint-output/$VOCABULARY \
  -minErrorLevel ERROR \
