package com.dzl.mongodb.config;

import com.dzl.mongodb.service.Impl.MyMongoTemplate;
import com.dzl.mongodb.service.Impl.PService;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.util.EnforceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class Config {

    @Autowired
    private MongoDatabaseFactory mongoDatabaseFactory;

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    PService pService() {
        return new PService();
    }


    @Bean
    @Qualifier("myMongoTemplate")
    public MongoTemplate MyMongoTemplate() throws Exception {
        MyMongoTemplate my = new MyMongoTemplate(mongoDatabaseFactory);
        return my;
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate my = new MongoTemplate(mongoDatabaseFactory);
        return my;
    }

    @Bean
    public Enforcer enforcer() throws Exception {
        EnforceContext enforceContext = new EnforceContext("2");
        Resource model = new ClassPathResource("policy/model.conf");
        Resource basicPolicy = new ClassPathResource("policy/basic_policy.csv");

        Enforcer e = new Enforcer(model.getFile().getPath(), basicPolicy.getFile().getPath());
        e.loadPolicy();//Load the policy from DB
        return e;

    }

    // ...
}
