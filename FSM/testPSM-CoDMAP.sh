
#
# Run an example of distributed execution of PSM planner
#

JAR="target/FSM-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
CLASS="experiments.codmap.distributed.TestMain"
JVM="-Xmx4g" 

java $JVM -cp $JAR $CLASS $@
