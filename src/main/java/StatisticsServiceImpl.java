import interfaces.*;
import enums.StatisticsAttribute;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class StatisticsServiceImpl implements StatisticsService {

    private final FileFinderService fileFinder;
    private final JsonParsingService parsingService;
    private final XmlWriterService xmlWriter;
    private final int threadCount;

    public StatisticsServiceImpl(FileFinderService fileFinder,
                                 JsonParsingService parsingService,
                                 XmlWriterService xmlWriter,
                                 int threadCount) {
        this.fileFinder = fileFinder;
        this.parsingService = parsingService;
        this.xmlWriter = xmlWriter;
        this.threadCount = threadCount;
    }

    @Override
    public void generateStatistics(Path folderPath, StatisticsAttribute attribute) throws Exception {

        List<Path> jsonFiles = fileFinder.findJsonFiles(folderPath);
        if (jsonFiles.isEmpty()) {
            System.out.println("JSON файли не знайдено в: " + folderPath);
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        StatisticsAggregator aggregator = new ConcurrentStatisticsAggregator();

        Consumer<String> valueConsumer = (value) -> {
            if (attribute.isComplex()) {
                aggregator.addComplex(value, ", ");
            } else {
                aggregator.add(value);
            }
        };

        for (Path file : jsonFiles) {
            executor.submit(() -> {
                try {
                    parsingService.processFile(file, attribute.getJsonKey(), valueConsumer);
                } catch (Exception e) {
                    System.err.println("Помилка обробки файлу " + file + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        Map<String, Long> finalStats = aggregator.getResult();

        String outputFileName = "statistics_by_" + attribute + ".xml";
        Path outputPath = folderPath.resolve(outputFileName);

        xmlWriter.write(outputPath, finalStats);
    }
}