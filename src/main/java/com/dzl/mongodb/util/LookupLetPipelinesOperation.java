package com.dzl.mongodb.util;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LookupLetPipelinesOperation implements AggregationOperation {
    private String jsonOperation;
    private String from;
    private String as;
    private List<String> pipelines;
    private String let;

    public LookupLetPipelinesOperation(String jsonOperation) {
        this.jsonOperation = jsonOperation;
    }

    public LookupLetPipelinesOperation() {

    }

    @Override
    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
        return aggregationOperationContext.getMappedObject(Document.parse(jsonOperation));
    }

    public static LookupLetPipelinesOperation LookupLetPipelinesOperation() {
        LookupLetPipelinesOperation v = new LookupLetPipelinesOperation("{$lookup: {\n" +
                "    from: '%s', \n" +
                "    let: {%s}, \n" +
                "    pipeline: [\n" +
                "        %s    \n" +
                "    ], \n" +
                "    as: '%s'}\n" +
                "}\n");
        v.pipelines = new ArrayList<>();
        return v;
    }

    public LookupLetPipelinesOperation from(String from) {
        this.from = from;
        return this;
    }

    public LookupLetPipelinesOperation let(String let) {
        this.let = let;
        return this;
    }

    public LookupLetPipelinesOperation pipelines(List<String> pipelines) {
        this.pipelines = pipelines;
        return this;
    }

    public LookupLetPipelinesOperation as(String as) {
        this.as = as;
        return this;
    }

    public LookupLetPipelinesOperation build() {
        String pipeline = buildPipeline();
        this.jsonOperation = String.format(this.jsonOperation, from, let, pipeline, as);
        return this;
    }

    private String buildPipeline() {
        StringBuilder sb = new StringBuilder();
        this.pipelines.forEach(f -> {
            sb.append("{");
            sb.append(f);
            sb.append("}");
            sb.append(",");
        });
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

}


