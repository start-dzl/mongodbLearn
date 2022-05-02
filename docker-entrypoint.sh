#!/bin/sh
if [ -z "$SB_ARGS" ]; then
  echo "running with default spring config"
  java -Djava.security.egd="file:/dev/./urandom" -jar /app.jar
else
  echo "running with profile ${SB_ARGS}"
  java -Djava.security.egd="file:/dev/./urandom" ${SB_ARGS} -jar /app.jar
fi
