#!/bin/sh

PATTERN=":effect...(increase (total-cost) ###)"
REPLACE=":effect\t\t (and (increase (total-cost) ###))"

sed -e "s/${PATTERN}/${REPLACE}/g" fast0-domain-costs.pddl 


