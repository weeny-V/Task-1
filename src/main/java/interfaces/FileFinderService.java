package interfaces;

import java.nio.file.Path;
import java.util.List;
import java.io.IOException;

public interface FileFinderService {
    /**
     * Знаходить усі файли з розширенням .json у вказаній папці та її підпапках.
     * @param rootDirectory початкова папка для пошуку
     * @return Список шляхів (Path) до знайдених .json файлів
     * @throws IOException якщо є проблеми з доступом до папки
     */
    List<Path> findJsonFiles(Path rootDirectory) throws IOException;
}