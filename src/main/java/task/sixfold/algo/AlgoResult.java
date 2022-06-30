package task.sixfold.algo;

public class AlgoResult {
    public int numberOfAlternativeRoutes;
    public Route route;
    public int totalLegsGrown;
    public long timeSpendMillis;

    public AlgoResult(Route route) {
        this.route = route;
    }
}
