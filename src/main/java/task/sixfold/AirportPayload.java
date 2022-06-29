package task.sixfold;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AirportPayload {
    String IATA;
    String ICAO;

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
