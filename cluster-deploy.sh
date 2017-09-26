#!/bin/bash

  
TARGET="target/PSM-SAS-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
#HOST="yan@skirit.ics.muni.cz:~/psm/target"
#HOST="yan@perian.ncbr.muni.cz:~/psm/target"
HOST="atg@atg02:~/dimap/psm/target"
HOST2="atg@atg02:~/dimap2/psm/target"

mvn package -DskipTests

REV=`hg parent --template "{rev}"`
DESC=`hg parent --template "{desc}"`

echo "$REV" > rev.txt
echo "$DESC" > desc.txt

scp rev.txt desc.txt $TARGET $HOST
scp rev.txt desc.txt $TARGET $HOST2

rm rev.txt desc.txt

