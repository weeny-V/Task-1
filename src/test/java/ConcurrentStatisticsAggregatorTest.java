import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentStatisticsAggregatorTest {

    @Test
    void testSimpleAggregation() {
        ConcurrentStatisticsAggregator aggregator = new ConcurrentStatisticsAggregator();

        aggregator.add("ClientA");
        aggregator.add("ClientB");
        aggregator.add("ClientA");

        Map<String, Long> result = aggregator.getResult();

        assertEquals(2, result.size(), "Має бути 2 унікальних елементи");
        assertEquals(2L, result.get("ClientA"), "ClientA має мати count 2");
        assertEquals(1L, result.get("ClientB"), "ClientB має мати count 1");
    }

    @Test
    void testComplexAttributeSplitting() {
        ConcurrentStatisticsAggregator aggregator = new ConcurrentStatisticsAggregator();

        aggregator.addComplex("Electronic, Sport", ",");
        aggregator.addComplex("Sport, Adventure, ", ",");

        Map<String, Long> result = aggregator.getResult();

        assertEquals(3, result.size(), "Має бути 3 унікальні жанри");
        assertEquals(1L, result.get("Electronic"), "Electronic має бути 1");
        assertEquals(2L, result.get("Sport"), "Sport має бути 2");
        assertEquals(1L, result.get("Adventure"), "Adventure має бути 1");
    }

    @Test
    void testSplittingWithSpacesAndEmptyValues() {
        ConcurrentStatisticsAggregator aggregator = new ConcurrentStatisticsAggregator();
        aggregator.addComplex("Sport , , Games,Adventure ", ",");

        Map<String, Long> result = aggregator.getResult();

        assertEquals(3, result.size(), "Має бути 3 унікальні жанри після очищення");
        assertEquals(1L, result.get("Sport"));
        assertEquals(1L, result.get("Games"));
        assertEquals(1L, result.get("Adventure"));
    }

    @Test
    void testThreadSafetyInConcurrentCounting() throws InterruptedException {
        ConcurrentStatisticsAggregator aggregator = new ConcurrentStatisticsAggregator();
        int numThreads = 10;
        int operationsPerThread = 1000;
        int totalExpectedCount = numThreads * operationsPerThread;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    aggregator.add("CONCURRENT_TEST_KEY");
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "Потоки не завершились вчасно");

        Map<String, Long> result = aggregator.getResult();

        assertEquals(totalExpectedCount, result.get("CONCURRENT_TEST_KEY"),
                "Потокобезпечний підрахунок має бути точним");
    }
}