package task.sixfold.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Route {
    public static int legsGrown = 0;
    public ArrayList<Airport> airports = new ArrayList<>();

    public Route(Airport... airports) {
        this.airports.addAll(List.of(airports));
    }

    public Airport getTip() {
        if (airports.isEmpty()) {
            return null;
        }
        return this.airports.get(airports.size() - 1);
    }

    public double calculateDistance() {
        double distance = 0;
        Airport prev = null;
        for (Airport airport : airports) {
            if (prev == null) {
                prev = airport;
                continue;
            }
            distance += airport.distanceFrom(prev);
            prev = airport;
        }
        return distance;
    }

    @Override
    public String toString() {
        String str = "Route: ";
        for (int i = 0; i < airports.size(); i++) {
            Airport airport = airports.get(i);
            str += String.format("(%s)", airport.identifier);
            if (i < airports.size() - 1) {
                str += String.format("-%.2f-", airport.distanceFrom(airports.get(i + 1)));
            }
        }
        str += String.format(" :: %.2f", calculateDistance());
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        if (airports.size() != route.airports.size()) {
            return false;
        }
        for (int i = 0; i < route.airports.size(); i++) {
            if (!airports.get(i).equals(route.airports.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(airports);
    }

    public void addAirport(Airport airport) {
        airports.add(airport);
        legsGrown++;
    }

    public Airport pop() {
        return this.airports.remove(airports.size() - 1);
    }

    public Route copy() {
        return new Route(airports.toArray(new Airport[airports.size()]));
    }

    public List<Airport> getAirports() {
        return this.airports;
    }
}
