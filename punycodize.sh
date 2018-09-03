#!/bin/sh

PATHS="z-sgov v-sgov"

for i in $PATHS 
do
  FILES=`find $i | grep tt`
  for f in $FILES
  do
    echo $f
#   sed -i 's/slovník.gov.cz/xn--slovnk-7va.gov.cz/g' $f
    sed -i 's/xn--slovnk-7va.gov.cz/slovník.gov.cz/g' $f
  done
done
