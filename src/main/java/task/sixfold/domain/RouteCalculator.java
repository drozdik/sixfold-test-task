package task.sixfold.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import task.sixfold.Airports;
import task.sixfold.algo.Airport;
import task.sixfold.algo.AlgoResult;
import task.sixfold.algo.MyAlgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
/* build model from records
 * find shortest route using iataOrIcao*/
public class RouteCalculator {
    Logger logger = LoggerFactory.getLogger(RouteCalculator.class);

    private MyAlgo myAlgo = new MyAlgo();
    private Airports airports;

    public Result shortestRouteBetween(String fromId, String toId) {
        Map<AirportIdentifier, Airport> airportNodes = this.createModel(airports);
        Airport sourceAirport = airportNodes.get(airports.getId(fromId));
        Airport destAirport = airportNodes.get(airports.getId(toId));
        AlgoResult algoResult = myAlgo.findShortestRoute(sourceAirport, destAirport, new ArrayList<>(airportNodes.values()));
        return Result.from(algoResult);
    }

    public void load(Airports airports) {
        this.airports = airports;
    }

    private Map<AirportIdentifier, Airport> createModel(Airports airports) {
        Map<AirportIdentifier, Airport> model = new HashMap<>();
        airports.entries().forEach(entry -> model.put(entry.getKey(), entry.getValue().toAirportNode()));
        model.entrySet().forEach((Map.Entry<AirportIdentifier, Airport> entry) -> {
            airports.getConnections(entry.getKey()).forEach(connection -> {
                entry.getValue().addConnectionWith(model.get(connection));
            });
        });
        return model;
    }
}
