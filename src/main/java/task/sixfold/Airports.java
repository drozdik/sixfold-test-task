package task.sixfold;

import task.sixfold.domain.AirportIdentifier;
import task.sixfold.file.AirportRecord;

import java.util.HashMap;
import java.util.List;

public class Airports {
    private final HashMap<String, AirportIdentifier> iataToId = new HashMap<>();
    private final HashMap<String, AirportIdentifier> icaoToId = new HashMap<>();
    private final HashMap<AirportIdentifier, AirportRecord> records = new HashMap<>();

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
}
