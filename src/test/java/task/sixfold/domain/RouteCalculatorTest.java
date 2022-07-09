package task.sixfold.domain;

import org.junit.jupiter.api.Test;
import task.sixfold.Airports;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.RouteRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RouteCalculatorTest {
    RouteCalculator calculator = new RouteCalculator();

    @Test
    void find_route_between_two_airports() {
        AirportRecord tallinn = new AirportRecord();
        tallinn.ICAO = "EETN";
        tallinn.IATA = "TLL";
        tallinn.latitude = "59.41329956049999";
        tallinn.longitude = "24.832799911499997";
        tallinn.altitude = "131";

        AirportRecord riga = new AirportRecord();
        riga.ICAO = "EVRA";
        riga.IATA = "RIX";
        riga.latitude = "56.92359924316406";
        riga.longitude = "23.971099853515625";
        riga.altitude = "36";

        RouteRecord tallinn_to_riga = new RouteRecord();
        tallinn_to_riga.sourceAirport = tallinn.IATA;
        tallinn_to_riga.destinationAirport = riga.IATA;

        Airports airports = new Airports();
        airports.loadBoth(List.of(tallinn, riga), List.of(tallinn_to_riga));

        calculator.load(airports);

        // when
        List<String> route = calculator.shortestRouteBetween(tallinn.ICAO, riga.ICAO).route;

        // then
        assertThat(route).containsExactly(tallinn.ICAO, riga.ICAO);
    }
}
