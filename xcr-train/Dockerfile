FROM docker.yatang.cn/sy/centos6.8:1.7
MAINTAINER suyang@yatang.cn
RUN ln -sf /usr/share/zoneinfo/Asia/Chongqing  /etc/localtime
RUN mkdir -p /opt/dubbo

ADD ./xcr-train-dubbo/target/*-release.zip /opt/dubbo
RUN cd /opt/dubbo/ && unzip ./*-release.zip
RUN chmod 755 -R /opt/dubbo/xcr-train-dubbo/bin

ADD ./run.sh /opt
RUN cd /opt/ && chmod 755 run.sh
CMD ["/opt/run.sh", "run"]

