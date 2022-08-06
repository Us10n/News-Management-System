package by.stas.nms.config;

import by.stas.nms.cache.CustomCacheManager;
import by.stas.nms.cache.impl.CustomHazelcastCacheManager;
import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.renovator.impl.CommentDtoRenovator;
import by.stas.nms.renovator.impl.NewsWithCommentsDtoRenovator;
import by.stas.nms.repository.CommentRepository;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.ReadConcern;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Profile("test")
@Configuration
public class TestConfig {
    @Bean
    public Config testClientConfig() {
        Config config = new Config();
        config.setClusterName("test");
        return config;
    }

    @Bean
    public HazelcastInstance testHazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
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

    @Bean
    public Renovator<CommentDto> commentDtoRenovator() {
        return new CommentDtoRenovator();
    }

    @Bean
    public Renovator<NewsWithCommentsDto> newsWithCommentsDtoRenovator(CommentRepository commentRepository) {
        return new NewsWithCommentsDtoRenovator(commentRepository);
    }
}

