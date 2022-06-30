package task.sixfold.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import task.sixfold.Airports;
import task.sixfold.algo.Airport;
import task.sixfold.algo.AlgoResult;
import task.sixfold.algo.MyAlgo;
import task.sixfold.algo.Route;
import task.sixfold.file.RouteRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
/* build model from records
 * find shortest route using iataOrIcao*/
public class RouteCalculator {
    Logger logger = LoggerFactory.getLogger(RouteCalculator.class);

    private MyAlgo myAlgo = new MyAlgo();
    private Map<AirportIdentifier, Airport> airportNodes;
    private Airports airports;

    public Result shortestRouteBetween(String fromId, String toId) {
        Airport sourceAirport = airportNodes.get(airports.getId(fromId));
        Airport destAirport = airportNodes.get(airports.getId(toId));
        AlgoResult algoResult = myAlgo.findShortestRoute(sourceAirport, destAirport, new ArrayList<>(airportNodes.values()));
        return Result.from(algoResult);
    }

    public void buildModel(Airports airports, List<RouteRecord> routes) {
        Map<AirportIdentifier, Airport> model = new HashMap<>();
        airports.entries().forEach(entry -> model.put(entry.getKey(), entry.getValue().toAirportNode()));
        routes.forEach(record -> {
            Airport source = model.get(airports.getId(record.sourceAirport));
            Airport destination = model.get(airports.getId(record.destinationAirport));
            if (source == null || destination == null) {
                logger.warn("Skipping connection {} because either src or dest missing in model", record);
                return;
            }
            source.addConnectionWith(destination);
        });
        this.airportNodes = model;
        this.airports = airports;
    }

    public List<String> getAirportConnections(String airportId) {
        Airport airport = airportNodes.get(airports.getId(airportId));
        return airport.getConnections().stream().map(c -> c.getIdentifier()).collect(Collectors.toList());
    }

}
