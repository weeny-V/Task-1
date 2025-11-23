import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JacksonStreamingParsingServiceTest {

    @TempDir
    Path tempDir;

    private final JacksonStreamingParsingService parser = new JacksonStreamingParsingService();

    @SuppressWarnings("unchecked")
    private final Consumer<String> mockConsumer = Mockito.mock(Consumer.class);

    private void writeTestFile(Path filePath, String content) throws IOException {
        Files.writeString(filePath, content);
    }

    @Test
    void testSuccessfulAttributeExtraction() throws Exception {
        Path tempFile = tempDir.resolve("client_simple.json");
        String jsonContent = "[{\"order_id\": \"ORD-1001\", \"client_id\": \"C001\", \"client_name\": \"Іван Петренко\", \"order_date\": \"2025-03-12\", \"total_amount\": 2499.5, \"status\": \"completed\", \"categories\": \"Електроніка, Аксесуари\"}]";
        writeTestFile(tempFile, jsonContent);

        parser.processFile(tempFile, "status", mockConsumer);

        verify(mockConsumer, times(1)).accept("completed");
        verifyNoMoreInteractions(mockConsumer);
    }

    @Test
    void testExtractionFromMultipleObjects() throws Exception {
        Path tempFile = tempDir.resolve("multiple_clients.json");
        String jsonContent = "[" +
                "{" +
                "\"order_id\": \"ORD-1001\"," +
                "\"client_id\": \"C001\"," +
                "\"client_name\": \"Іван Петренко\"," +
                "\"order_date\": \"2025-03-12\"," +
                "\"total_amount\": 2499.5," +
                "\"status\": \"completed\"," +
                "\"categories\": \"Електроніка, Аксесуари\"" +
                "}," +
                "{" +
                "\"order_id\": \"ORD-1002\"," +
                "\"client_id\": \"C002\"," +
                "\"client_name\": \"Олена Мельник\"," +
                "\"order_date\": \"2025-03-12\"," +
                "\"total_amount\": 399.0," +
                "\"status\": \"completed\"," +
                "\"categories\": \"Книги, Освіта\"" +
                "}" +
                "]";
        writeTestFile(tempFile, jsonContent);

        parser.processFile(tempFile, "categories", mockConsumer);

        verify(mockConsumer, times(1)).accept("Електроніка, Аксесуари");
        verify(mockConsumer, times(1)).accept("Книги, Освіта");
        verifyNoMoreInteractions(mockConsumer);
    }

    @Test
    void testMissingAttributeIsIgnored() throws Exception {
        Path tempFile = tempDir.resolve("missing_attr.json");
        String jsonContent = "[" +
                "{" +
                "\"order_id\": \"ORD-1001\"," +
                "\"client_id\": \"C001\"," +
                "\"client_name\": \"Іван Петренко\"," +
                "\"order_date\": \"2025-03-12\"," +
                "\"total_amount\": 2499.5," +
                "\"status\": \"completed\"," +
                "\"categories\": \"Електроніка, Аксесуари\"" +
                "}," +
                "{" +
                "\"order_id\": \"ORD-1002\"," +
                "\"client_id\": \"C002\"," +
                "\"client_name\": \"Олена Мельник\"," +
                "\"total_amount\": 399.0," +
                "\"status\": \"completed\"," +
                "\"categories\": \"Книги, Освіта\"" +
                "}" +
                "]";

        writeTestFile(tempFile, jsonContent);

        parser.processFile(tempFile, "order_date", mockConsumer);

        verify(mockConsumer, times(1)).accept("2025-03-12");
        verifyNoMoreInteractions(mockConsumer);
    }

    @Test
    void testInvalidJsonThrowsException() throws Exception {
        Path tempFile = tempDir.resolve("invalid.json");

        String jsonContent = "{" +
                "\"order_id\": \"ORD-1001\"," +
                "\"client_id\": \"C001\"," +
                "\"client_name\": \"Іван Петренко\"," +
                "\"order_date\": \"2025-03-12\"," +
                "\"total_amount\": 2499.5," +
                "\"status\": \"completed\"," +
                "\"categories\": \"Електроніка, Аксесуари\"" +
                "}," +
                "[{" +
                "\"order_id\": \"ORD-1002\"," +
                "\"client_id\": \"C002\"," +
                "\"client_name\": \"Олена Мельник\"," +
                "\"order_date\": \"2025-03-12\"," +
                "\"total_amount\": 399.0," +
                "\"status\": \"completed\"," +
                "\"categories\": \"Книги, Освіта\"" +
                "}]";
        writeTestFile(tempFile, jsonContent);

        assertThrows(JsonParseException.class, () ->
                parser.processFile(tempFile, "author", mockConsumer)
        );
        verifyNoInteractions(mockConsumer);
    }
}