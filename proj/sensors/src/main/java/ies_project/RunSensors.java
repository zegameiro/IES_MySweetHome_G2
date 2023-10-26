package ies_project;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class RunSensors {

    private final static String QUEUE_NAME = "sensor_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            TemperatureSensor temperatureSensor = new TemperatureSensor(channel, QUEUE_NAME);
            temperatureSensor.run();
        }
        catch(java.net.ConnectException e){
            System.out.println("Couldnt connect to rabbitmq-server. Please make sure it is running.");
            System.out.println("sudo service rabbitmq-server start");
        }
    }
}
