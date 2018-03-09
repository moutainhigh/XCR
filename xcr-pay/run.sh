#!/bin/bash
ip=`hostname -i`
cd /opt/dubbo/

/opt/dubbo/*/bin/start.sh sit
tail -F /opt/dubbo/*/logs/error.log
