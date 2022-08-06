package by.stas.nms.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@ComponentScan(basePackages = {"by.stas.nms.repository", "by.stas.nms.logging"})
public class PersistenceConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database:test}")
    private String dbName;
    @Value("${spring.data.mongodb.host:localhost}")
    private String dbHost;
    @Value("${spring.data.mongodb.port:27017}")
    private String dbPort;
    @Value("${spring.data.mongodb.auto-index-creation:true}")
    private Boolean autoIndexCreation;

    private static final String mongodbConnectionString = "mongodb://%s:%s/%s";

    @Bean(name = "mongoTransactionManager")
    @Profile({"dev", "prod"})
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        TransactionOptions transactionOptions = TransactionOptions
                .builder()
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.W1)
                .build();
        return new MongoTransactionManager(dbFactory, transactionOptions);
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(String.format(mongodbConnectionString, dbHost, dbPort, dbName));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    protected boolean autoIndexCreation() {
        return autoIndexCreation;
    }
}
