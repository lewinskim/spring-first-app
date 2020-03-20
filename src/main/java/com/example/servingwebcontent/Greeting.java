package com.example.servingwebcontent;

import lombok.Getter;

@Getter
public class Greeting {

    private long id;
    private String name;

    public Greeting(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toCsvformat(){
        return String.format("%d,%s",id,name);
    }
}
