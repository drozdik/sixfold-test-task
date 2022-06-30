package task.sixfold.domain;

import org.junit.jupiter.api.Test;
import task.sixfold.IataOrIcao;
import task.sixfold.algo.Route;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.AirportsFileReader;
import task.sixfold.file.RouteRecord;
import task.sixfold.file.RoutesFileReader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RouteCalculatorTest {
    RouteCalculator calculator = new RouteCalculator();

    @Test
    void find_simplest_route() {
        // load airports
        AirportRecord tallinn = AirportRecord.from("" +
                "415,\"Lennart Meri Tallinn Airport\",\"Tallinn-ulemiste International\",\"Estonia\",\"TLL\",\"EETN\"" +
                ",59.41329956049999,24.832799911499997,131,2,\"E\",\"Europe/Tallinn\",\"airport\",\"OurAirports\" ");
        AirportRecord riga = AirportRecord.from("" +
                "3953,\"Riga International Airport\",\"Riga\",\"Latvia\",\"RIX\",\"EVRA\"" +
                ",56.92359924316406,23.971099853515625,36,2,\"E\",\"Europe/Riga\",\"airport\",\"OurAirports\"" +
                "");
        calculator.loadAirportRecords(List.of(tallinn, riga));

        // load connections
        RouteRecord tallinn_to_riga = RouteRecord.from("BT,333,TLL,415,RIX,3953,,0,73C DH4");
        calculator.loadRouteRecords(List.of(tallinn_to_riga));

        // when
        List<String> route = calculator.shortestRouteBetween(tallinn.ICAO, riga.ICAO).route;

        // then
        assertThat(route).containsExactly(tallinn.ICAO, riga.ICAO);
    }

    @Test
    void using_real_files() {
        AirportsFileReader airportsFileReader = new AirportsFileReader();
        List<AirportRecord> airportRecords = airportsFileReader.readFile();
        calculator.loadAirportRecords(airportRecords);
        RoutesFileReader routesFileReader = new RoutesFileReader();
        List<RouteRecord> routeRecords = routesFileReader.readFile();
        calculator.loadRouteRecords(routeRecords);

        AirportRecord tallinn = AirportRecord.from("" +
                "415,\"Lennart Meri Tallinn Airport\",\"Tallinn-ulemiste International\",\"Estonia\",\"TLL\",\"EETN\"" +
                ",59.41329956049999,24.832799911499997,131,2,\"E\",\"Europe/Tallinn\",\"airport\",\"OurAirports\" ");
        AirportRecord riga = AirportRecord.from("" +
                "3953,\"Riga International Airport\",\"Riga\",\"Latvia\",\"RIX\",\"EVRA\"" +
                ",56.92359924316406,23.971099853515625,36,2,\"E\",\"Europe/Riga\",\"airport\",\"OurAirports\"" +
                "");
        calculator.loadAirportRecords(List.of(tallinn, riga));

        // load connections
        RouteRecord tallinn_to_riga = RouteRecord.from("BT,333,TLL,415,RIX,3953,,0,73C DH4");
        calculator.loadRouteRecords(List.of(tallinn_to_riga));

        // when
        List<String> route = calculator.shortestRouteBetween(tallinn.ICAO, riga.ICAO).route;
        List<String> evraConnections = calculator.getAirportConnections("EVRA");

        // then
        assertThat(route).containsExactly(tallinn.ICAO, riga.ICAO);
    }

    @Test
    void load_files() {
        AirportsFileReader airportsFileReader = new AirportsFileReader();
        List<AirportRecord> airportRecords = airportsFileReader.readFile();
        calculator.loadAirportRecords(airportRecords);
        RoutesFileReader routesFileReader = new RoutesFileReader();
        List<RouteRecord> routeRecords = routesFileReader.readFile();
        calculator.loadRouteRecords(routeRecords);

        // when

        // then
        assertThat(calculator.getMap().size()).isGreaterThan(100);
    }
}
