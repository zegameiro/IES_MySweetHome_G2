package mysweethome.MSHbackend.DataProcessors;

import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Services.*;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Component
public class Processor {

  private final static ObjectMapper objectMapper = new ObjectMapper();
  String host;
  String queueName;

  @Autowired
  private DataService dataRepository;

  @Autowired
  private DataSourceService dataSourceRepository;

  public Processor(@Qualifier("host") String host, @Qualifier("sensorQueue") String queueName) {
    this.host = host;
    this.queueName = queueName;
    System.out.println("Processor initialized");
  }

  @EventListener(ApplicationReadyEvent.class)
  @Async
  public void receiveMessage() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(this.host);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(this.queueName, false, false, false, null);
    // System.out.println("Processor is waiting for messages uwu ");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      // System.out.println(" [Processor] Received '" + message + "'");
      processMessage(message);
    };
    channel.basicConsume(this.queueName, true, deliverCallback, consumerTag -> {
    });

  }

  public void processMessage(String message) {

    Map<String, String> data = null;

    try {
      data = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {
      });
    } catch (Exception e) {
      System.out.println("Error converting message data to JSON");
      return;
    }

    String register_msg = (String) data.get("register_msg");
    if (register_msg != null) {
      if (register_msg.equals("1")) {
        registerSensor(data);
        return;
      }
    }

    String sensorId =  data.get("device_id");
    Long timestamp = Long.parseLong( data.get("timestamp"));
    String sensorInformation = data.get("sensor_information");

    dataRepository.saveData(new SensorData(sensorId, timestamp, sensorInformation));

     System.out.println("Data from sensor with ID " + sensorId + " saved sucessfully!");

  }

  public void registerSensor(Map<String, String> data) {

    String device_id =  data.get("device_id");
    String device_category =  data.get("device_category");
    String device_name =  data.get("name");

    String device_location = "None";

    if (data.containsKey("device_location")) {
      device_location = data.get("device_location");
    }

    dataSourceRepository
        .saveDataSource(new DataSource(device_id, Integer.parseInt(device_category), device_location , device_name));
    System.out.println("Sensor with ID -> " + device_id + " registered sucessfully!");

  }

}
