package com.example.servingwebcontent.web;

import com.example.servingwebcontent.Greeting;
import com.example.servingwebcontent.data.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@Slf4j
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();
    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
        this.counter.set(greetingService.getHistorySize());
    }


    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                           @RequestParam(value = "history", required = false) Long historyPosition,
                           Model model) throws IOException {
        if (historyPosition == null) {
            Greeting temporaryGreeting = new Greeting(counter.getAndIncrement(), name);
            greetingService.addToHistory(temporaryGreeting);
            log.info("value added, current history size is " + greetingService.getHistorySortedByName().size());
        } else {
            greetingService.replaceElementInHistory(historyPosition, name);
        }
        greetingService.storeHistory();
        addAttributesToModel(name, model);
        return "greeting";
    }

    private void addAttributesToModel(String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("history", greetingService.getHistorySortedByName());
    }

}