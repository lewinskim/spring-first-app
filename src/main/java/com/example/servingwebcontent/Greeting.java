package com.example.servingwebcontent;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Greeting {

    private long id;
    @Setter
    private String name;

    public Greeting(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
