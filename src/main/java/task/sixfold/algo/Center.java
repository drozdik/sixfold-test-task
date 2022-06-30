package task.sixfold.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.sixfold.domain.AirportIdentifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Center {
    public List<Route> routes = new ArrayList<>();
    public List<Route> finishedRoutes = new ArrayList<>();
    Logger logger = LoggerFactory.getLogger(Center.class);
    private Airport destination;
    private Route currentShortest;
    private Route lastActive;

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
        double shortestPathDistance = 0.0; // not sure


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
                //prev.setShortestPathDistance(shortestPathDistance); not sure about this yet
            }
        }
        logger.info("Total branched routes {}", routes.size());


        /////////////////////////////////////////
/*
        boolean canGrowOrBranch = true;
        while (canGrowOrBranch) {

            r.addAirport(r.getTip().getClosestToDestinationConnection());

            // need to branch
            boolean reachedDestination = r.getTip().equals(destination);
            boolean stuck = r.getTip().getConnections().isEmpty();
            boolean grewMaxLegs = r.airports.size() == 3;
            if (grewMaxLegs || stuck || reachedDestination) {
                routes.add(r.copy());

                // step back and search for option
                Airport next = null;
                while (next == null) {

                    if (r.getTip().equals(start)) {
                        canGrowOrBranch = false;
                        break;
                    }

                    // leave best route from here to destination
                    Airport prev = r.pop();

                    List<Airport> connections = r.getTip().getSortedConnections();
                    for (int i = connections.indexOf(prev) + 1; i < connections.size(); i++) {

                        Airport option = connections.get(i);
                        boolean willCreateLoop = r.airports.contains(option);
                        if (willCreateLoop) {
                            continue;
                        }

                        next = option;
                        break;
                    }

                }
                if (next != null) {
                    r.addAirport(next);
                }

            }
        }
*/
        return finishedRoutes.stream().sorted(Comparator.comparingDouble(route -> route.calculateDistance())).findFirst().orElseThrow(() -> new RuntimeException("None reached destination"));
    }
    // branch or grow further

    // if reachedDestination
    // if stuck // no connections further
    // grewMaxLegs
/*
            unfinishedRoutes().forEach(route -> {
                lastActive = route;
                Airport next = route.getTip().getSortedConnections().get(0); // with the least distance to destination
                route.addAirport(next);
                checkForShortest();
                if (route.getTip().equals(destination)) {
                    logger.info("FOUND NEW VALID ROUTE {}", route);
                }
                // stuck or finished
                if (route.getTip().equals(destination) || route.airports.size() == 5 || route.getTip().getConnections().isEmpty()) {
                    Route branch = route.copy();
                    boolean branchCreated = false;
                    while (branch.airports.size() != 1 && !branchCreated) {
                        Airport prev = branch.pop();
//                        if (prev.equals(destination)) {
//                            continue; // two steps right away, won't find alternative route from one step back
//                        }
//                        if (lastLegLeft(branch) && !connectedToDestination(branch.getTip())) {
//                            continue; // step back
//                        }
                        if (wasLastSortedOption(branch, prev)) {
                            continue; // step back
                        }

                        List<Airport> connections = branch.getTip().getSortedConnections();

                        for (int i = connections.indexOf(prev) + 1; i < connections.size(); i++) {
                            boolean willCreateLoop = branch.airports.contains(connections.get(i));
                            if (willCreateLoop) {
                                continue;
                            }
                            Airport nextForBranch = connections.get(i);
                            if (lastLegLeft(branch) && !nextForBranch.equals(destination)) {
                                break;
                            }
                            if (!nextForBranch.equals(destination) && nextForBranch.getConnections().isEmpty()) {
                                continue;
                            }
                            // can't be shorter than shortest so far, narrowed to 30 routes and 59 legs
                            if (currentShortest != null) {
                                double expectedDistance = branch.calculateDistance() + branch.getTip().distanceFrom(nextForBranch);
                                if (expectedDistance > currentShortest.calculateDistance()) {
                                    continue;
                                }
                            }
                            branch.addAirport(nextForBranch);
                            if (routes.contains(branch)) {
                                logger.warn("DUPLICATE ROUTE");
                            }

                            routes.add(branch);


                            checkForShortest();
                            if (branch.getTip().equals(destination)) {
                                logger.info("FOUND NEW VALID ROUTE {}", branch);
                            }
                            branchCreated = true;
                            break;
                        }
                    }
                }
            });
        }

        System.out.println("TOTAL ROUTES BUILD: " + routes.size());
        System.out.println("TOTAL LEGS GROWN: " + Route.legsGrown);
        Route shortestRoute = routes.stream()
                .filter(route -> route.getTip() != null && route.getTip().equals(destination))
                .sorted(Comparator.comparing(s -> s.calculateDistance()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("None reached destination"));
        System.out.println(String.format("Shortest route: %s", shortestRoute));
        List<Route> finisehdSortedRoutes = routes.stream().filter(route -> route.getTip().equals(destination)).sorted(Comparator.comparingDouble(Route::calculateDistance)).collect(Collectors.toList());
        finisehdSortedRoutes.forEach(route -> {
            System.out.println(String.format("Finised route: %s", route));
        });
        System.out.println(String.format("Total finished routes: %d", finisehdSortedRoutes.size()));
        long algoTime = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        logger.info("Found route within {} ms", algoTime);
        return shortestRoute;
*/


    private void checkForShortest() {
        this.currentShortest = this.routes.stream().filter(route -> route.getTip().equals(destination)).sorted(Comparator.comparingDouble(Route::calculateDistance)).findFirst().orElse(null);
    }

    private boolean wasLastSortedOption(Route route, Airport airport) {
        return route.getTip().getSortedConnections().indexOf(airport) == route.getTip().getSortedConnections().size() - 1;
    }

    private boolean lastLegLeft(Route route) {
        return route.airports.size() == 4;
    }

    private boolean connectedToDestination(Airport airport) {
        return airport.getConnections().contains(destination);
    }

    private boolean hasFinishedRoutes(int i) {
        return routes.stream().filter(route -> route.getTip().equals(destination)).collect(Collectors.toList()).size() >= i;
    }

    private void assignDistance(List<Airport> allAirports) {
        allAirports.forEach(airport -> {
            airport.setDistanceFromDestination(airport.distanceFrom(this.destination));
        });
    }

    private List<Route> unfinishedRoutes() {
        return routes.stream()
                .filter(route ->
                        route.getTip() != null &&
                                !route.getTip().equals(destination) &&
                                !route.getTip().getConnections().isEmpty() &&// this stops everythin earlier as expected
                                route.airports.size() != 5)
                .collect(Collectors.toList());
    }

    // is there any route which is not reached destination yet has options to connect with
    private boolean thereIsStuffToDo() {
        return !unfinishedRoutes().isEmpty();
    }

}
