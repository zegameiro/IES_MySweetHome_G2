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
import java.util.List;

@Component
public class Processor {

  private final static ObjectMapper objectMapper = new ObjectMapper();
  String host;
  String queueName;

  @Autowired
  private DataService dataService;

  @Autowired
  private AlertService alertRepository;

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

    String sensorId = data.get("device_id");
    Long timestamp = Long.parseLong(data.get("timestamp"));
    String sensorInformation = data.get("sensor_information");
    String unit = data.get("unit");

    checkForAlerts(sensorId, sensorInformation, unit);

    dataService.saveData(new SensorData(sensorId, timestamp, sensorInformation, unit));

    System.out.println("Data from sensor with ID " + sensorId + " saved sucessfully!");

  }

  public void registerSensor(Map<String, String> data) {

    String device_id = data.get("device_id");
    String device_category = data.get("device_category");
    String device_name = data.get("name");

    String device_location = "None";

    if (data.containsKey("device_location")) {
      device_location = data.get("device_location");
    }

    dataSourceRepository
        .saveDataSource(new DataSource(device_id, Integer.parseInt(device_category), device_location, device_name));

    Alert newAlert = new Alert();
    newAlert.setId(device_id);
    newAlert.setAlert_header("New sensor added!");
    newAlert.setAlert_level(3);
    newAlert.setAlert_information("A new device called '" + device_name + "' has been registered in the application!");
    newAlert.setTimestamp((int) (System.currentTimeMillis() / 1000L));
    alertRepository.saveAlert(newAlert);

    System.out.println("Sensor with ID -> " + device_id + " registered sucessfully!");

  }

  public void checkForAlerts(String sensorId, String sensorInformation, String unit) {

    DataSource sensor = dataSourceRepository.findByID(sensorId);

    if (sensor == null) {
      System.out.println("Sensor with ID -> " + sensorId + " not found while checking for alerts!");
      return;
    }

    if (sensor.getDevice_category() == 4) { // sensor de pessoas
      if (sensorInformation.equals("None")) {
        return;
      } else {
        Alert newAlert = new Alert();
        newAlert.setId(sensorId);
        newAlert.setAlert_header(sensorInformation + " is at the door!");
        newAlert.setAlert_level(1);
        newAlert.setAlert_information(
            "The sensor '" + sensor.getName() + "' has detected " + sensorInformation + " at the door!");
        newAlert.setTimestamp((int) (System.currentTimeMillis() / 1000L));
        alertRepository.saveAlert(newAlert);
        System.out.println("Alert generated for sensor with ID -> " + sensorId + "!");
        return;
      }

    }

    // get the last 3 values

    List<SensorData> last3Values = dataService.listDataBySensor(sensorId, "last_hour"); // last hour is enough for 3
                                                                                        // values

    if (last3Values.size() < 5) {
      return;
    }
    if (last3Values.size() > 5) {
      last3Values = last3Values.subList(0, 5);
    }

    // get the average of the last 3 values

    double average = 0;

    for (SensorData value : last3Values) {
      average += Double.parseDouble(value.getSensor_information());
    }

    average = average / 5;

    // check if the current values is above or below 30% of the average

    double currentValue = Double.parseDouble(sensorInformation);

    if (currentValue > average * 1.3 || currentValue < average * 0.7) {

      Alert newAlert = new Alert();
      newAlert.setId(sensorId);
      newAlert.setAlert_header("Unusual value detected!");
      newAlert.setAlert_level(2);
      newAlert.setAlert_information(
          "The sensor '" + sensor.getName() + "' has detected an unusual value of " + currentValue + " " + unit + "!");
      newAlert.setTimestamp((int) (System.currentTimeMillis() / 1000L));
      alertRepository.saveAlert(newAlert);

      System.out.println("Alert generated for sensor with ID -> " + sensorId + "!");
    }

  }

}
