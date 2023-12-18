package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.time.Instant;
import java.time.ZoneOffset;

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
                        "device_category", device_category, "name", name, "device_id", uniqueID,  "reading_type", "House Temperature"));
        broker_queue.basicPublish("", this.queue_name, null, register_msg.getBytes());

        while (true) {

            String message = MAPPER.writeValueAsString(
                    Map.of(
                            "timestamp", String.valueOf(System.currentTimeMillis()), "sensor_information",
                            String.valueOf(Math.round(getRandomTemp()*100)/100), "device_id",
                            uniqueID, "unit" , "ºC"));
            broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
            TimeUnit.SECONDS.sleep(10);
        }
    }


    // Simulate a more realistic temperature with slight randomness and occasional extreme values
    public double getRandomTemp() {
        int weekDay = Instant.now().atZone(ZoneOffset.UTC).getDayOfWeek().getValue();
                            //    dom, seg, ter, qua, qui, sex, sab
        Double[] weekdayMedian = {1.8, 1.3, 1.0, 0.7, 0.8, 1.0, 1.5};

        int currentHour = Instant.now().atZone(ZoneOffset.UTC).getHour();
                            //  00h,  01h,  02h,  03h,  04h,  05h,  06h,  07h,  08h,  09h,  10h,  11h,  12h,  13h,  14h,  15h,  16h,  17h,  18h,  19h,  20h,  21h,  22h,  23h
        Double[] hourMedian = {17.8, 17.3, 17.0, 16.3, 16.0, 16.2, 16.5, 18.0, 20.0, 23.0, 24.0, 25.0, 25.5, 25.8, 25.8, 25.5, 25.0, 24.0, 23.0, 22.0, 21.0, 20.0, 19.0, 18.3};
        Double currMedian = hourMedian[currentHour];

        Double temperatureChange = (RANDOM.nextDouble() * 2  - 1) * weekdayMedian[weekDay];  // Random change between -1 and 1

        // Add the random change to the current temperature
        currMedian += temperatureChange;

        // Occasionally introduce extreme values for alerts (e.g., 15% chance)
        if (RANDOM.nextDouble() < 0.15) {
            currMedian += RANDOM.nextBoolean() ? 10 : -10;  // Either +10 or -10
        }

        return Math.round(currMedian * 100) / 100;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
