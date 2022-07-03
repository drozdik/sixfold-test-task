package task.sixfold.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AirportsFileReader {
    public List<AirportRecord> readFile(InputStream inputStream) {
        try {
            List<String> lines = readFromInputStream(inputStream);
            return lines.stream().map(AirportRecord::from).collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e); // throwing runtime because if we can't read file, no point running
        }
    }

    private List<String> readFromInputStream(InputStream inputStream) throws IOException {
        List<String> resultStringBuilder = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.add(line);
            }
        }
        return resultStringBuilder;
    }
}
