package mysweethome.sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class EletricitySensor {
    Channel broker_queue = null;
    String queue_name = null, device_location, device_category;
    int device_id;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static Random RANDOM = new Random();

    public EletricitySensor(Channel queue, String queue_name, int device_id, String device_category,
            String device_location) {
        this.broker_queue = queue;
        this.queue_name = queue_name;
        this.device_id = device_id;
        this.device_category = device_category;
        this.device_location = device_location;
    }

    public void run() throws Exception {

        // send register message
        String register_msg = MAPPER
                .writeValueAsString(Map.of("register_msg", "1", "device_id", String.valueOf(device_id),
                        "device_category", device_category, "device_location", device_location));
        broker_queue.basicPublish("", this.queue_name, null, register_msg.getBytes());
        // System.out.println(" [EletricitySensor] ");

        // send normal data every 10 sec

        while (true) {
            try {
                String message = MAPPER.writeValueAsString(
                        Map.of("data_source_id", String.valueOf(device_id),
                                "timestamp", String.valueOf(System.currentTimeMillis()),
                                "sensor_information", String.valueOf(getRandomElectricityUsage())));
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
}
