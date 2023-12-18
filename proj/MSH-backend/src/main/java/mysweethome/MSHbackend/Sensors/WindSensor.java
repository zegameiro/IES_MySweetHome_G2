package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.time.Instant;
import java.time.ZoneOffset;

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
        int weekDay = Instant.now().atZone(ZoneOffset.UTC).getDayOfWeek().getValue();
                            //    dom, seg, ter, qua, qui, sex, sab
        Double[] weekdayMedian = {0.3, 0.4, 0.3, 0.7, 1.5, 1.0, 0.8};

        int currentHour = Instant.now().atZone(ZoneOffset.UTC).getHour();
        Double windMedian = 0.5;
        Double finalValue;

        if (currentHour == 20 || currentHour == 21 || currentHour == 9 || currentHour == 10) {
            windMedian = 1.15;
        }
        else if (currentHour == 22 || currentHour == 8) {
            windMedian = 1.0;
        }
        else if (currentHour == 23 || currentHour == 7) {
            windMedian = 1.10;
        }
        else if (currentHour > 23 || currentHour < 7) {
            windMedian = 1.25;
        } 
        else if (currentHour <= 14 && currentHour >= 12) {
            windMedian = 0.75;
        }

        Random random = new Random();

        // Determine whether to generate a normal or extreme value
        if (random.nextDouble() < EXTREME_PROBABILITY) {
            // 5% chance for an extreme value
            finalValue = random.nextDouble() < 0.5 ? 10 + 4.0 * random.nextDouble()
                    : 20 - 4.0 * random.nextDouble();
        } 
        else {
            // 90% chance for a normal value between 5 and 10
            finalValue = 5.0 + 5.0 * random.nextDouble();
        }

        return Math.round(finalValue * windMedian * weekdayMedian[weekDay] * 100) / 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}