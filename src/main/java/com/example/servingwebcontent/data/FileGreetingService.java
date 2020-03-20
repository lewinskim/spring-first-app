package com.example.servingwebcontent.data;

import com.example.servingwebcontent.Greeting;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileGreetingService implements GreetingService {

    private final Map<Long, Greeting> webHistoryCache = new HashMap<>();
    private final Path fileWithHistory = Path.of("history", "history.csv");
    private Long lastVersionFromHistoryFile;

    private FileGreetingService() {
        initializeFileGreetingService();
    }

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
    @SneakyThrows(IOException.class)
    public List<Greeting> readHistory() {
        List<String> inputFile = Files.readAllLines(fileWithHistory);
        return inputFile.stream()
                .map(s -> s.split(","))
                .map(strings -> new Greeting(Long.parseLong(strings[0]), strings[1]))
                .collect(Collectors.toList());
    }

    @Override
    public Long getHistorySize() {
        return lastVersionFromHistoryFile;
    }

    private void initializeFileGreetingService() {
        List<Greeting> greetingListFromHistory = readHistory();
        greetingListFromHistory.forEach(greeting -> webHistoryCache.put(greeting.getId(), greeting));
        lastVersionFromHistoryFile = (long) greetingListFromHistory.size();
    }

    private List<String> parseHistoryToCsvFormat() {
        return getHistorySortedByName()
                .stream()
                .map(Greeting::toCsvformat)
                .collect(Collectors.toList());
    }
}
