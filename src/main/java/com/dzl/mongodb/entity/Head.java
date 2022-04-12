package com.dzl.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("head")
public class Head {

    @Id
    private String id;

    private String name;
    private String pinyin;
    private Integer order;

    private String expressions;

}
