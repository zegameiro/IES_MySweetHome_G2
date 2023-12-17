#!/bin/bash
device_id=$(uuidgen)

rabbitmqadmin publish routing_key="sensor_queue" payload="{\"register_msg\":\"1\", \"device_id\":\"$device_id\", \"device_category\":\"1\", \"device_name\":\"presence_bash\"}"

while : 
do
  time=$(date +%s)
  rabbitmqadmin publish routing_key="sensor_queue" payload="{\"device_id\":\"$device_id\", \"timestamp\":\"$time\", \"sensor_information\":\"$(($RANDOM%(2)))\", \"unit\":\"people\"}"
  sleep 10
done
