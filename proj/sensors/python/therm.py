#  WIND_SENSOR sensor
import time
import pika
import random
<<<<<<< HEAD
import json
import uuid
=======
>>>>>>> 0bba46014bd84657e9e6ccdf2430deb37577fe73

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
channel.queue_declare(queue='sensor_queue')
<<<<<<< HEAD
deviceID = uuid.uuid1()
=======
deviceID = "6767"
>>>>>>> 0bba46014bd84657e9e6ccdf2430deb37577fe73
higher_bound = 50
lower_bound = 5


# send register message
<<<<<<< HEAD
register_msg = json.dumps({"register_msg": "1", "device_id": str(deviceID), "device_category": "4", "device_name": "wind_python"})
channel.basic_publish(exchange="", routing_key='sensor_queue', body=register_msg)
=======
register_msg = {"register_msg": "1", "device_id": deviceID, "device_category": "WIND_SENSOR", "device_name": "wind_python"}
channel.basic_publish(exchange='', routing_key='', body=register_msg)
>>>>>>> 0bba46014bd84657e9e6ccdf2430deb37577fe73
print(" [TemperatureSensor] Registered with: '" + register_msg + "'");

while True:
    # send normal messages every 3sec
    randNum = random.randint(lower_bound, higher_bound)
<<<<<<< HEAD
    message = json.dumps({"device_id": str(deviceID), "timestamp": (int) (time.time() * 1000), "sensor_information": randNum})
    channel.basic_publish(exchange='', routing_key='sensor_queue', body=message)
    
    print(" [TemperatureSensor] Sent: '", message ,"'");

    time.sleep(10)
=======
    message = {"device_id": deviceID, "timestamp": time.time() * 1000, "sensor_information": randNum}
    channel.basic_publish(exchange='', routing_key='', body=message)
    
    print(" [TemperatureSensor] Sent: '", message ,"'");

    time.sleep(3)
>>>>>>> 0bba46014bd84657e9e6ccdf2430deb37577fe73
    
connection.close()
