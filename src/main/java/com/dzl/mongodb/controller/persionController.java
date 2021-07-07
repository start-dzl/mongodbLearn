package com.dzl.mongodb.controller;

import com.dzl.mongodb.service.PersonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@Validated
@RestController
public class persionController {

    private static final Log log = LogFactory.getLog(persionController.class);

    @Autowired
    private PersonService personService;

    @GetMapping("test")
    public String list(String name) {
        personService.testTransactional(name);
        return "ok";
    }


}
