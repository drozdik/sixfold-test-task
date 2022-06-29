package task.sixfold.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirportIdentifier {

    Logger logger = LoggerFactory.getLogger(AirportIdentifier.class);

    private String iata;
    private String icao;

    public AirportIdentifier(String iata, String icao) {
        if (iata != null && (iata.strip().length() == 0 || iata.equals("\\N"))) {
            iata = null;
        }
        if (icao != null && (icao.strip().length() == 0 || icao.equals("\\N"))) {
            icao = null;
        }
        if (iata != null
                && (
                !iata.chars().allMatch(Character::isLetter)
                        || (iata.length() > 3 || iata.length() < 2)
                        || iata.strip().length() == 0)
        ) {
            logger.warn("Unexpected IATA code {}", iata);
        }
        if (icao != null && (
                icao.length() != 4
                        || icao.strip().length() == 0)) {
            logger.warn("Unexpected ICAO code {}", icao);
        }
        this.iata = iata;
        this.icao = icao;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }
}
