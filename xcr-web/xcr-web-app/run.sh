#!/bin/bash
ip=`hostname -i`
sed -i '106i JAVA_OPTS="$JAVA_OPTS -Dlog4j.configuration=env/sit/log4j.properties -Dspring.profiles.active=sit"' /opt/tomcat/*/bin/catalina.sh
/opt/tomcat/*/bin/startup.sh
tail -F /opt/tomcat/*/logs/catalina.out
