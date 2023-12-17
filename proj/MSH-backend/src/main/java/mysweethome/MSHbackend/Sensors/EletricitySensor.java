package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.text.DecimalFormat;

public class EletricitySensor {
    Channel broker_queue = null;
    String queue_name = null, device_category, name;
    String uniqueID = UUID.randomUUID().toString();
    static DecimalFormat df = new DecimalFormat("0.00");

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
                        "device_id", uniqueID, "reading_type", "Eletricity Usage"));
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
        double value = RANDOM.nextDouble();
        if (value < 0.03) {
            // 3% chance to simulate an unusually high electricity usage, e.g., 15 kWh to 25 kWh
            return 15.0 + 10.0 * RANDOM.nextDouble();
        } else if (value < 0.04) {
            // 1% chance of generating a very high value, e.g., 25 kWh to 50 kWh
            return 25.0 + 25.0 * RANDOM.nextDouble();
        } else {
            // Simulate electricity usage for 10 seconds with varying patterns

            // Base electricity usage in the estimated range of 2.5 kWh to 15 kWh
            double baseUsage = 2.5 + 12.5 * RANDOM.nextDouble();

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
