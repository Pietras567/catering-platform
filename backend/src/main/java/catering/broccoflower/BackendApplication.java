package catering.broccoflower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"authority", "configuration", "dto", "entities", "utils", "repositories", "exceptions", "dishes", "events"})
@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "entities")
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
