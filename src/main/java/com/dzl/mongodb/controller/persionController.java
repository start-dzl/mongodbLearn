package com.dzl.mongodb.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.dzl.mongodb.entity.Rule;
import com.dzl.mongodb.service.PersonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.util.Map;


@Validated
@RestController("/persion")
public class persionController {

    private static final Log log = LogFactory.getLog(persionController.class);

    @Autowired
    private PersonService personService;

    @GetMapping("/test")
    public String list(String name) {
        personService.testTransactional(name);
        return "ok";
    }

    @PostMapping("/rule")
    public String rule(@RequestBody Rule rule) {
        Yaml yaml = new Yaml(new Constructor(Rule.class));
        Double aDouble = rule.getPart1();
        System.out.printf(aDouble.toString());
        Map<Integer, Integer> part1Map = rule.getPart2Map();
        Integer integer = part1Map.get(2);
        System.out.printf(integer.toString());
        System.out.printf(rule.toString());
        String dump = yaml.dump(rule);
        System.out.printf(dump);
        return dump;
    }

}
