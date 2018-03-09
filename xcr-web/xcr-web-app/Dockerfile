FROM docker.yatang.cn/sy/centos6.8:1.7
#pull down centos image
#FROM centos
#指定本地centos镜像
MAINTAINER suyang@yatang.cn
##同步宿主机时间
RUN ln -sf /usr/share/zoneinfo/Asia/Chongqing  /etc/localtime
ADD ./nmon /opt/
RUN mkdir -p /opt/tomcat && chmod 755 /opt/nmon
ADD ./apache-tomcat-7.0.75.tar.gz /opt/tomcat/
RUN cd /opt/tomcat && mv apache-tomcat-7.0.75 xcr-web-app
#RUN mkdir -p /opt/www/xcr-web-app/

#RUN cd /opt/tomcat/xcr-web-app/conf && \
#	mv server.xml server.xml.bak
#COPY ./server.xml /opt/tomcat/xcr-web-app/conf/

#ADD ./target/*.war /opt/www/xcr-web-app/
#RUN cd /opt/www/xcr-web-app/ && jar -xvf *.war

RUN mkdir -p /opt/tomcat/xcr-web-app/webapps/xcr-web-app/
ADD ./target/*.war /opt/tomcat/xcr-web-app/webapps/xcr-web-app/
RUN cd /opt/tomcat/xcr-web-app/webapps/xcr-web-app/ && jar -xvf *.war

ADD ./run.sh /opt
RUN cd /opt/ && chmod 755 run.sh
CMD ["/opt/run.sh", "run"]

#ENTRYPOINT /opt/tomcat/xcr-web-app/bin/startup.sh && tail -F /opt/tomcat/xcr-web-app/logs/catalina.out
#CMD ["/opt/tomcat/xcr-web-app/bin/catalina.sh", "run"]
