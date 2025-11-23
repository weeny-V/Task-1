import enums.StatisticsAttribute;
import interfaces.StatisticsService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConsoleInterface {

    private final StatisticsService statisticsService;

    public ConsoleInterface(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Головний метод запуску CLI.
     * @param args Аргументи командного рядка
     */
    public void run(String[] args) {
        if (args.length != 2) {
            printError("Невірна кількість аргументів.");
            printUsage();
            return;
        }

        String folderPathStr = args[0];
        String attributeNameStr = args[1];
        StatisticsAttribute attribute;

        Path folderPath = Paths.get(folderPathStr);
        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            printError("Вказаний шлях не існує або не є папкою: " + folderPathStr);
            return;
        }

        try {
            attribute = StatisticsAttribute.valueOf(attributeNameStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            printError("Атрибут '" + attributeNameStr + "' не підтримується.");
            System.err.println("Доступні атрибути: " + java.util.Arrays.toString(StatisticsAttribute.values()));
            return;
        }

        System.out.println("✅ Розпочинаю обробку...");
        System.out.println("   Папка: " + folderPath.toAbsolutePath());
        System.out.println("   Атрибут: " + attribute);

        long startTime = System.currentTimeMillis();

        try {
            statisticsService.generateStatistics(folderPath, attribute);

            long endTime = System.currentTimeMillis();
            String outputFileName = "statistics_by_" + attribute + ".xml";

            System.out.println("==========================================");
            System.out.println("🎉 Успіх!");
            System.out.println("   Файл з результатами: " + outputFileName);
            System.out.println("   Час виконання: " + (endTime - startTime) + " мс");
            System.out.println("==========================================");

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            printError("Під час обробки сталася критична помилка: " + e.getMessage());
            System.out.println("   Час виконання (до помилки): " + (endTime - startTime) + " мс");
        }
    }

    /**
     * Друкує повідомлення про помилку в System.err
     */
    private void printError(String message) {
        System.err.println("❌ ПОМИЛКА: " + message);
    }

    /**
     * Друкує інструкцію з використання програми
     */
    private void printUsage() {
        System.out.println("\nВикористання:");
        System.out.println("  java -jar statistics-app.jar <path_to_folder> <attribute_name>");
        System.out.println("\nПриклад:");
        System.out.println("  java -jar statistics-app.jar ./data/books genre");
    }
}