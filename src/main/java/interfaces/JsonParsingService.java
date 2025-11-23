package interfaces;

import java.nio.file.Path;
import java.util.function.Consumer;

public interface JsonParsingService {
    /**
     * Потоково читає JSON-файл і "віддає" кожне знайдене значення
     * для вказаного атрибута через Consumer.
     *
     * @param filePath Шлях до .json файлу
     * @param attributeName Назва атрибута, який шукаємо (напр., "status" або "categories")
     * @param valueConsumer "Споживач", який отримує кожне знайдене значення.
     * Наприклад, для "categories": "Electronic, Sport" буде
     * викликано valueConsumer.accept("Electronic, Sport")
     * @throws Exception якщо файл не знайдено або він має невірний формат
     */
    void processFile(Path filePath, String attributeName, Consumer<String> valueConsumer) throws Exception;
}