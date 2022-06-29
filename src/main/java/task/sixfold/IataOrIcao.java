package task.sixfold;

import java.util.Objects;

public class IataOrIcao {
    public String IATA, ICAO;

    public IataOrIcao(String value) {
        if (value.length() == 4) {
            ICAO = value;
        } else if (value.length() == 2 || value.length() == 3) {
            IATA = value;
        } else {
            throw new RuntimeException("Invalid value for IATA or ICAO");
        }
    }

    public boolean isIcao() {
        return ICAO != null;
    }

    public boolean isIata() {
        return IATA != null;
    }

    public String getValue() {
        return ICAO != null ? ICAO : IATA;
    }

}
