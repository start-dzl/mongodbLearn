package com.dzl.mongodb.controller;

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

}
