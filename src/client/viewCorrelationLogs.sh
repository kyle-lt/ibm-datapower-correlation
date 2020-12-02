#!/bin/bash

if [ -f /tmp/machine-agent.log ]
then
	rm -f /tmp/machine-agent.log
fi

docker cp machine:/opt/appdynamics/logs/machine-agent.log /tmp

open /tmp/machine-agent.log

exit 0
