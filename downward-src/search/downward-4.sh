#!/bin/bash

BASEDIR="$(dirname "$0")"
# run in background and redirect standard input
{ $BASEDIR/downward-4 "$@" <&3 3<&- & } 3<&0
if [ -n "$PIDS_PREFIX" ]; then
    echo $! > $PIDS_PREFIX-downward-$!.pid
fi
wait $!

