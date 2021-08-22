package com.dzl.mongodb.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.dzl.mongodb.mapper.MbMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mb")
public class mbController {

    @Autowired
    private MbMapper mbMapper;

    @GetMapping("/mb")
    public Map list(Long id) {
        return mbMapper.getById(id);
    }

    @GetMapping("/mb/all")
    public Map all() {
        List aLl = mbMapper.getAll("file");
        return (Map) aLl.get(0);
    }

    @GetMapping("/mb/list")
    public Map list() {
        List aLl = mbMapper.getBySql("select * from file");
        return (Map) aLl.get(0);
    }

    @GetMapping("/mb/excel")
    public String excel(String tableName) {
        String fileName = "E:\\ex.xlsx";

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        ExcelReaderBuilder builder = EasyExcel.read(fileName);
        List<Map<Integer, String>> listMap = builder.sheet().doReadSync();
        Map<Integer, String> map = listMap.get(0);
        listMap.remove(map);


        if (mbMapper.existTable(tableName) < 1) {
            String sql = "create table replaceTableName\n (" +
                    "replaceSql" +
                    ");";
            StringBuilder stringBuilder = new StringBuilder();
            int size = map.size();
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    stringBuilder.append(map.get(i) + "\t" + "varchar(255) \n");
                    stringBuilder.append("primary key,\n");
                } else if (i == (size - 1)) {
                    stringBuilder.append(map.get(i) + "\t" + "varchar(255) \n");
                } else {
                    stringBuilder.append(map.get(i) + "\t" + "varchar(255), \n");
                }
            }
            String replaceSql = stringBuilder.toString();
            sql = sql.replace("replaceSql", replaceSql);
            sql = sql.replace("replaceTableName", tableName);
            System.out.printf(sql);
            mbMapper.createNewTable(sql);
        }

        String insertSql = "INSERT INTO " + tableName + " (replaceColumn) VALUES replaceValues ;";
        StringBuilder replaceColumnBuild = new StringBuilder();
        int replaceColumnSize = map.size();
        for (int i = 0; i < replaceColumnSize; i++) {
            if (i == (replaceColumnSize - 1)) {
                replaceColumnBuild.append(map.get(i));
            } else {
                replaceColumnBuild.append(map.get(i)).append(",");
            }
        }
        insertSql = insertSql.replace("replaceColumn", replaceColumnBuild.toString());

        StringBuilder replaceColumn = new StringBuilder();
        int size1 = listMap.size();
        for (int j = 0; j < size1; j++) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            Map<Integer, String> data = listMap.get(j);
            int size = data.size();
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    replaceColumn.append("(\t" + "'" + data.get(i) + "'" + ",");
                } else if (i == (size - 1)) {
                    if (j != (size1 - 1)) {
                        replaceColumn.append("'" + data.get(i) + "'" + "\t),\n");
                    } else {
                        replaceColumn.append("'" + data.get(i) + "'" + "\t)\n");
                    }

                } else {
                    replaceColumn.append("'" + data.get(i) + "'" + ",");
                }
            }
        }

        insertSql = insertSql.replace("replaceValues", replaceColumn.toString());
        System.out.printf(insertSql);
        mbMapper.createNewTable(insertSql);


        return insertSql;
    }
}
