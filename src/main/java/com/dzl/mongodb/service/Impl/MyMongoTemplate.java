package com.dzl.mongodb.service.Impl;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MyMongoTemplate extends MongoTemplate {

    public MyMongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }
}
