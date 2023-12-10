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

        String register_msg = MAPPER
                .writeValueAsString(Map.of("register_msg", "1", "device_category", device_category, "name", name,
                        "device_id", uniqueID));
        broker_queue.basicPublish("", this.queue_name, null, register_msg.getBytes());

        while (true) {
            String message = MAPPER.writeValueAsString(
                    Map.of(
                            "timestamp", String.valueOf(System.currentTimeMillis()), "sensor_information",
                            String.valueOf(getRandomElectricityUsage()), "device_id", uniqueID , "unit", "kWh"));
            broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
            TimeUnit.SECONDS.sleep(10);
        }

    }

    private static double getRandomElectricityUsage() {
        // 5% chance of generating an unusually high value
        if (RANDOM.nextDouble() < 0.05) {
            // Simulate an unusually high electricity usage, e.g., 0.1 kWh to 1 kWh
            return 0.1 + 0.9 * RANDOM.nextDouble();
        } else if (RANDOM.nextDouble() < 0.08) {
            // 10% chance of generating a very high value, e.g., 5 kWh to 10 kWh
            return 5.0 + 5.0 * RANDOM.nextDouble();
        } else {
            // Simulate electricity usage for 10 seconds with varying patterns
    
            // Base electricity usage in the estimated range of 0.001 kWh to 0.02 kWh
            double baseUsage = 0.003 * RANDOM.nextDouble();

            return baseUsage;
        }
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
