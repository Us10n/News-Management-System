package by.stas.nms.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@Import(PersistenceConfig.class)
@ComponentScan(basePackages = {"by.stas.nms.service", "by.stas.nms.cache",
        "by.stas.nms.logging", "by.stas.nms.renovator",})
public class ServiceConfig {
    @Bean
    @Profile({"dev", "prod"})
    public ClientConfig clientConfig(@Value("${hazelcast.cluster.name:dev}") String clusterName) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        return clientConfig;
    }

    @Bean
    @Profile({"dev", "prod"})
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
