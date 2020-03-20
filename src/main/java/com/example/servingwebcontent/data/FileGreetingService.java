package com.example.servingwebcontent.data;

import com.example.servingwebcontent.Greeting;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileGreetingService implements GreetingService {

    private final Map<Long, Greeting> webHistoryCache = new HashMap<>();
    private final Path fileWithHistory = Path.of("history", "history.csv");

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
        webHistoryCache.computeIfPresent(histId, (key, val) -> new Greeting(histId, name));
    }

    @Override
    @SneakyThrows(IOException.class)
    public void storeHistory() {
        if (Files.notExists(fileWithHistory)) {
            Files.createDirectories(fileWithHistory.getParent());
            Files.createFile(fileWithHistory);
        }
        Files.write(fileWithHistory, parseHistoryToCsvFormat(), StandardOpenOption.TRUNCATE_EXISTING);

    }


    @Override
    public void readHistory() {

    }

    private List<String> parseHistoryToCsvFormat() {
        return getHistorySortedByName()
                .stream()
                .map(Greeting::toCsvformat)
                .collect(Collectors.toList());
    }
}
