package mysweethome.MSHbackend.DataProcessors;

import mysweethome.MSHbackend.Repositories.*;
import mysweethome.MSHbackend.Models.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Processor {

  private final static ObjectMapper objectMapper = new ObjectMapper();
  String host;
  String queueName;

  @Autowired
  private static DataRepository dataRepository;

  @Autowired
  private static DataSourceRepository dataSourceRepository;

  public Processor(String host, String queueName) {
    this.host = host;
    this.queueName = queueName;
  }

  public void receiveMessage() throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(this.host);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(this.queueName, false, false, false, null);
    System.out.println("Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      processMessage(message);
    };
    channel.basicConsume(this.queueName, true, deliverCallback, consumerTag -> {
    });

  }

  public static void processMessage(String message) {

    Map<String, Object> data = null;
    System.out.println(" Processing message -> '" + message + "'");

    try {
      data = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {
      });
    } catch (Exception e) {
      System.out.println("Error converting message data to JSON");
      return;
    }

    String register_msg = (String) data.get("register_msg");
    if (register_msg != null) {
      registerSensor(data);
      return;
    }

    int sensorId = (int) data.get("data_source_id");
    int timestamp = (int) data.get("timestamp");
    String sensorInformation = (String) data.get("sensor_information");

    dataRepository.save(new SensorData(sensorId, timestamp, sensorInformation));

  }

  public static void registerSensor(Map<String, Object> data) {

    int device_id = (int) data.get("device_id");
    int device_category = (int) data.get("device_category");
    String device_location = (String) data.get("device_location");

    dataSourceRepository.save(new DataSource(device_id, device_category, device_location));

  }

}
