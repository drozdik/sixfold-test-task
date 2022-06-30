package task.sixfold.algo;

import org.junit.jupiter.api.Test;
import task.sixfold.Airports;
import task.sixfold.domain.Result;
import task.sixfold.domain.RouteCalculator;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.AirportsFileReader;
import task.sixfold.file.RouteRecord;
import task.sixfold.file.RoutesFileReader;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MyAlgoTest {
    private MyAlgo myAlgo = new MyAlgo();

    @Test
    void shortest_route_between_tallinn_and_riga() {
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

        // when
        Airport start = tallinn.toAirportNode();
        Airport destination = riga.toAirportNode();
        // it will be good to convert just into list of identifiers
        List<Airport> model = List.of(start, destination);
        start.addConnectionWith(destination);
        Route result = myAlgo.findShortestRoute(start, destination, model);
        List<String> idsOfShortestRoute = result.airports.stream().map(airport -> airport.identifier).collect(Collectors.toList());

        // then
        assertThat(idsOfShortestRoute).isEqualTo(List.of("EETN", "EVRA"));

    }

    @Test
    void route_between_tallinn_and_tokyo() {
        AirportsFileReader airportsFileReader = new AirportsFileReader();
        List<AirportRecord> airportRecords = airportsFileReader.readFile();
        Airports airports = new Airports();
        airports.loadRecords(airportRecords);
        RoutesFileReader routesFileReader = new RoutesFileReader();
        List<RouteRecord> routeRecords = routesFileReader.readFile();

        RouteCalculator calculator = new RouteCalculator();
        calculator.buildModel(airports, routeRecords);

        Result result = calculator.shortestRouteBetween("tll", "hnd");

        // then
        assertThat(result.route).isEqualTo(List.of("EETN", "EFHK", "RJGG", "RJTT"));
    }
}
