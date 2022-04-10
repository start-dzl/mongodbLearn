package com.dzl.mongodb.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class NoModleDataListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */

    public Map<Integer, String> head  = new HashMap<>();

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        head = headMap;
        System.out.println("读取到数据headMap:{}"+ JSON.toJSONString(headMap));
    }



    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        //super.invoke(data, context);
//        if (list.size() >= BATCH_COUNT) {
//            saveData();
//            list.clear();
//        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {

    }

}
