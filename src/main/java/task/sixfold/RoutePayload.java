package task.sixfold;

import java.util.List;

public class RoutePayload {
    AirportPayload from, to;
    List<AirportPayload> route;

    public List<AirportPayload> getRoute() {
        return route;
    }

    public void setRoute(List<AirportPayload> route) {
        this.route = route;
    }

    public AirportPayload getFrom() {
        return from;
    }

    public void setFrom(AirportPayload from) {
        this.from = from;
    }

    public AirportPayload getTo() {
        return to;
    }

    public void setTo(AirportPayload to) {
        this.to = to;
    }
}
