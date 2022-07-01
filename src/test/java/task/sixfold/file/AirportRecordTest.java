package task.sixfold.file;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AirportRecordTest {
    @Test
    void parse_line_into_record() {
        String line = "1,\"Goroka Airport\",\"Goroka\",\"Papua New Guinea\",\"GKA\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\"\n";

        AirportRecord record = AirportRecord.from(line);

        assertThat(record.airportId).isEqualTo("1");
        assertThat(record.name).isEqualTo("Goroka Airport");
        assertThat(record.city).isEqualTo("Goroka");
        assertThat(record.country).isEqualTo("Papua New Guinea");
        assertThat(record.IATA).isEqualTo("GKA");
        assertThat(record.ICAO).isEqualTo("AYGA");
        assertThat(record.latitude).isEqualTo("-6.081689834590001");
        assertThat(record.longitude).isEqualTo("145.391998291");
        assertThat(record.altitude).isEqualTo("5282");
    }

    @Test
    void parse_airport_name_with_comma_inside() {
        String line = "1,\"Goroka, Airport\",\"Goroka\",\"Papua New Guinea\",\"GKA\",\"AYGA\",-6.081689834590001,145.391998291,5282,10,\"U\",\"Pacific/Port_Moresby\",\"airport\",\"OurAirports\"\n";

        AirportRecord record = AirportRecord.from(line);

        assertThat(record.name).isEqualTo("Goroka, Airport");
    }
}