package task.sixfold.file;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AirportsFileReader {
    public List<AirportRecord> readFile() {
        try {
            Path path = Paths.get(AirportsFileReader.class.getClassLoader().getResource("airports.dat").toURI());
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .map(AirportRecord::from)
                    .collect(toList());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e); // throwing runtime because if we can't read file, no point running
        } catch (IOException e) {
            throw new RuntimeException(e); // throwing runtime because if we can't read file, no point running
        }
    }
}
