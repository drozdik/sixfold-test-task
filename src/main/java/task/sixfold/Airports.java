package task.sixfold;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.sixfold.domain.AirportIdentifier;
import task.sixfold.file.AirportRecord;
import task.sixfold.file.RouteRecord;

import java.util.*;

import static java.util.Collections.emptySet;

public class Airports {
    private final HashMap<String, AirportIdentifier> iataToId = new HashMap<>();
    private final HashMap<String, AirportIdentifier> icaoToId = new HashMap<>();
    private final HashMap<AirportIdentifier, AirportRecord> records = new HashMap<>();
    private final Map<AirportIdentifier, Set<AirportIdentifier>> connections = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(Airports.class);
    public Airports(List<AirportRecord> airportRecords) {
        loadRecords(airportRecords);
    }

    public Airports() {

    }

    public AirportIdentifier getId(String iataOrIcao) {
        iataOrIcao = iataOrIcao.toUpperCase();
        if (iataToId.containsKey(iataOrIcao)) {
            return iataToId.get(iataOrIcao);
        }
        if (icaoToId.containsKey(iataOrIcao)) {
            return icaoToId.get(iataOrIcao);
        }
        return null;
    }

    public void loadRecords(List<AirportRecord> records) {
        records.forEach(r -> {
            AirportIdentifier id = new AirportIdentifier(r.IATA, r.ICAO);
            this.records.put(id, r);
            if (id.hasIata()) {
                iataToId.put(id.getIata(), id);
            }
            if (id.hasIcao()) {
                icaoToId.put(id.getIcao(), id);
            }
        });
    }

    public AirportRecord getRecord(String iataOrIcao) {
        AirportIdentifier id = getId(iataOrIcao);
        if (id == null || !records.containsKey(id)) {
            return null;
        }
        return records.get(id);
    }

    public List missingAny(String from, String to) {
        List<String> missing = new ArrayList<>();
        if (getId(from) == null) {
            missing.add(from);
        }
        if (getId(to) == null) {
            missing.add(to);
        }
        return missing;
    }

    public Collection<AirportRecord> allRecords() {
        return records.values();
    }

    public Set<Map.Entry<AirportIdentifier, AirportRecord>> entries() {
        return records.entrySet();
    }

    public void loadBoth(List<AirportRecord> aRecords, List<RouteRecord> rRecords) {
        loadRecords(aRecords);
        rRecords.forEach(route -> {
            if (getId(route.sourceAirport) == null || getId(route.destinationAirport) == null) {
                logger.warn("route record has airports missing in database");
                return;
            }
            AirportIdentifier airportId = getId(route.sourceAirport);
            Set<AirportIdentifier> connections = this.connections.getOrDefault(airportId, new HashSet<>());
            connections.add(getId(route.destinationAirport));
            this.connections.put(airportId, connections);
        });
    }

    public List<AirportIdentifier> getConnections(String iataOrIcao) {
        return new ArrayList<>(connections.getOrDefault(getId(iataOrIcao), emptySet()));
    }

    public List<AirportIdentifier> getConnections(AirportIdentifier identifier) {
        return new ArrayList<>(connections.getOrDefault(identifier, emptySet()));
    }
}
