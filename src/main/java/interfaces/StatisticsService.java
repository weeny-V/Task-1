package interfaces;

import enums.StatisticsAttribute;

import java.nio.file.Path;

public interface StatisticsService {

    /**
     * Головний метод, що запускає весь процес:
     * 1. Знаходить всі .json файли в папці.
     * 2. Запускає їх паралельну обробку в пулі потоків.
     * 3. (Кожен потік) Використовує стрімінговий парсер (JsonReader/Jackson) для читання файлу.
     * 4. Збирає проміжну статистику з кожного файлу.
     * 5. Агрегує загальну статистику.
     * 6. Сортує результат.
     * 7. Записує фінальний XML-файл.
     *
     * @param folderPath Шлях до папки з JSON файлами
     * @param attribute Атрибут для збору статистики
     * @throws Exception Може кидати IOException або власні винятки у разі помилок
     */
    void generateStatistics(Path folderPath, StatisticsAttribute attribute) throws Exception;
}