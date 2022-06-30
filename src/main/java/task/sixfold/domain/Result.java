package task.sixfold.domain;

import java.util.List;

public class Result {
    public List<String> route;
    public double distance;

    public Result(List<String> route, double distance) {
        this.route = route;
        this.distance = distance;
    }
}
