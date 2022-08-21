package com.dzl.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document("head")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "head")
public class Head {

    @Id
    private String id;

    private String name;
    private String pinyin;
    private Integer order;

    private String showExpressions;

    private String expressions;

    private List<HeadExpress> expresses;

    private Date time;

}
