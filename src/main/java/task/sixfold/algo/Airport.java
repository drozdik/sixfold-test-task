package task.sixfold.algo;

import java.util.*;
import java.util.stream.Collectors;

public class Airport {
    public final String identifier;
    private final Coordinates coordinates;
    private Set<Airport> connected = new HashSet<>();
    private double distanceFromDestination;

    public Airport(String identifier, Coordinates coordinates) {
        this.identifier = identifier;
        this.coordinates = coordinates;
    }

    public double distanceFrom(Airport another) {
        return this.coordinates.distanceFrom(another.coordinates);
    }

    public void addConnectionWith(Airport... airports) {
        connected.addAll(List.of(airports));
    }

    public List<Airport> getConnections() {
        return new ArrayList<>(connected);
    }

    public List<Airport> getSortedConnections() {
        return connected.stream().sorted(Comparator.comparingDouble(Airport::getDistanceFromDestination)).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return identifier.equals(airport.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "identifier='" + identifier + '\'' +
                "distanceToDest='" + distanceFromDestination + '\'' +
                '}';
    }

    public void setDistanceFromDestination(double distanceFromDestination) {
        this.distanceFromDestination = distanceFromDestination;
    }

    public double getDistanceFromDestination() {
        return distanceFromDestination;
    }

    public String getIdentifier() {
        return identifier;
    }

}
