package com.example.servingwebcontent.data;

import com.example.servingwebcontent.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileGreetingService implements GreetingService {

    private final Map<Long, Greeting> webHistoryCache = new HashMap<>();
    private final Path fileWithHistory = Path.of("history", "history.csv");
    private final AtomicLong historyCounter = new AtomicLong();

    private FileGreetingService() {
        initializeFileGreetingService();
    }

    @Override
    public void addToHistory(String name) {
        webHistoryCache.put(historyCounter.incrementAndGet(),
                new Greeting(historyCounter.get(), name));
        log.info("Greeting added");
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
    public void storeHistory() {
        if (Files.notExists(fileWithHistory)) {
            try {
                Files.createDirectories(fileWithHistory.getParent());
                Files.createFile(fileWithHistory);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("file with history not created");
            }
        }
        try {
            Files.write(fileWithHistory, parseHistoryToCsvFormat(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("could not write history to file");
        }
    }

    @Override
    public List<Greeting> readHistory() {
        if (Files.notExists(fileWithHistory)) {
            return new ArrayList<>();
        } else {
            List<String> historyFromFile = null;
            try {
                historyFromFile = Files.readAllLines(fileWithHistory);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("couldnt read file with history");
            }

            log.info("list size from history is " + historyFromFile.size());
            return historyFromFile.stream()
                    .map(s -> s.split(","))
                    .map(strings -> new Greeting(Long.parseLong(strings[0]), strings[1]))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void removeFromHistory(List<String> idsOfHistoryForRemoval) {
        idsOfHistoryForRemoval.forEach(s -> webHistoryCache.remove(Long.parseLong(s)));
    }

    private void initializeFileGreetingService() {
        List<Greeting> greetingListFromHistory = readHistory();
        if (greetingListFromHistory.isEmpty()) {
            historyCounter.set(0);
        } else {
            greetingListFromHistory.forEach(greeting -> webHistoryCache.put(greeting.getId(), greeting));
            historyCounter.set(greetingListFromHistory.size());
        }
    }

    private List<String> parseHistoryToCsvFormat() {
        return getHistorySortedByName()
                .stream()
                .map(Greeting::toCsvformat)
                .collect(Collectors.toList());
    }
}
