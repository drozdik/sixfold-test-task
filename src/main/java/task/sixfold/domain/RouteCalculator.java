package task.sixfold.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import task.sixfold.Airports;
import task.sixfold.algo.Airport;
import task.sixfold.algo.AlgoResult;
import task.sixfold.algo.MyAlgo;
import task.sixfold.file.RouteRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
/* build model from records
 * find shortest route using iataOrIcao*/
public class RouteCalculator {
    Logger logger = LoggerFactory.getLogger(RouteCalculator.class);

    private MyAlgo myAlgo = new MyAlgo();
    private Airports airports;
    private List<RouteRecord> routes;

    public Result shortestRouteBetween(String fromId, String toId) {
        Map<AirportIdentifier, Airport> airportNodes = this.createModel(airports, routes);
        Airport sourceAirport = airportNodes.get(airports.getId(fromId));
        Airport destAirport = airportNodes.get(airports.getId(toId));
        AlgoResult algoResult = myAlgo.findShortestRoute(sourceAirport, destAirport, new ArrayList<>(airportNodes.values()));
        return Result.from(algoResult);
    }

    public void load(Airports airports, List<RouteRecord> routes) {
        this.routes = routes;
        this.airports = airports;
    }

    private Map<AirportIdentifier, Airport> createModel(Airports airports, List<RouteRecord> routes) {
        Map<AirportIdentifier, Airport> model = new HashMap<>();
        // create nodes
        airports.entries().forEach(entry -> model.put(entry.getKey(), entry.getValue().toAirportNode()));
        // set connections
        routes.forEach(record -> {
            Airport source = model.get(airports.getId(record.sourceAirport));
            Airport destination = model.get(airports.getId(record.destinationAirport));
            if (source == null || destination == null) {
                logger.warn("Skipping connection {} because either src or dest missing in model", record);
                return;
            }
            source.addConnectionWith(destination);
        });
        return model;
    }
}
