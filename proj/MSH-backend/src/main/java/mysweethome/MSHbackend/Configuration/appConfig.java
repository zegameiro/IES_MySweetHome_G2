package mysweethome.MSHbackend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class appConfig {

    @Bean
    public String sensorQueue() {
        return "sensor_queue";
    }

    @Bean
    public String host() {
        return "rabbitmq";
    }
}

