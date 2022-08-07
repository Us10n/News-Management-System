package by.stas.nms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class {@code NewsManagementApplication} contains method to run Spring Boot application.
 * @see SpringBootApplication
 */
@SpringBootApplication
public class NewsManagementApplication {
    /**
     * The entry point of Spring Boot application.
     */
    public static void main(String[] args) {
        SpringApplication.run(NewsManagementApplication.class, args);
    }
}
