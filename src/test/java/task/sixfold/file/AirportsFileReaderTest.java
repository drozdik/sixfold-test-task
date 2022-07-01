package task.sixfold.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AirportsFileReaderTest {
    private AirportsFileReader reader = new AirportsFileReader();

    @TempDir
    Path tempDir;

    @Test
    void read_record_from_file() throws IOException {
        Path tempFile = Files.createFile(tempDir.resolve("test-airports.dat"));
        String line = "1,\"Goroka Airport\",\"Goroka\",\"Papua New Guinea\",\"GKA\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\"\n";
        Files.writeString(tempFile, line);

        List<AirportRecord> records = reader.readFile(tempFile);

        assertThat(records).hasSize(1);
        assertThat(records.get(0).city).isEqualTo("Goroka");
    }
}