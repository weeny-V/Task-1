import interfaces.FileFinderService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileFinderServiceImpl implements FileFinderService {
    @Override
    public List<Path> findJsonFiles(Path rootDirectory) throws IOException {
        try (var stream = Files.walk(rootDirectory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                    .collect(Collectors.toList());
        }
    }
}