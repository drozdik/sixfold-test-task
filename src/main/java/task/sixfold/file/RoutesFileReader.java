package task.sixfold.file;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class RoutesFileReader {
    public List<RouteRecord> readFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .map(RouteRecord::from)
                    .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e); // throwing runtime because if we can't read file, no point running
        }
    }
}
