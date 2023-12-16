require "amqp-client"
require "uuid"
require "random"
require "time"

AMQP::Client.start("amqp://guest:guest@localhost") do |c|
	uuid = UUID.random
	ch = c.channel
	e = ch.exchange("", type: "")

	msg = "{\"register_msg\": \"1\", \"device_id\": \"#{uuid}\", \"device_category\":\"1\", \"device_name\":\"crystal_temp\"}"
	e.publish msg, routing_key: "sensor_queue"
	puts "Register message: #{msg}"
	
 	msg = ""

  	while true 
		epoch_time_integer = Time.utc.to_unix.to_i
  		msg = "{\"device_id\": \"#{uuid}\", \"timestamp\": \"#{epoch_time_integer}\", \"sensor_information\":\"#{rand(20)}\", \"unit\":\"ºC\"}"
  		e.publish msg, routing_key: "sensor_queue"
  	
		puts "Sensor message sent: #{msg}"

		sleep(10)
  	end
end
