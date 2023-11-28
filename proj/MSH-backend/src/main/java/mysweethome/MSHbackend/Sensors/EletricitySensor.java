package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class EletricitySensor {
    Channel broker_queue = null;
    String queue_name = null, device_category, name;
    String uniqueID = UUID.randomUUID().toString();


    private static final ObjectMapper MAPPER = new ObjectMapper();
    static Random RANDOM = new Random();

    public EletricitySensor(String name, Channel queue, String queue_name, String device_category) {
        this.broker_queue = queue;
        this.queue_name = queue_name;
        this.device_category = device_category;
        this.name = name;
    }

    public void run() throws Exception {

        // send register message
        String register_msg = MAPPER
                .writeValueAsString(Map.of("register_msg", "1", "device_category", device_category, "name", name , "device_id", uniqueID));
        broker_queue.basicPublish("", this.queue_name, null, register_msg.getBytes());

        while (true) {
            try {
                String message = MAPPER.writeValueAsString(
                        Map.of(
                                "timestamp", String.valueOf(System.currentTimeMillis()), "sensor_information",
                                String.valueOf(getRandomElectricityUsage()), "device_id", uniqueID));
                broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
                // System.out.println(" [EletricitySensor] Sent '" + message + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static double getRandomElectricityUsage() {
        // 5% chance of generating an unusually high value
        if (RANDOM.nextDouble() < 0.05) {
            // Simulate an unusually high electricity usage, e.g., 0.1 kWh to 1 kWh
            return 0.1 + 0.9 * RANDOM.nextDouble();
        } else {
            // Simulate electricity usage for 10 seconds in the estimated range of 0.001 kWh
            // to 0.02 kWh
            return 0.001 + (0.02 - 0.001) * RANDOM.nextDouble();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
