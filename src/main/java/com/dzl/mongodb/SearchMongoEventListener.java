package com.dzl.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@Slf4j
public class SearchMongoEventListener extends AbstractMongoEventListener<Object>{

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * onBefore代表前置处理
     * @param event
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event)
    {
        Object source = event.getSource();
        log.info("szsssonBeforeConvert" + source.toString());
    }



}
