package task.sixfold;

import org.junit.jupiter.api.Test;
import task.sixfold.domain.AirportIdentifier;
import task.sixfold.file.AirportRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AirportsTest {

    private Airports airports = new Airports();

    @Test
    void get_id_by_lower_case_iata() {
        // given
        AirportRecord tallinn = new AirportRecord();
        tallinn.IATA = "TLL";
        tallinn.ICAO = "EETN";
        airports.loadRecords(List.of(tallinn));

        // when
        AirportIdentifier id = airports.getId("tll");

        // then
        assertThat(id.getIata()).isEqualTo("TLL");
        assertThat(id.getIcao()).isEqualTo("EETN");
    }

    @Test
    void get_id_by_lower_case_icao() {
        // given
        AirportRecord tallinn = new AirportRecord();
        tallinn.IATA = "TLL";
        tallinn.ICAO = "EETN";
        airports.loadRecords(List.of(tallinn));

        // when
        AirportIdentifier id = airports.getId("eetn");

        // then
        assertThat(id.getIata()).isEqualTo("TLL");
        assertThat(id.getIcao()).isEqualTo("EETN");
    }

    @Test
    void get_record_by_iata() {
        // given
        AirportRecord tallinn = new AirportRecord();
        tallinn.IATA = "TLL";
        tallinn.ICAO = "EETN";
        airports.loadRecords(List.of(tallinn));

        // when
        AirportRecord record = airports.getRecord("tll");

        // then
        assertThat(record).isEqualTo(tallinn);

    }

    @Test
    void get_record_by_icao() {
        // given
        AirportRecord tallinn = new AirportRecord();
        tallinn.IATA = "TLL";
        tallinn.ICAO = "EETN";
        airports.loadRecords(List.of(tallinn));

        // when
        AirportRecord record = airports.getRecord("eetn");

        // then
        assertThat(record).isEqualTo(tallinn);

    }
}
