import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import interfaces.JsonParsingService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class JacksonStreamingParsingService implements JsonParsingService {

    private final JsonFactory factory = new JsonFactory();

    @Override
    public void processFile(Path filePath, String attributeName, Consumer<String> valueConsumer) throws Exception {

        try (JsonParser parser = factory.createParser(Files.newInputStream(filePath))) {

            while (parser.nextToken() != null) {

                if (parser.getCurrentToken() == JsonToken.FIELD_NAME) {

                    if (attributeName.equals(parser.getCurrentName())) {

                        parser.nextToken();

                        String value = parser.getText();

                        if (value != null && !value.isBlank()) {
                            valueConsumer.accept(value);
                        }
                    }
                }
            }
        }
    }
}