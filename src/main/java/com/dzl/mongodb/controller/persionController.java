package com.dzl.mongodb.controller;

import com.dzl.mongodb.entity.ExpressEnum;
import com.dzl.mongodb.entity.Head;
import com.dzl.mongodb.entity.Rule;
import com.dzl.mongodb.service.Impl.PService;
import com.dzl.mongodb.service.PersonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@RestController("/persion")
@CrossOrigin
public class persionController {

    private static final Log log = LogFactory.getLog(persionController.class);

    @Autowired
    private PService pService;

    @Autowired
    private PersonService personService;

    @GetMapping("/test")
    public String list(String name) {
        personService.testTransactional(name);
        return "ok";
    }

    @GetMapping("/excel")
    public Map<String, Object> excel(@RequestParam(required = false) String fildName,
                                     @RequestParam(required = false) String fildvalue) {
        return personService.excelShow(fildName, fildvalue);
    }

    @GetMapping("/excel/output")
    public void excelOutput(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        personService.excelOutput(response);
    }

    @PostMapping("/excel")
    public void excelPost(MultipartFile file) throws IOException {
         personService.createHead(file);
    }

    @PostMapping("/excel/update")
    public void updateValues(MultipartFile file) throws IOException {
        personService.updateValues(file);
    }


    @GetMapping("/expressEnum")
    public List<Map<String, Object>>  excel() {
        ExpressEnum[] values = ExpressEnum.values();
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (ExpressEnum value : values) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("name",value.name());
            hashMap.put("express",value.getExpress());
            arrayList.add(hashMap);
        }

        return arrayList;
    }


    @PostMapping("/head")
    public Head head(@RequestBody Head head) {
        return  personService.saveAndUpdate(head);
    }

    @GetMapping("/excel/head")
    public List<Head> excelShowHead() {
        return personService.excelShowHead();
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
