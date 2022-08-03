package by.stas.nms.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"by.stas.nms"})
public class ServiceConfig {
    @Bean
    public ClientConfig clientConfig(@Value("${hazelcast.cluster.name:dev}") String clusterName) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        return clientConfig;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
