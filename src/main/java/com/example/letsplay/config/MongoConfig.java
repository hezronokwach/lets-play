package com.example.letsplay.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "lets-play";
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        String uri = "mongodb+srv://hezronokwach_db_user:JtpXWgQlSuX9TwJX@lets-play.j9mvfxn.mongodb.net/lets-play?retryWrites=true&w=majority";
        return MongoClients.create(uri);
    }
}