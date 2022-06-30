package task.sixfold.domain;

import task.sixfold.algo.AlgoResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Result {
    public int numberOfAlternativeRoutes;
    public int totalLegsGrown;
    public long timeSpendMillis;
    public List<String> route;
    public double distance;

    public static Result from(AlgoResult a) {
        Result r = new Result();
        r.numberOfAlternativeRoutes = a.numberOfAlternativeRoutes;
        r.totalLegsGrown = a.totalLegsGrown;
        r.timeSpendMillis = a.timeSpendMillis;
        if (a.route != null) {
            r.route = a.route.airports.stream().map(airport -> airport.identifier).collect(toList());
            r.distance = a.route.calculateDistance();
        }
        return r;
    }
}
