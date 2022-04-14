package com.dzl.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class HeadExpress {

    private String id;
    private String name;
    private String pinyin;
    private ExpressEnum expressEnum;
    private Integer order;



}
