package mysweethome.MSHbackend;

import org.springframework.boot.SpringApplication;
import mysweethome.MSHbackend.DataProcessors.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories
public class MshBackendApplication {

	@Autowired
	private Processor processor = new Processor("localhost", "sensor_queue");

	public static void main(String[] args) {
		SpringApplication.run(MshBackendApplication.class, args);

	}

	@PostConstruct
	public void init() {
		try {
			processor.receiveMessage();
		} catch (Exception e) {
			System.out.println("Error in receiving message");
		}

	}

}
