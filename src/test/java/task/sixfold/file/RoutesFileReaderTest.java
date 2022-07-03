package task.sixfold.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoutesFileReaderTest {
    private RoutesFileReader reader = new RoutesFileReader();

    @TempDir
    Path tempDir;

    @Test
    void read_record_from_file() throws IOException {
        Path path = tempDir.resolve("test-airports.dat");
        String line = "2B,410,AER,2965,KZN,2990,,0,CR2\n";
        Files.writeString(path, line);

        List<RouteRecord> records = reader.readFile(Files.newInputStream(path));

        assertThat(records).hasSize(1);
        assertThat(records.get(0).sourceAirport).isEqualTo("AER");
    }
}