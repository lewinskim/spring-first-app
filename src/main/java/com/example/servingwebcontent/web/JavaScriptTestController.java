package com.example.servingwebcontent.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JavaScriptTestController {

    @GetMapping("/jstest")
    public String jsTest(){
        return "javaScriptTest";
    }
}
