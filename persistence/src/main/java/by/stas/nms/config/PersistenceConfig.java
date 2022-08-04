package by.stas.nms.config;

import com.mongodb.ReadConcern;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@ComponentScan(basePackages = {"by.stas.nms.repository","by.stas.nms.logging"})
public class PersistenceConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database:test}")
    private String dbName;
    @Value("${spring.data.mongodb.auto-index-creation:true}")
    private Boolean autoIndexCreation;

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        TransactionOptions transactionOptions = TransactionOptions
                .builder()
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.W1)
                .build();
        return new MongoTransactionManager(dbFactory, transactionOptions);
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
