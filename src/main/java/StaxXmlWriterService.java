import interfaces.XmlWriterService;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StaxXmlWriterService implements XmlWriterService {

    @Override
    public void write(Path outputFilePath, Map<String, Long> statistics) throws Exception {

        List<Map.Entry<String, Long>> sortedList = statistics.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        try (var writerStream = Files.newOutputStream(outputFilePath)) {

            XMLStreamWriter writer = factory.createXMLStreamWriter(writerStream, "UTF-8");


            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");

            writer.writeStartElement("statistics");
            writer.writeCharacters("\n");

            for (Map.Entry<String, Long> entry : sortedList) {
                writer.writeCharacters("  ");
                writer.writeStartElement("item");
                writer.writeCharacters("\n");

                writer.writeCharacters("    ");
                writer.writeStartElement("value");
                writer.writeCharacters(entry.getKey());
                writer.writeEndElement();
                writer.writeCharacters("\n");

                writer.writeCharacters("    ");
                writer.writeStartElement("count");
                writer.writeCharacters(String.valueOf(entry.getValue()));
                writer.writeEndElement();
                writer.writeCharacters("\n");

                writer.writeCharacters("  ");
                writer.writeEndElement();
                writer.writeCharacters("\n");
            }

            writer.writeEndElement();
            writer.writeEndDocument();

            writer.flush();
            writer.close();

        } catch (IOException | XMLStreamException e) {
            throw new Exception("Помилка запису XML-файлу: " + outputFilePath, e);
        }
    }
}