package task.sixfold.file;

import java.util.Arrays;

public class RouteRecord {
    /*
        Airline 	2-letter (IATA) or 3-letter (ICAO) code of the airline.
        Airline ID 	Unique OpenFlights identifier for airline (see Airline).
        Source airport 	3-letter (IATA) or 4-letter (ICAO) code of the source airport.
        Source airport ID 	Unique OpenFlights identifier for source airport (see Airport)
        Destination airport 	3-letter (IATA) or 4-letter (ICAO) code of the destination airport.
        Destination airport ID 	Unique OpenFlights identifier for destination airport (see Airport)
        Codeshare 	"Y" if this flight is a codeshare (that is, not operated by Airline, but another carrier), empty otherwise.
        Stops 	Number of stops on this flight ("0" for direct)
        Equipment 	3-letter codes for plane type(s) generally used on this flight, separated by spaces

        The data is UTF-8 encoded. The special value \N is used for "NULL" to indicate that no value is available, and is understood automatically by MySQL if imported.

        example: 2B,410,AER,2965,KZN,2990,,0,CR2
    */
    public String airline, airlineId, sourceAirport, sourceAirportId, destinationAirport, destinationAirportId, codeShare, stops, equipment;

    public static RouteRecord from(String line) {
        String[] tokens = null;
        try {
            tokens = line.split(",");
            RouteRecord r = new RouteRecord();
            r.airline = tokens[0];
            r.airlineId = tokens[1];
            r.sourceAirport = tokens[2];
            r.sourceAirportId = tokens[3];
            r.destinationAirport = tokens[4];
            r.destinationAirportId = tokens[5];
            r.codeShare = tokens[6];
            r.stops = tokens[7];
            if (tokens.length == 8) {
                r.equipment = "";
            } else {
                r.equipment = tokens[8];
            }
            return r;
        } catch (IndexOutOfBoundsException e) {
            System.out.println(String.format("Line %s doesn't have 8 tokens after split %s", line, Arrays.toString(tokens)));
            throw e;
        }
    }
}
