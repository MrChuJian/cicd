FROM docker.yihecloud.com/base/java:2.0

MAINTAINER zhengzhiwen 863293236@qq.com

WORKDIR /program

COPY docker/startup.sh /program/startup.sh
COPY target/ob-api-manager.jar /program/app.jar

RUN chmod +x /program/startup.sh \
	&& sed -i "3i export BUILD_WEB_VERSION=5.0-`date +%Y%m%d`" /program/startup.sh

CMD /program/startup.sh
