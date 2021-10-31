package com.dzl.mongodb.util;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 金艺群
 * @description
 * @date 2021/07/28
 **/
public class LookupSimLetPipelinesOperation implements AggregationOperation {
    private String jsonOperation;

    public LookupSimLetPipelinesOperation(String jsonOperation) {
        this.jsonOperation = "{$lookup: " + jsonOperation + "}";
    }

    @Override
    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
        return aggregationOperationContext.getMappedObject(Document.parse(jsonOperation));
    }



}

