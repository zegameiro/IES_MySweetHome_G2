#  WIND_SENSOR sensor
import time
import pika
import random

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
channel.queue_declare(queue='sensor_queue')
deviceID = "6767"
higher_bound = 50
lower_bound = 5


# send register message
register_msg = {"register_msg": "1", "device_id": deviceID, "device_category": "WIND_SENSOR", "device_name": "wind_python"}
channel.basic_publish(exchange='', routing_key='', body=register_msg)
print(" [TemperatureSensor] Registered with: '" + register_msg + "'");

while True:
    # send normal messages every 3sec
    randNum = random.randint(lower_bound, higher_bound)
    message = {"device_id": deviceID, "timestamp": time.time() * 1000, "sensor_information": randNum}
    channel.basic_publish(exchange='', routing_key='', body=message)
    
    print(" [TemperatureSensor] Sent: '", message ,"'");

    time.sleep(3)
    
connection.close()
