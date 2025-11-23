package interfaces;

import java.nio.file.Path;
import java.util.Map;

public interface XmlWriterService {
    /**
     * Сортує статистику (від більшого до меншого) і записує у XML-файл.
     *
     * @param outputFilePath Шлях до файлу (напр., "statistics_by_categories.xml")
     * @param statistics Несортована Map зі статистикою
     * @throws Exception якщо є проблеми із записом файлу
     */
    void write(Path outputFilePath, Map<String, Long> statistics) throws Exception;
}