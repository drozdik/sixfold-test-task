package task.sixfold.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyAlgo {

    private Logger logger = LoggerFactory.getLogger(MyAlgo.class);

    public AlgoResult findShortestRoute(Airport start, Airport destination, List<Airport> allAirports) {
        List<Route> finishedRoutes = new ArrayList<>();
        Route currentShortest = null;

        long startTime = System.nanoTime();
        int legsGrown = 0;

        assignDistance(allAirports, destination);


        Route r = new Route(start);
        // before step back, store shortest distance from this node
        // when it finishes? when all routes stuck or finished(means reached destination
        // while route can grow or branch
        Airport prev = null;
        while (r.airports.size() > 0) {
            List<Airport> connections = r.getTip().getSortedConnections();
            Airport next = null;
            for (int i = prev != null ? connections.indexOf(prev) + 1 : 0; i < connections.size(); i++) {
                Airport option = connections.get(i);
                if (r.airports.contains(option)) {
                    continue;// avoid loop
                }
                // first optimization
                if (currentShortest != null && r.calculateDistance() + option.distanceFrom(destination) > currentShortest.calculateDistance()) {
                    continue;
                }
                next = option;
                break; // after next assigned
            }
            if (next != null) {
                r.addAirport(next);
                legsGrown++;
                prev = null;
            }
            if (next == null || next.equals(destination) || r.airports.size() == 5) {
                if (next != null && next.equals(destination)) {
                    Route route = r.copy();
                    finishedRoutes.add(route);
                    currentShortest = finishedRoutes.stream().sorted(Comparator.comparingDouble(Route::calculateDistance)).findFirst().get();
                    logger.debug("Finished route {}", route);
                }
                prev = r.pop();
            }
        }

        AlgoResult result = new AlgoResult(currentShortest);
        result.numberOfAlternativeRoutes = finishedRoutes.size();
        result.totalLegsGrown = legsGrown;
        result.timeSpendMillis = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        return result;
    }

    private void assignDistance(List<Airport> allAirports, Airport destination) {
        allAirports.forEach(airport -> {
            airport.setDistanceFromDestination(airport.distanceFrom(destination));
        });
    }
}
