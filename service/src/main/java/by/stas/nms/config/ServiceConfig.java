package by.stas.nms.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@Import(PersistenceConfig.class)
@ComponentScan(basePackages = {"by.stas.nms.service", "by.stas.nms.cache",
        "by.stas.nms.logging", "by.stas.nms.renovator",})
public class ServiceConfig {

    private static final String hazelcastServerAddress = "%s:%s";

    @Bean
    @Profile({"dev", "prod"})
    public ClientConfig clientConfig(@Value("${hazelcast.cluster.name:test}") String clusterName,
                                     @Value("${hazelcast.server.host:localhost}") String host,
                                     @Value("${hazelcast.server.port:5701}") String port) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(String.format(hazelcastServerAddress, host, port));
        return clientConfig;
    }

    @Bean
    @Profile({"dev", "prod"})
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
