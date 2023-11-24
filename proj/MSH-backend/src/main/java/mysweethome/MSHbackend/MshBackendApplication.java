package mysweethome.MSHbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "mysweethome.MSHbackend")
@EnableMongoRepositories
@EnableAsync

public class MshBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MshBackendApplication.class, args);
    }


}
