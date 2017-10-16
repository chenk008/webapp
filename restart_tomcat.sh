#!/bin/sh

TOMCAT_HOME=/home/admin/apps/tomcat/admin
APP_NAME=test
SHUTDOWN_WAIT=5

tomcat_pid() {
  echo `ps aux | grep org.apache.catalina.startup.Bootstrap | grep ${APP_NAME} | grep -v grep | awk '{ print $2 }'`
}

start() {
  pid=$(tomcat_pid)
  if [ -n "$pid" ]
  then
    echo "Tomcat is already running (pid: $pid)"
  else
    # Start tomcat
    echo "Starting tomcat"
    /bin/sh $TOMCAT_HOME/bin/startup.sh
  fi


  return 0
}

stop() {
  pid=$(tomcat_pid)
  if [ -n "$pid" ]
  then
    echo "Stoping Tomcat"
    /bin/sh $TOMCAT_HOME/bin/shutdown.sh

    let kwait=$SHUTDOWN_WAIT
    count=0;
    until [ `ps -p $pid | grep -c $pid` = '0' ] || [ $count -gt $kwait ]
    do
      echo -n -e "\nwaiting for processes to exit";
      sleep 1
      let count=$count+1;
    done

    if [ $count -gt $kwait ]; then
      echo -n -e "\nkilling processes which didn't stop after $SHUTDOWN_WAIT seconds"
      kill -9 $pid
      echo  " \nprocess killed manually"
    fi

    if [ `ps -p $pid | grep -c $pid` = '0' ]; then
      return 0
    else
      return 1
    fi
  else
    echo "Tomcat is not running"
    return 0
  fi
}

restart() {
  stop
  isStop=$?
  if [ "$isStop" = '0' ]; then
    start
  else
    echo "Tomcat stop fail"
  fi
  exit 0
}

if [ $# == 0 ]
then
  echo "need param:stop|start|restart"
else
  case $1 in
  "stop")  
    stop 
    ;;
  "start")
    start 
    ;;
  "restart")
    restart 
    ;;
  *) echo "need param:stop|start|restart";;
  esac
fi
