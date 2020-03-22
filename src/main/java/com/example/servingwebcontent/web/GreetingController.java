package com.example.servingwebcontent.web;

import com.example.servingwebcontent.data.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class GreetingController {

    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }


    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                           Model model) throws IOException {
        greetingService.addToHistory(name);
        greetingService.storeHistory();
        addAttributesToModel(name, model);
        return "greeting";
    }

    @GetMapping("/greeting/{edit}")
    public String editHistory(@PathVariable("edit") String input, Model model) throws IOException {
        String[] posistionAndNameForReplacement = input.split(":");
        greetingService.replaceElementInHistory(Long.parseLong(posistionAndNameForReplacement[0]), posistionAndNameForReplacement[1]);
        greetingService.storeHistory();
        addAttributesToModel(posistionAndNameForReplacement[1], model);
        return "greeting";
    }

    private void addAttributesToModel(String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("history", greetingService.getHistorySortedByName());
    }

}