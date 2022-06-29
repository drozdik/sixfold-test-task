package task.sixfold.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import task.sixfold.IataOrIcao;
import task.sixfold.algo.Airport;
import task.sixfold.algo.Center;
import task.sixfold.algo.Coordinates;
import task.sixfold.algo.Route;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.RouteRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RouteCalculator {
    private Center center;
    private Map<String, AirportIdentifier> iataToId = new HashMap<>(); // ICAO for now
    private Map<String, AirportIdentifier> icaoToId = new HashMap<>(); // ICAO for now
    private Map<AirportIdentifier, Airport> map = new HashMap<>(); // ICAO for now
    public RouteCalculator() {
        center = new Center();
    }

    Logger logger = LoggerFactory.getLogger(RouteCalculator.class);
    public Map<AirportIdentifier, Airport> getMap() {
        return map;
    }

    public void loadAirportRecords(List<AirportRecord> records) {
        map.clear();
        icaoToId.clear();
        iataToId.clear();
        records.forEach(r -> {
            Airport airport = new Airport(r.ICAO, new Coordinates(Integer.parseInt(r.latitude.split("\\.")[0]), Integer.parseInt(r.longitude.split("\\.")[0])));
            AirportIdentifier identifier = new AirportIdentifier(r.IATA, r.ICAO);
            map.put(identifier, airport);
            icaoToId.put(r.ICAO, identifier);
            iataToId.put(r.IATA, identifier);
        });
    }

    public List<String> shortestRouteBetween(String fromId, String toId) {
        AirportIdentifier sourceId = getId(fromId);
        Airport sourceAirport = map.get(sourceId);

        AirportIdentifier destId = getId(toId);
        Airport destAirport = map.get(destId);

        Route route = center.findShortestRoute(sourceAirport, destAirport, new ArrayList<>(map.values()));
        return route.airports.stream().map(airport -> airport.identifier).collect(Collectors.toList());
    }

    public void loadRouteRecords(List<RouteRecord> records) {
        records.forEach(record -> {
            try {
                AirportIdentifier sourceId = getId(record.sourceAirport);
                Airport sourceAirport = map.get(sourceId);

                AirportIdentifier destId = getId(record.destinationAirport);
                Airport destAirport = map.get(destId);

                sourceAirport.addConnectionWith(destAirport);
            } catch (RuntimeException e) {
                logger.warn("Won't add connection between {} and {} because {}", record.sourceAirport, record.destinationAirport, e.getMessage());
            }
        });
    }

    private AirportIdentifier getId(String key) {
        key = key.toUpperCase();
        if (iataToId.containsKey(key)) {
            return iataToId.get(key);
        }
        if (icaoToId.containsKey(key)) {
            return icaoToId.get(key);
        }
        throw new RuntimeException(String.format("Can't find identifier by iata/icao %s", key));
    }

    public List<String> getAirportConnections(String airportId) {
        Airport airport = map.get(getId(airportId));
        return airport.getConnections().stream().map(c -> c.getIdentifier()).collect(Collectors.toList());
    }
}
