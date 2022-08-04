package by.stas.nms.config;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.cache.impl.CustomHazelcastCacheManager;
import by.stas.nms.dto.CommentDto;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.renovator.impl.CommentDtoRenovator;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.ReadConcern;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
@ComponentScan("by.stas.nms.repository")
@Profile("test")
public class TestConfig {
    @Bean
    public Config testClientConfig(@Value("${hazelcast.cluster.name:test}") String clusterName) {
        Config config = new Config();
        config.setClusterName(clusterName);
        return config;
    }

    @Bean
    public HazelcastInstance testHazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public Renovator<CommentDto> commentDtoRenovator() {
        return new CommentDtoRenovator();
    }

    @Bean
    public CustomCacheManager customCacheManager(HazelcastInstance hazelcastInstance) {
        return new CustomHazelcastCacheManager(hazelcastInstance);
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        TransactionOptions transactionOptions = TransactionOptions
                .builder()
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.W1)
                .build();
        return new MongoTransactionManager(dbFactory, transactionOptions);
    }
}
