#!/bin/bash

index=0
keyword=*lengfeng*

function keysDel()
{
  cat hostlist | while read line
  do
    host=`echo "$line"|awk '{print $1}'`
    port=`echo "$line"|awk '{print $2}'`
    username=`echo "$line"|awk '{print $3}'`
    passwd=`echo "$line"|awk '{print $4}'`

    ((index++));

    echo $index $host $port $username $passwd

    redis-cli -c -h $host -p $port keys "$keyword"
    redis-cli -c -h $host -p $port keys "$keyword" | xargs -t -n1 redis-cli -c -h $host -p $port del
    redis-cli -c -h $host -p $port keys "$keyword"

  done

}

keysDel
