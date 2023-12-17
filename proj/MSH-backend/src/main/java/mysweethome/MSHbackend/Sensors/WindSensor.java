package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class WindSensor {

    Channel broker_queue = null;
    String queue_name = null, device_category, name;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    Random RANDOM = new Random();
    String uniqueID = UUID.randomUUID().toString();

    public WindSensor(String name, Channel queue, String queue_name, String device_category) {
        this.broker_queue = queue;
        this.queue_name = queue_name;
        this.device_category = device_category;
        this.name = name;
    }

    public void run() throws Exception {

        String register_msg = MAPPER
                .writeValueAsString(Map.of("register_msg", "1",
                        "device_category", device_category, "name", name, "device_id", uniqueID, "reading_type",
                        "Wind Strength"));
        broker_queue.basicPublish("", this.queue_name, null, register_msg.getBytes());

        while (true) {

            String message = MAPPER.writeValueAsString(
                    Map.of(
                            "timestamp", String.valueOf(System.currentTimeMillis()), "sensor_information",
                            String.valueOf(getRandomWindStrength()), "device_id",
                            uniqueID, "unit", "m/s"));
            broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
            TimeUnit.SECONDS.sleep(10);
        }
    }

    private static final double MIN_WIND_SPEED = 0.1; // Minimum wind speed in m/s
    private static final double MAX_WIND_SPEED = 20.0; // Maximum wind speed in m/s
    private static final double EXTREME_PROBABILITY = 0.05; // Probability of an extreme value

    // Simulate a realistic wind strength value in the range of 0.1 m/s to 20 m/s
    public double getRandomWindStrength() {

        Random random = new Random();

        // Determine whether to generate a normal or extreme value
        if (random.nextDouble() < EXTREME_PROBABILITY) {
            // 5% chance for an extreme value
            return random.nextDouble() < 0.5 ? MIN_WIND_SPEED + 4.0 * random.nextDouble()
                    : MAX_WIND_SPEED - 4.0 * random.nextDouble();
        } else {
            // 90% chance for a normal value between 5 and 10
            return 5.0 + 5.0 * random.nextDouble();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}