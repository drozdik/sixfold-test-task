package task.sixfold.file;

import task.sixfold.algo.Airport;
import task.sixfold.algo.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;

public class AirportRecord {
    /*
    Airport ID 	Unique OpenFlights identifier for this airport.
    Name 	Name of airport. May or may not contain the City name.
    City 	Main city served by airport. May be spelled differently from Name.
    Country 	Country or territory where airport is located. See Countries to cross-reference to ISO 3165-1 codes.
    IATA 	2-letter IATA code. Null if not assigned/unknown. // is a three-character alphanumeric geocode designating many airports
    ICAO 	3-letter ICAO code. // In general IATA codes are usually derived from the name of the airport or the city it serves, while ICAO codes are distributed by region and country
    Null if not assigned.
    Latitude 	Decimal degrees, usually to six significant digits. Negative is South, positive is North.
    Longitude 	Decimal degrees, usually to six significant digits. Negative is West, positive is East.
    Altitude 	In feet.
    Timezone 	Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 4.5.
    DST 	Daylight savings time. One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown). See also: Help: Time
    Tz database time zone 	Timezone in "tz" (Olson) format, eg. "America/Los_Angeles".
    Type 	Type of the airport. Value "airport" for air terminals, "station" for train stations, "port" for ferry terminals and "unknown" if not known. In airports.csv, only type=airport is included.
    Source 	Source of this data. "OurAirports" for data sourced from OurAirports, "Legacy" for old data not matched to OurAirports (mostly DAFIF), "User" for unverified user contributions. In airports.csv, only source=OurAirports is included.
    * */
    public String airportId, name, city, country, IATA, ICAO, latitude, longitude, altitude, timezone, DST, tz, type, source;

    public static AirportRecord from(String line) {
        String[] tokens = split(line);
        for (int i = 0; i < 14; i++) {
            try {
                String token = tokens[i];
                if (token.startsWith(" ")) {
                    System.out.println(String.format("WARNING: token %s value starts with space, trimming actually needed", token));
                }
            } catch (IndexOutOfBoundsException e ) {
                System.out.println(String.format("WARNING: line \n %s \n has less than 14 tokens", Arrays.toString(tokens)));
            }
        }
        var r = new AirportRecord();
        r.airportId = tokens[0];
        r.name = tokens[1];
        r.city = tokens[2];
        r.country = tokens[3];
        r.IATA = tokens[4];
        r.ICAO = tokens[5];
        r.latitude = tokens[6];
        r.longitude = tokens[7];
        r.altitude = tokens[8];
        r.timezone = tokens[9];
        r.DST = tokens[10];
        r.tz = tokens[11];
        r.type = tokens[12];
        r.source = tokens[13];
        return r;
    }

    private static String[] split(String line) {
        var tokens = new ArrayList();
        String token = "";
        boolean quoteOpened = false;
        for (char c : line.toCharArray()) {
            if (c == ',' && !quoteOpened) {
                tokens.add(token);
                token = "";
                continue;
            }
            if (c == '"') {
                if (quoteOpened) {
                    quoteOpened = false;
                    continue;
                }
                quoteOpened = true;
                continue;
            }
            token += c;
        }
        // append last token
        tokens.add(token);
        return (String[]) tokens.toArray(new String[tokens.size()]);
    }

    public Airport toAirportNode() {
        Airport airport = new Airport(ICAO, new Coordinates(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(altitude)));
        return airport;
    }
}
