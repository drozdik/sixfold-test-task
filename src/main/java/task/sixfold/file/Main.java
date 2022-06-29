package task.sixfold.file;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        var reader = new AirportsFileReader();
        Collection<AirportRecord> airportRecords = reader.readFile();
        System.out.println(airportRecords.size());
        var reader2 = new RoutesFileReader();
        Collection<RouteRecord> routesRecords = reader2.readFile();
        System.out.println(routesRecords.size());
    }
}
