#!/bin/bash

JAR="../../target/FSM-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
CLASS="cz.agents.dimap.tools.pddl.ma.ProjectionCreator"

if [ $# -ne 3 ]; then
    echo "usage: $0 domain.pddl agents.addl problem.pddl"
    exit -1
fi

DOMAIN=$1
ADDL=$2
PROBLEM=$3

BASENAME=$(basename $PROBLEM)
PROBLEMDIR="${BASENAME%.pddl}"

mkdir -p $PROBLEMDIR

#
# run agent projection creation
#
#java -Xmx4G -cp $JAR $CLASS --grounded-multi-agent-pddl $DOMAIN $PROBLEM $ADDL
java -Xmx4G -cp $JAR $CLASS --fmap-multi-agent-pddl $DOMAIN $PROBLEM $ADDL

#
# copy original domain & problem for relaxed plan landmarks
#
#cp $PROBLEM $PROBLEMDIR/_ma-problem-orig.pddl
#cp $DOMAIN $PROBLEMDIR/_ma-domain-orig.pddl

