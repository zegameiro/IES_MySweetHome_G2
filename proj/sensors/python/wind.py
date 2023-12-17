#  WIND_SENSOR sensor
import time
import pika
import random
import json
import uuid

connection = pika.BlockingConnection(pika.ConnectionParameters("localhost"))
channel = connection.channel()
channel.queue_declare(queue="sensor_queue")
deviceID = uuid.uuid1()
higher_bound = 50
lower_bound = 5


# send register message
register_msg = json.dumps({"register_msg": "1", "device_id": str(deviceID), "device_category": "3", "device_name": "wind_python", "reading_type":"Wind Outside"})
channel.basic_publish(exchange="", routing_key="sensor_queue", body=register_msg)
print(" [TemperatureSensor] Registered with: '" + register_msg + "'");

while True:
    # send normal messages every 3sec
    randNum = random.randint(lower_bound, higher_bound)
    message = json.dumps({"device_id": str(deviceID), "timestamp": (int) (time.time() * 1000), "sensor_information": randNum, "unit": "ºC"})
    channel.basic_publish(exchange="", routing_key="sensor_queue", body=message)
    
    print(" [TemperatureSensor] Sent: '", message ,"'");

    time.sleep(10)

connection.close()
