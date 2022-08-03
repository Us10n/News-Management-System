package by.stas.nms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class NewsManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsManagementApplication.class, args);
    }
}
