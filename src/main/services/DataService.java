package main.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataService {

    public Map<String, String> createMap(String url, List<String> keys, List<String> values) {
        if (keys.size() != values.size())
            throw new RuntimeException("--- Lists for "+url+" have different sizes ---");
        Iterator<String> keysIterator = keys.iterator();
        Iterator<String> valuesIterator = values.iterator();
        Map<String, String> createdMap = new HashMap<>();
        while(keysIterator.hasNext() && valuesIterator.hasNext())
            createdMap.put(
                    keysIterator.next(),
                    valuesIterator.next()
            );
        return createdMap;
    }
}
