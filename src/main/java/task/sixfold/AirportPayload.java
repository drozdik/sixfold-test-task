package task.sixfold;

import com.fasterxml.jackson.annotation.JsonProperty;
import task.sixfold.domain.AirportIdentifier;
import task.sixfold.file.AirportRecord;

public class AirportPayload {
    String IATA;
    String ICAO;
    String name;
    String city;

    public static AirportPayload from(AirportRecord record) {
        AirportPayload p = new AirportPayload();
        p.IATA = record.IATA;
        p.ICAO = record.ICAO;
        p.name = record.name;
        p.city = record.city;
        return p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("IATA")
    public String getIATA() {
        return IATA;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }

    @JsonProperty("ICAO")
    public String getICAO() {
        return ICAO;
    }

    public void setICAO(String ICAO) {
        this.ICAO = ICAO;
    }
}
