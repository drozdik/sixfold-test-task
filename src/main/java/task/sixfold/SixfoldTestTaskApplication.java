package task.sixfold;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import task.sixfold.domain.AirportIdentifier;
import task.sixfold.domain.Result;
import task.sixfold.domain.RouteCalculator;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.AirportsFileReader;
import task.sixfold.file.RouteRecord;
import task.sixfold.file.RoutesFileReader;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class SixfoldTestTaskApplication {
    private final RouteCalculator calculator;
    Logger logger = LoggerFactory.getLogger(SixfoldTestTaskApplication.class);

    public SixfoldTestTaskApplication(RouteCalculator calculator) {
        this.calculator = calculator;
    }

    public static void main(String[] args) {
        SpringApplication.run(SixfoldTestTaskApplication.class, args);
    }

    private Airports airports = new Airports();


    @PostConstruct
    public void loadAirportRecords() {
        AirportsFileReader airportsFileReader = new AirportsFileReader();
        List<AirportRecord> airportRecords = airportsFileReader.readFile();
        airports.loadRecords(airportRecords);
    }

    // endpoint which takes two
    @GetMapping(path = "/from/{fromId}/to/{toId}")
    public ResponseEntity<Object> shortestRoute(@PathVariable String fromId, @PathVariable String toId) {
        long startTime = System.nanoTime();

        List missingKeys = airports.missingAny(fromId, toId);
        if (!missingKeys.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid airport identifiers: %s ", missingKeys));
        }

        // calculate
        Result result;
        try {
            result = calculator.shortestRouteBetween(fromId, toId);
        } catch (RuntimeException e) {
            if (e.getMessage().startsWith("None reached destination")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Did not find any valid route between %s and %s", fromId, toId));
            }
            throw e;
        }

        RoutePayload payload = new RoutePayload();
        AirportPayload from = AirportPayload.from(airports.getRecord(fromId));
        AirportPayload to = AirportPayload.from(airports.getRecord(toId));
        payload.from = from;
        payload.to = to;
        payload.route = result.route.stream().map(icao -> {
            AirportPayload p = AirportPayload.from(airports.getRecord(icao));
            return p;
        }).collect(Collectors.toList());
        payload.distance = result.distance;

        long requestTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        logger.info("Handled request within {} ms", requestTime);
        return ResponseEntity.ok(payload);
    }

    private AirportIdentifier getAirportIdentifier(String value) {
        return calculator.getAirportIdentifier(value);
    }

    // from, to
    // route [ airport payload ... up to 5 ]
    @GetMapping("airport/{airportId}")
    public Object getAirportConnections(@PathVariable String airportId) {
        return calculator.getAirportConnections(airportId);
    }

    @GetMapping("load")
    public void load() {
        AirportsFileReader airportsFileReader = new AirportsFileReader();
        List<AirportRecord> airportRecords = airportsFileReader.readFile();
        calculator.loadAirportRecords(airportRecords);
        RoutesFileReader routesFileReader = new RoutesFileReader();
        List<RouteRecord> routeRecords = routesFileReader.readFile();
        calculator.loadRouteRecords(routeRecords);
    }

}
