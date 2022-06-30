package task.sixfold.algo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MyAlgoTest {
    private MyAlgo myAlgo = new MyAlgo();

    @Test
    void two_nodes_connected() {
        // given
        Airport start = new Airport("foo", new Coordinates(1.0, 1.0, 0));
        Airport dest = new Airport("bar", new Coordinates(2.0, 2.0, 0));
        start.addConnectionWith(dest);
        List<Airport> allNodes = List.of(start, dest);

        // when
        AlgoResult result = myAlgo.findShortestRoute(start, dest, allNodes);

        // then
        assertThat(result.route.airports).isEqualTo(List.of(start, dest));
        assertThat(result.numberOfAlternativeRoutes).isEqualTo(1);
        assertThat(result.totalLegsGrown).isEqualTo(1);
    }

    @Test
    void three_nodes_one_alternate() {
        // given
        Airport start = new Airport("start", new Coordinates(1.0, 1.0, 0));
        Airport dest = new Airport("dest", new Coordinates(2.0, 2.0, 0));
        Airport third = new Airport("third", new Coordinates(2.0, 3.0, 0));
        start.addConnectionWith(dest, third);
        third.addConnectionWith(dest);
        List<Airport> allNodes = List.of(start, dest);

        // when
        AlgoResult result = myAlgo.findShortestRoute(start, dest, allNodes);

        // then
        assertThat(result.route.airports).isEqualTo(List.of(start, dest));
        assertThat(result.numberOfAlternativeRoutes).isEqualTo(2);
        assertThat(result.totalLegsGrown).isEqualTo(3);
    }

    @Test
    void two_nodes_disconnected() {
        // given
        Airport start = new Airport("foo", new Coordinates(1.0, 1.0, 0));
        Airport dest = new Airport("bar", new Coordinates(2.0, 2.0, 0));
//        start.addConnectionWith(dest);
        List<Airport> allNodes = List.of(start, dest);

        // when
        AlgoResult result = myAlgo.findShortestRoute(start, dest, allNodes);

        // then
        assertThat(result.route).isNull();
        assertThat(result.numberOfAlternativeRoutes).isEqualTo(0);
        assertThat(result.totalLegsGrown).isEqualTo(0);
    }
}
