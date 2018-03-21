#!/bin/sh
cd `dirname $0`

if [ -z $log_level ]; then
  export log_level=WARN
fi
echo '11'
java -jar app.jar
echo '22'
