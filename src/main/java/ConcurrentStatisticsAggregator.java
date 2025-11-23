import interfaces.StatisticsAggregator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class ConcurrentStatisticsAggregator implements StatisticsAggregator {

    private final ConcurrentHashMap<String, LongAdder> stats = new ConcurrentHashMap<>();

    @Override
    public void add(String value) {
        stats.computeIfAbsent(value, k -> new LongAdder()).increment();
    }

    @Override
    public void addComplex(String complexValue, String delimiter) {
        if (complexValue == null || complexValue.isBlank()) return;

        String[] values = complexValue.split(delimiter);
        for (String value : values) {
            String trimmedValue = value.trim();
            if (!trimmedValue.isEmpty()) {
                add(trimmedValue);
            }
        }
    }

    @Override
    public Map<String, Long> getResult() {
        return stats.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().sum()
                ));
    }
}