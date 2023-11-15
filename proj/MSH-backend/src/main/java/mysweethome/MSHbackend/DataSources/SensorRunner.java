package mysweethome.MSHbackend.DataSources;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ExecutorService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import mysweethome.sensors.TemperatureSensor;

@Component
public class SensorRunner {

    private final static String QUEUE_NAME = "sensor_queue";
    private static Channel channel = null;

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void run() throws Exception{
        System.out.println("SensorRunner initialized");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try{ 
            Connection connection = factory.newConnection();
            channel = connection.createChannel(); 
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (java.net.ConnectException e) {
            System.out.println("Couldnt connect to rabbitmq-server. Please make sure it is running.");
            System.out.println("sudo service rabbitmq-server start");
        }

        TemperatureSensor temperatureSensor = new TemperatureSensor(channel, QUEUE_NAME, 12345, "1", "Kitchen");
        temperatureSensor.run();
    }

}
