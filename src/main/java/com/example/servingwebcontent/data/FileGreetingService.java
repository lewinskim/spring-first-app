package com.example.servingwebcontent.data;

import com.example.servingwebcontent.Greeting;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileGreetingService implements GreetingService {

    private final Map<Long, Greeting> webHistoryCache = new HashMap<>();

    @Override
    public void addToHistory(Greeting greeting) {
        webHistoryCache.put(greeting.getId(), greeting);
    }

    @Override
    public List<Greeting> getHistorySortedByName() {
        return webHistoryCache.values()
                .stream()
                .sorted(Comparator.comparing(greeting -> greeting.getName().toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void replaceElementInHistory(Long histId, String name) {
            webHistoryCache.computeIfPresent(histId,(key,val) -> new Greeting(histId, name));
    }
}
