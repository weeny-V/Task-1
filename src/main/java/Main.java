import interfaces.FileFinderService;
import interfaces.JsonParsingService;
import interfaces.StatisticsService;
import interfaces.XmlWriterService;

/**
 * Головна точка входу в програму.
 * Тепер цей метод лише координує головні кроки.
 */

void main(String[] args) {

    StatisticsService statisticsService = createStatisticsService();

    ConsoleInterface cli = new ConsoleInterface(statisticsService);

    cli.run(args);
}

/**
 * Приватний "фабричний" метод, який відповідає за створення
 * та "збірку" всіх сервісів програми.
 *
 * @return Повністю налаштований екземпляр StatisticsService.
 */
private static StatisticsService createStatisticsService() {

    FileFinderService fileFinder = new FileFinderServiceImpl();
    JsonParsingService parsingService = new JacksonStreamingParsingService();
    XmlWriterService xmlWriter = new StaxXmlWriterService();

    int threadCount = 8;

    return new StatisticsServiceImpl(
            fileFinder,
            parsingService,
            xmlWriter,
            threadCount
    );
}