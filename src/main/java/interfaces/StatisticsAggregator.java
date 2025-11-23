package interfaces;

import java.util.Map;

public interface StatisticsAggregator {
    /**
     * Додає одне значення до статистики.
     * @param value Значення атрибута (напр., "pending" або "2025-03-12")
     */
    void add(String value);

    /**
     * Обробляє складний атрибут (як "categories")
     * @param complexValue Значення типу "Electronic, Sport"
     * @param delimiter Роздільник (напр., ", ")
     */
    void addComplex(String complexValue, String delimiter);

    /**
     * Повертає фінальний результат підрахунків.
     * @return Map, де ключ - значення, а значення - кількість.
     */
    Map<String, Long> getResult();
}