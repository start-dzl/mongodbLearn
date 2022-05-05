package com.dzl.mongodb.consumer;

import com.dzl.mongodb.entity.OaOrganization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.util.List;

@Profile("!prod")
@Component
@Slf4j
@KafkaListener(topics = {"org-topic"})
public class Consumer {

    @KafkaHandler
    public void receive(String message){
        //OaOrganization organization = JSON.parseObject(message, OaOrganization.class);
        log.info("我是消费者1，我接收到的消息是："+message.toString());
    }
}