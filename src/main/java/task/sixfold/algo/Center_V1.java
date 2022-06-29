package task.sixfold.algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Center_V1 {
    public List<Route> routes = new ArrayList<>();
    private Airport destination;

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
    public Route findShortestRoute(Airport start, Airport destination) {
        this.destination = destination;
        Route r = new Route(start);
        routes.add(r);

        while (thereIsStuffToDo()) {
            for (Route route : unfinishedRoutes()) {
                Airport next = route.getTip().getConnections().stream()
                        .filter(airport -> !route.airports.contains(airport))
                        .collect(Collectors.toList())
                        .get(0);

                route.getTip().getConnections().stream().filter(airport -> !airport.equals(next) && !route.airports.contains(airport)).forEach(airport -> {
                    Route branchRoute = new Route(route.airports.toArray(new Airport[route.airports.size()]));
                    branchRoute.addAirport(airport);
                    routes.add(branchRoute);
                });

                route.addAirport(next);
            }
        }
        System.out.println("TOTAL ROUTES BUILD: " + routes.size());
        System.out.println("TOTAL LEGS GROWN: " + Route.legsGrown);
        List<Route> finishedSortedRoutes = routes.stream().filter(route -> route.getTip().equals(destination)).sorted(Comparator.comparingDouble(Route::calculateDistance)).collect(Collectors.toList());
        finishedSortedRoutes.forEach(route -> {
            System.out.println(String.format("Finised route: %s", route));
        });
        System.out.println(String.format("Total finished routes: %d", finishedSortedRoutes.size()));
        return routes.stream()
                .filter(route -> route.getTip() != null && route.getTip().equals(destination))
                .sorted(Comparator.comparing(s -> s.calculateDistance()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("None reached destination"));

    }

    private List<Route> unfinishedRoutes() {
        return routes.stream()
                .filter(route ->
                        route.getTip() != null &&
                                !route.getTip().equals(destination) &&
                                !route.getTip().getConnections().isEmpty() &&
                                route.airports.size() < 5)
                .collect(Collectors.toList());
    }

    // is there any route which is not reached destination yet has options to connect with
    private boolean thereIsStuffToDo() {
        return !unfinishedRoutes().isEmpty();
    }

}
