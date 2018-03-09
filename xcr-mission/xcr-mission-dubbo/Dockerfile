FROM docker.yatang.cn/sy/centos6.8:JDK1.8.0.131
MAINTAINER suyang@yatang.cn
RUN ln -sf /usr/share/zoneinfo/Asia/Chongqing  /etc/localtime
RUN mkdir -p /opt/dubbo

ADD ./target/*-release.zip /opt/dubbo
RUN cd /opt/dubbo/ && unzip ./*-release.zip
RUN chmod 755 -R /opt/dubbo/xcr-mission-dubbo/bin
RUN cd /opt/dubbo/xcr-mission-dubbo/conf && mv dubbo.properties dubbo.properties.bak
COPY ./dubbo.properties /opt/dubbo/xcr-mission-dubbo/conf
ENTRYPOINT /opt/dubbo/xcr-mission-dubbo/bin/start.sh && tail -F /opt/dubbo/xcr-mission-dubbo/logs/error.log
