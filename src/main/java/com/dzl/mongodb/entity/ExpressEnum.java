package com.dzl.mongodb.entity;

public enum ExpressEnum {

    ADD("+"),

    SUB("-"),

    MULT("*"),

    DIVIDE("/"),

    REMAINDER("%"),

    LEFT_BRACKET("("),

    RIGHT_BRACKET(")"),

    QUESTION("?"),

    GT("<"),

    LT(">"),

    COLON(":"),

    eq("=");

    private String express;

    ExpressEnum(String express){
     this.express = express;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }
}
