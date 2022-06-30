package task.sixfold;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import task.sixfold.domain.Result;
import task.sixfold.domain.RouteCalculator;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.AirportsFileReader;
import task.sixfold.file.RouteRecord;
import task.sixfold.file.RoutesFileReader;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class SixfoldTestTaskApplication {
    Logger logger = LoggerFactory.getLogger(SixfoldTestTaskApplication.class);

    private final RouteCalculator calculator;

    public SixfoldTestTaskApplication(RouteCalculator calculator) {
        this.calculator = calculator;
    }

    public static void main(String[] args) {
        SpringApplication.run(SixfoldTestTaskApplication.class, args);
    }

    // endpoint which takes two
    @GetMapping(path = "/from/{fromId}/to/{toId}")
    public RoutePayload shortestRoute(@PathVariable String fromId, @PathVariable String toId) {
        long startTime = System.nanoTime();
        Result result = calculator.shortestRouteBetween(fromId, toId);

        RoutePayload payload = new RoutePayload();
        AirportPayload from = new AirportPayload();
        from.ICAO = fromId;
        AirportPayload to = new AirportPayload();
        to.ICAO = toId;
        payload.from = from;
        payload.to = to;
        payload.route = result.route.stream().map(icao -> {
            AirportPayload p = new AirportPayload();
            p.ICAO = icao;
            p.IATA = calculator.getIataByIcao(icao);
            return p;
        }).collect(Collectors.toList());
        payload.distance = result.distance;
        long requestTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        logger.info("Handled request within {} ms", requestTime);
        return payload;
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
