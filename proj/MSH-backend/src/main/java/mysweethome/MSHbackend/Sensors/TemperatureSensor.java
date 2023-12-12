package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class TemperatureSensor {

    Channel broker_queue = null;
    String queue_name = null, device_category, name;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    Random RANDOM = new Random();
    String uniqueID = UUID.randomUUID().toString();

    public TemperatureSensor(String name, Channel queue, String queue_name, String device_category) {
        this.broker_queue = queue;
        this.queue_name = queue_name;
        this.device_category = device_category;
        this.name = name;
    }

    public void run() throws Exception {

        
        String register_msg = MAPPER
                .writeValueAsString(Map.of("register_msg", "1",
                        "device_category", device_category, "name", name, "device_id", uniqueID));
        broker_queue.basicPublish("", this.queue_name, null, register_msg.getBytes());

        while (true) {

            String message = MAPPER.writeValueAsString(
                    Map.of(
                            "timestamp", String.valueOf(System.currentTimeMillis()), "sensor_information",
                            String.valueOf(getRandomTemp()), "device_id",
                            uniqueID, "unit" , "ºC"));
            broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
            TimeUnit.SECONDS.sleep(10);
        }
    }


    // Simulate a more realistic temperature with slight randomness and occasional extreme values
    public int getRandomTemp() {
        int currentTemperature = 23;  // Starting temperature
        int temperatureChange = RANDOM.nextInt(4) - 2;  // Random change between -2 and 2

        // Add the random change to the current temperature
        currentTemperature += temperatureChange;

        // Occasionally introduce extreme values for alerts (e.g., 15% chance)
        if (RANDOM.nextDouble() < 0.25) {
            currentTemperature += RANDOM.nextBoolean() ? 10 : -10;  // Either +10 or -10
        }

        // Ensure the temperature stays within a realistic range (e.g., 18 to 28 degrees Celsius)
        currentTemperature = Math.max(18, Math.min(28, currentTemperature));

        return currentTemperature;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
