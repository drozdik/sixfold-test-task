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
import task.sixfold.domain.Result;
import task.sixfold.domain.RouteCalculator;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.AirportsFileReader;
import task.sixfold.file.RouteRecord;
import task.sixfold.file.RoutesFileReader;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@SpringBootApplication
@RestController
public class SixfoldTestTaskApplication {
    private final RouteCalculator calculator;
    Logger logger = LoggerFactory.getLogger(SixfoldTestTaskApplication.class);
    private final Airports airports = new Airports();

    public SixfoldTestTaskApplication(RouteCalculator calculator) {
        this.calculator = calculator;
    }

    public static void main(String[] args) {
        SpringApplication.run(SixfoldTestTaskApplication.class, args);
    }

    @PostConstruct
    public void loadAirportRecords() {
        AirportsFileReader airportsFileReader = new AirportsFileReader();
        List<AirportRecord> airportRecords = airportsFileReader.readFile();
        RoutesFileReader routesFileReader = new RoutesFileReader();
        List<RouteRecord> routeRecords = routesFileReader.readFile();

        airports.loadRecords(airportRecords);
        calculator.buildModel(airports, routeRecords);
    }

    @GetMapping(path = "/from/{fromId}/to/{toId}")
    public ResponseEntity<Object> shortestRoute(@PathVariable String fromId, @PathVariable String toId) {
        long startTime = System.nanoTime();

        List missingKeys = airports.missingAny(fromId, toId);
        if (!missingKeys.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid airport identifiers: %s ", missingKeys));
        }

        Result result;
        try {
            result = calculator.shortestRouteBetween(fromId, toId);
        } catch (RuntimeException e) {
            if (e.getMessage().startsWith("None reached destination")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Did not find any valid route between %s and %s", fromId, toId));
            }
            throw e;
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("from", AirportPayload.from(airports.getRecord(fromId)));
        payload.put("to", AirportPayload.from(airports.getRecord(toId)));
        payload.put("route", result.route.stream()
                .map(icao -> AirportPayload.from(airports.getRecord(icao)))
                .collect(toList()));
        payload.put("distance", distanceInKm(result.distance));

        long requestTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        logger.info("Handled request within {} ms", requestTime);
        return ResponseEntity.ok(payload);
    }

    @GetMapping("airport/{airportId}")
    public Object getAirportConnections(@PathVariable String airportId) {
        return calculator.getAirportConnections(airportId).stream()
                .map(iataOrIcao -> airports.getRecord(iataOrIcao))
                .map(AirportPayload::from)
                .collect(toList());
    }

    private String distanceInKm(double distanceInMeters) {
        BigDecimal inKm = BigDecimal.valueOf(distanceInMeters).divide(BigDecimal.valueOf(1000));
        DecimalFormat df = new DecimalFormat("###.00");
        return df.format(inKm) + " km";
    }

}
