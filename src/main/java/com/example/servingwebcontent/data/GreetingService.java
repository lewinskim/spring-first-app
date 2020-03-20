package com.example.servingwebcontent.data;

import com.example.servingwebcontent.Greeting;

import java.io.IOException;
import java.util.List;

public interface GreetingService {

    void addToHistory(Greeting greeting);

    List<Greeting> getHistorySortedByName();

    void replaceElementInHistory(Long histId, String name);

    void storeHistory() throws IOException;

    List<Greeting> readHistory();

    Long getHistorySize();

}
