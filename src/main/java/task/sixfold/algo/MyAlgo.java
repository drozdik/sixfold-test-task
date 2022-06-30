package task.sixfold.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyAlgo {
    public List<Route> routes = new ArrayList<>();
    public List<Route> finishedRoutes = new ArrayList<>();
    Logger logger = LoggerFactory.getLogger(MyAlgo.class);
    private Airport destination;
    private Route currentShortest;

    // sooo, 1 cut off unnecessary routes, multiple tactics can be implemented >> again how to be fully sure that they were indeed unnecessary
    // encourage 'grow' routes with better chance first, but how to know when to stop checking others?, that there no more viable/better options
    // i have my ideas, я тут не собираюсь придумать лучший в мире алгоритм - просто загугли его и делов то
    // идея в том чтобы поиграться, но играть хочеться честно, по полной
    // start from both sides?
    // 7,5k airports, посчитать дистанцию каждого аэропорта от финишного аэропорта.. это и будет наше "свечение"
//        12 airports, all interconnected gives:
//    TOTAL ROUTES BUILD: 11111
//    TOTAL LEGS GROWN: 12221
    // ok , calculate distance of all AirportNodes when destination is known
    // then from starting node grow route, but not branch it on every other node, try reach destination
    // or fail, then step back and pick another route
    // можно отметать другие ветви по "предсказанию" дистанции если "отклонился слишком сильно" и нет шанса переплюнуть
    // существующий тогда нет смысла дальше отращивать эту ветку
    public Route findShortestRoute(Airport start, Airport destination, List<Airport> allAirports) {
        long startTime = System.nanoTime();

        // reset
        this.routes = new ArrayList<>();
        this.finishedRoutes = new ArrayList<>();
        this.destination = destination;
        assignDistance(allAirports);


        Route r = new Route(start);
        routes.add(r);
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
                prev = null;
            }
            if (next == null || next.equals(destination) || r.airports.size() == 5) {
                if (next != null && next.equals(destination)) {
                    Route route = r.copy();
                    finishedRoutes.add(route);
                    currentShortest = finishedRoutes.stream().sorted(Comparator.comparingDouble(Route::calculateDistance)).findFirst().get();
                    logger.info("Finished route {}", route);
                }
                routes.add(r.copy());
                prev = r.pop();
            }
        }
        logger.info("Total branched routes {}", routes.size());

        return finishedRoutes.stream().sorted(Comparator.comparingDouble(route -> route.calculateDistance())).findFirst().orElseThrow(() -> new RuntimeException("None reached destination"));
    }

    private void assignDistance(List<Airport> allAirports) {
        allAirports.forEach(airport -> {
            airport.setDistanceFromDestination(airport.distanceFrom(this.destination));
        });
    }
}
