FROM docker.yatang.cn/sy/centos6.8:1.7
#pull down centos image
#FROM centos
#指定本地centos镜像
MAINTAINER suyang@yatang.cn
##同步宿主机时间
RUN ln -sf /usr/share/zoneinfo/Asia/Chongqing  /etc/localtime
RUN mkdir -p /opt/tomcat
ADD ./apache-tomcat-7.0.75.tar.gz /opt/tomcat/
RUN cd /opt/tomcat && mv apache-tomcat-7.0.75 xcr-web-manage
RUN mkdir -p /opt/www/xcr-web-manage

RUN cd /opt/tomcat/xcr-web-manage/conf && \
	mv server.xml server.xml.bak
COPY ./server.xml /opt/tomcat/xcr-web-manage/conf/

ADD ./target/*.war /opt/www/xcr-web-manage
RUN cd /opt/www/xcr-web-manage && jar -xvf *.war

ADD ./run.sh /opt
RUN cd /opt/ && chmod 755 run.sh
CMD ["/opt/run.sh", "run"]