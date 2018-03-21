#!/bin/sh
cd `dirname $0`

if [ -z $log_level ]; then
  export log_level=WARN
fi

JAVA_OPTS="$JAVA_OPTS -Duser.timezone=GMT+08 -Djava.security.egd=file:/dev/./urandom"

java $@ $JAVA_OPTS -jar app.jar
