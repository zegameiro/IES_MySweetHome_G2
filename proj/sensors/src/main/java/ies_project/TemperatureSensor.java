package ies_project;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class TemperatureSensor {

    Channel broker_queue = null;
    String queue_name = null;
    private static final String[] ROOMS = {"Dining Room", "Kitchen", "Bedroom", "Bathroom"};
    private static final Random RANDOM = new Random();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public TemperatureSensor(Channel queue, String queue_name) {
        this.broker_queue = queue;
        this.queue_name = queue_name;
    }

    void run() {
        while (true) {
            String room = ROOMS[RANDOM.nextInt(ROOMS.length)];
            int temperature = 20 + RANDOM.nextInt(11);

            try {
                String message = MAPPER.writeValueAsString(Map.of("Room", room, "Temperature", temperature));
                broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

