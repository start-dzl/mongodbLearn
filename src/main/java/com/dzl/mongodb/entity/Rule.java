package com.dzl.mongodb.entity;

import java.util.List;
import java.util.Map;

public class Rule {
    private Double part1;
    private Double part2;
    private Double part3;
    private Double part4;

    private Map<String, Integer> part1Map;
    private Map<Integer, Integer> part2Map;
    private Map<String, Integer> part3Map;
    private Map<List<Integer>, Integer> part4Map;

    public Double getPart1() {
        return part1;
    }

    public void setPart1(Double part1) {
        this.part1 = part1;
    }

    public Double getPart2() {
        return part2;
    }

    public void setPart2(Double part2) {
        this.part2 = part2;
    }

    public Double getPart3() {
        return part3;
    }

    public void setPart3(Double part3) {
        this.part3 = part3;
    }

    public Double getPart4() {
        return part4;
    }

    public void setPart4(Double part4) {
        this.part4 = part4;
    }

    public Map<String, Integer> getPart1Map() {
        return part1Map;
    }

    public void setPart1Map(Map<String, Integer> part1Map) {
        this.part1Map = part1Map;
    }

    public Map<Integer, Integer> getPart2Map() {
        return part2Map;
    }

    public void setPart2Map(Map<Integer, Integer> part2Map) {
        this.part2Map = part2Map;
    }

    public Map<String, Integer> getPart3Map() {
        return part3Map;
    }

    public void setPart3Map(Map<String, Integer> part3Map) {
        this.part3Map = part3Map;
    }


    public Map<List<Integer>, Integer> getPart4Map() {
        return part4Map;
    }

    public void setPart4Map(Map<List<Integer>, Integer> part4Map) {
        this.part4Map = part4Map;
    }
}
