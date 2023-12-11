package mysweethome.MSHbackend.Sensors;

import com.rabbitmq.client.Channel;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class PeopleSensor {
    Channel broker_queue = null;
    String queue_name = null, device_category, name;
    static ArrayList<String> people = new ArrayList<>(Arrays.asList("John", "Mary", "Peter", "Paul", "George"));
    String uniqueID = UUID.randomUUID().toString();

    private static final ObjectMapper MAPPER = new ObjectMapper();
    static Random RANDOM = new Random();

    public PeopleSensor(String name, Channel queue, String queue_name, String device_category) {
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
                            String.valueOf(getRandomPeople()), "device_id", uniqueID , "unit", "people"));
            broker_queue.basicPublish("", this.queue_name, null, message.getBytes());
            TimeUnit.SECONDS.sleep(10);
        }

    }

    private static String getRandomPeople() {

        if (RANDOM.nextDouble() < 0.10) { // 10% chance of saying someone is at the door
        
            
            // choose one person randomly

            String person = people.get(RANDOM.nextInt(people.size()));

            return person;


        }

        return "None";
        
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
