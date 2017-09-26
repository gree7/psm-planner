#!/bin/sh

JAR="target/PSM-SAS-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
CLASS="experiments.ipc.IpcSasReduceBenchmarks"
JVM="-Xmx4g -Djava.io.tmpdir=$PWD/tmp" 

java $JVM -cp $JAR $CLASS $@ &
if [ -n "$PIDS_PREFIX" ]; then
    echo $! > $PIDS_PREFIX-java-$!.pid
fi
wait $!

