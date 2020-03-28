package com.example.servingwebcontent.web;

import com.example.servingwebcontent.GreetingsForRemovalCache;
import com.example.servingwebcontent.data.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/greeting")
public class GreetingController {

    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }


    @GetMapping
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "defaultValue") String name,
                           Model model) throws IOException {
        log.info("");
        greetingService.addToHistory(name);
        greetingService.storeHistory();
        addAttributesToModel(name, model);
        model.addAttribute("greetingsForRemoval", new GreetingsForRemovalCache());
        return "greeting";
    }

    @GetMapping("/{edit}")
    public String editHistory(@PathVariable("edit") String input, Model model) throws IOException {
        String[] posistionAndNameForReplacement = input.split(":");
        greetingService.replaceElementInHistory(Long.parseLong(posistionAndNameForReplacement[0]), posistionAndNameForReplacement[1]);
        greetingService.storeHistory();
        addAttributesToModel(posistionAndNameForReplacement[1], model);
        return "greeting";
    }

    @PostMapping
    public String removeSelectedElements(GreetingsForRemovalCache cache){
        //TO DO ZASTANOWIC SIE NAD METODA ADDATTRTO MODEL CZY ZA KAZDYM RAZEM MUSZE DODAWAC HISTORY
        greetingService.removeFromHistory(cache.getGreetings());
        log.info("list contains" +cache.toString());
        log.info("list size" +cache.getGreetings().size());
        return "redirect:/";
    }

    private void addAttributesToModel(String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("history", greetingService.getHistorySortedByName());
    }

}