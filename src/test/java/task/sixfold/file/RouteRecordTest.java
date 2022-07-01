package task.sixfold.file;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RouteRecordTest {

    @Test
    void parse_route_line() {
        String line = "2B,410,AER,2965,KZN,2990,,0,CR2\n";

        RouteRecord record = RouteRecord.from(line);

        assertThat(record.sourceAirport).isEqualTo("AER");
        assertThat(record.destinationAirport).isEqualTo("KZN");
    }
}