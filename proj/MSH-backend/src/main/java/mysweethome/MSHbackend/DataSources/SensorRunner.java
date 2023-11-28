package mysweethome.MSHbackend.DataSources;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import mysweethome.MSHbackend.Sensors.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SensorRunner {

    private final static String QUEUE_NAME = "sensor_queue";
    private static Channel channel = null;

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void run() throws Exception {
        System.out.println("SensorRunner initialized");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (java.net.ConnectException e) {
            System.out.println("Could not connect to rabbitmq-server. Please make sure it is running.");
            System.out.println("sudo service rabbitmq-server start");
        }

        // Use ExecutorService to run the sensors in separate threads
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            try {
                TemperatureSensor temperatureSensor = new TemperatureSensor("Termómetro", channel, QUEUE_NAME, "1");
                temperatureSensor.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executorService.submit(() -> {
            try {
                EletricitySensor eletricitySensor = new EletricitySensor("Contador de Energia",channel, QUEUE_NAME, "2");
                eletricitySensor.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Shutdown the executorService when the tasks are done
        executorService.shutdown();
    }
}

