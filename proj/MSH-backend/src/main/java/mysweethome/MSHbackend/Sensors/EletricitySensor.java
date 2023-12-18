package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneOffset;

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
                            String.valueOf(getRandomElectricityUsage()), "device_id", uniqueID, "unit", "kWh"));
            broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
            TimeUnit.SECONDS.sleep(10);
        }

    }

    private static double getRandomElectricityUsage() {
        Double hourMedian = 1.0;
        Double finalValue;

        int currentHour = Instant.now().atZone(ZoneOffset.UTC).getHour();
        
        //  Most of the people in the house are asleep, so they use less electricity (except programmers) (75% less average power)
        
        if (currentHour == 20 || currentHour == 21 || currentHour == 9 || currentHour == 10) {
            hourMedian = 1.15;
        }
        else if (currentHour == 22 || currentHour == 8) {
            hourMedian = 0.5;
        }
        else if (currentHour == 23 || currentHour == 7) {
            hourMedian = 0.4;
        }
        else if (currentHour > 23 || currentHour < 7) {
            hourMedian = 0.25;
        } 
        else if (currentHour <= 14 && currentHour >= 12) {
            hourMedian = 0.75;
        }

        // 5% chance of generating an unusually high value
        double value = RANDOM.nextDouble();
        if (value < 0.03) {
            // 3% chance to simulate an unusually high electricity usage, e.g., 15 kWh to 25
            // kWh
            finalValue = 15.0 + 10.0 * RANDOM.nextDouble();
        } 
        else if (value < 0.04) {
            // 1% chance of generating a very high value, e.g., 25 kWh to 50 kWh
            finalValue = 25.0 + 25.0 * RANDOM.nextDouble();
        } 
        else {
            // Simulate electricity usage for 10 seconds with varying patterns

            // Base electricity usage in the estimated range of 2.5 kWh to 15 kWh
            double baseUsage = 2.5 + 12.5 * RANDOM.nextDouble();

            finalValue = baseUsage;
        }

        return Math.round(finalValue * hourMedian * 100) / 100;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
