#!/bin/bash
ip=`hostname -i`

/opt/dubbo/*/bin/start.sh sit
tail -F /opt/dubbo/*/logs/error.log
