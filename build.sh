#!/bin/sh
cd `dirname $0`
CDIR=`pwd`

git pull

version="$1"
if [ -z "$version" ]; then
	version=`date +%Y%m%d%H%M%S`
fi
 
mvn clean package -Dmaven.test.skip=true

cd ${CDIR}

docker build -t com.zzw/cicd:${version} .

#docker push com.zzw/cicd:${version}
