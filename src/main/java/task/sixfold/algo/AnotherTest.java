package task.sixfold.algo;

import java.util.List;

public class AnotherTest {
    public static void main(String[] args) {
/*
        Route.legsGrown = 0;
        find_route_with_one_leg();
        Route.legsGrown = 0;
        find_route_when_one_has_dead_end();
        Route.legsGrown = 0;
        find_shortest_route_between_two_valid_options();
        Route.legsGrown = 0;
        find_simplest_route_between_2_airports();
        Route.legsGrown = 0;
        big_test();
        Route.legsGrown = 0;
        when_closest_airport_not_connected_to_destination();
        Route.legsGrown = 0;
        everything_connected();
        Route.legsGrown = 0;
        something_connected();
*/
        everything_connected();
    }

    private static void find_simplest_route_between_2_airports() {
        // given
        Center center = new Center();
        Airport a = new Airport("a", new Coordinates(0, 0));
        Airport b = new Airport("b", new Coordinates(2, 0));
        a.addConnectionWith(b);
        // when
        Route route = center.findShortestRoute(a, b, List.of(a, b));
        // then
        if (new Route(a, b).equals(route) && route.calculateDistance() == 2) {
            System.out.println("Test_1 PASSED find_simplest_route_between_2_airports");
        } else {
            System.out.println("Test_1 FAILED find_simplest_route_between_2_airports");
        }
//        System.out.println("Expected:\n" + new Route(a, b));
//        System.out.println("Actual:\n" + route);
    }

    private static void find_route_with_one_leg() {
        // given
        Center center = new Center();
        Airport a = new Airport("a", new Coordinates(0, 0));
        Airport b = new Airport("b", new Coordinates(2, 0));
        Airport c = new Airport("c", new Coordinates(4, 0));
        a.addConnectionWith(b);
        b.addConnectionWith(c);
        // when
        Route route = center.findShortestRoute(a, c, List.of(a, b, c));
        // then
        Route expected = new Route(a, b, c);
        if (expected.equals(route) && route.calculateDistance() == 4) {
            System.out.println("Test_1 PASSED find_route_with_one_leg");
        } else {
            System.out.println("Test_1 FAILED find_route_with_one_leg");
        }
//        System.out.println("Expected:\n" + expected);
//        System.out.println("Actual:\n" + route);
    }

    private static void find_route_when_one_has_dead_end() {
        // given
        Center center = new Center();
        Airport a = new Airport("a", new Coordinates(0, 0));
        Airport b = new Airport("b", new Coordinates(2, 0));
        Airport c = new Airport("c", new Coordinates(4, 0));
        Airport d = new Airport("d", new Coordinates(2, 1));
        a.addConnectionWith(b);
        b.addConnectionWith(c);
        b.addConnectionWith(d);
        // when
        Route route = center.findShortestRoute(a, d, List.of(a, b, c, d));
        // then
        Route expected = new Route(a, b, d);
        if (expected.equals(route) && route.calculateDistance() == 3) {
            System.out.println("Test_1 PASSED find_route_when_one_has_dead_end");
        } else {
            System.out.println("Test_1 FAILED find_route_when_one_has_dead_end");
        }
//        System.out.println("Expected:\n" + expected);
//        System.out.println("Actual:\n" + route);
    }

    private static void find_shortest_route_between_two_valid_options() {
        // given
        Center center = new Center();
        Airport a = new Airport("a", new Coordinates(0, 0));
        Airport b = new Airport("b", new Coordinates(3, 0));
        Airport c = new Airport("c", new Coordinates(0, 2));
        Airport d = new Airport("d", new Coordinates(2, 2));
        a.addConnectionWith(b);
        b.addConnectionWith(d);

        a.addConnectionWith(c);
        c.addConnectionWith(d);
        // when
        Route route = center.findShortestRoute(a, d, List.of(a, b, c, d));
        // then
        Route expected = new Route(a, c, d);
        if (expected.equals(route) && route.calculateDistance() == 4) {
            System.out.println("Test_1 PASSED find_shortest_route_between_two_valid_options");
        } else {
            System.out.println("Test_1 FAILED find_shortest_route_between_two_valid_options");
        }
//        System.out.println("Expected:\n" + expected);
//        System.out.println("Actual:\n" + route);
    }

    private static void big_test() {
        // given
        Center center = new Center();

        var a = new Airport("a", new Coordinates(1, 1));

        var aa = new Airport("aa", new Coordinates(0, 0));
        var ab = new Airport("ab", new Coordinates(1, 2));
        var ac = new Airport("ac", new Coordinates(4, 0));
        var ad = new Airport("ad", new Coordinates(1, 4));

        var z = new Airport("z", new Coordinates(7, 8));
        var za = new Airport("za", new Coordinates(9, 9));
        var zb = new Airport("zb", new Coordinates(8, 7));
        var zc = new Airport("zc", new Coordinates(5, 8));

        var ma = new Airport("ma", new Coordinates(0, 7));
        var mb = new Airport("mb", new Coordinates(6, 5));
        var mc = new Airport("mc", new Coordinates(8, 3));

        a.addConnectionWith(aa, ab, ac);
        aa.addConnectionWith(ma, mb, mc);
        ab.addConnectionWith(ma, mb, mc);
        ac.addConnectionWith(ma, mb, mc);
        ad.addConnectionWith(ma, mb, mc);

        ma.addConnectionWith(za, zb, zc);
        mb.addConnectionWith(za, zb, zc);
        mc.addConnectionWith(za, zb, zc);

        za.addConnectionWith(z);
        zb.addConnectionWith(z);
        zc.addConnectionWith(z);
        // when
        Route route = center.findShortestRoute(a, z, List.of(a, aa, ab, ac, ad, ma, mb, mc, za, zb, zc, z));

        // then
        Route expected = new Route(a, ab, mb, zb, z);
        if (expected.equals(route) && route.calculateDistance() < 11.5 && route.calculateDistance() > 11) {
            System.out.println("Test_1 PASSED big_test");
        } else {
            System.out.println("Test_1 FAILED big_test");
        }
//        System.out.println("Expected:\n" + expected);
//        System.out.println("Actual:\n" + route);
    }

    private static void everything_connected() {
        // given
//        Center center = new Center();
        Center_V1 center = new Center_V1();

        var a = new Airport("a", new Coordinates(1, 1));
        var aa = new Airport("aa", new Coordinates(0, 0));
        var ab = new Airport("ab", new Coordinates(1, 2));
        var ac = new Airport("ac", new Coordinates(4, 0));
        var ad = new Airport("ad", new Coordinates(1, 4));
        var z = new Airport("z", new Coordinates(7, 8));
        var za = new Airport("za", new Coordinates(9, 9));
        var zb = new Airport("zb", new Coordinates(8, 7));
        var zc = new Airport("zc", new Coordinates(5, 8));
        var ma = new Airport("ma", new Coordinates(0, 7));
        var mb = new Airport("mb", new Coordinates(6, 5));
        var mc = new Airport("mc", new Coordinates(8, 3));

        a.addConnectionWith(aa, ab, ac, ad, ma, mb, mc, za, zb, zc, z);
        aa.addConnectionWith(a, ab, ac, ad, ma, mb, mc, za, zb, zc, z);
        ab.addConnectionWith(a, aa, ac, ad, ma, mb, mc, za, zb, zc, z);
        ac.addConnectionWith(a, aa, ab, ad, ma, mb, mc, za, zb, zc, z);
        ad.addConnectionWith(a, aa, ab, ac, ma, mb, mc, za, zb, zc, z);
        z.addConnectionWith(a, aa, ab, ac, ad, ma, mb, mc, za, zb, zc);
        za.addConnectionWith(a, aa, ab, ac, ad, ma, mb, mc, zb, zc, z);
        zb.addConnectionWith(a, aa, ab, ac, ad, ma, mb, mc, za, zc, z);
        zc.addConnectionWith(a, aa, ab, ac, ad, ma, mb, mc, za, zb, z);
        ma.addConnectionWith(a, aa, ab, ac, ad, mb, mc, za, zb, zc, z);
        mb.addConnectionWith(a, aa, ab, ac, ad, ma, mc, za, zb, zc, z);
        mc.addConnectionWith(a, aa, ab, ac, ad, ma, mb, za, zb, zc, z);
        // when
//        Route route = center.findShortestRoute(a, z, List.of(a, aa, ab, ac, ad, ma, mb, mc, za, zb, zc, z));
        Route route = center.findShortestRoute(a, z);

        // then
        if (route.calculateDistance() < 11.5) {
            System.out.println("Test_1 PASSED everything_connected");
        } else {
            System.out.println("Test_1 FAILED everything_connected");
        }
//        System.out.println("Expected:\n" + expected);
//        System.out.println("Actual:\n" + route);
        /*
        12 airports, all interconnected gives:
        TOTAL ROUTES BUILD: 11111
        TOTAL LEGS GROWN: 12221
         */

    }

    private static void when_closest_airport_not_connected_to_destination() {
        // given
        Center center = new Center();

        var a = new Airport("a", new Coordinates(0, 0));
        var b = new Airport("b", new Coordinates(2, 0));
        var c = new Airport("c", new Coordinates(2, 2));
        var z = new Airport("z", new Coordinates(3, 0));

        a.addConnectionWith(b);
        a.addConnectionWith(c);
        c.addConnectionWith(z);

        // when
        Route route = center.findShortestRoute(a, z, List.of(a, b, c, z));
        // then
        Route expected = new Route(a, c, z);
        if (expected.equals(route)) {
            System.out.println("Test_1 PASSED when_closest_airport_not_connected_to_destination");
        } else {
            System.out.println("Test_1 FAILED when_closest_airport_not_connected_to_destination");
        }
        System.out.println("Expected:\n" + expected);
        System.out.println("Actual:\n" + route);
    }

    private static void interconnected_square() {
        // given
        Center center = new Center();

        var a = new Airport("a", new Coordinates(0, 0));
        var b = new Airport("b", new Coordinates(2, 0));
        var c = new Airport("c", new Coordinates(2, 0));
        var z = new Airport("z", new Coordinates(2, 2));

        a.addConnectionWith(b, c, z);
        b.addConnectionWith(a, c, z);
        c.addConnectionWith(a, b, z);
        z.addConnectionWith(a, b, c);

        // when
        Route route = center.findShortestRoute(a, z, List.of(a, b, c, z));

        // then
        Route expected = new Route(a, z);
        if (expected.equals(route)) {
            System.out.println("Test_1 PASSED interconnected_square");
        } else {
            System.out.println("Test_1 FAILED interconnected_square");
        }
        System.out.println("Expected:\n" + expected);
        System.out.println("Actual:\n" + route);
    }

    private static void something_connected() {
        // given
        Center center = new Center();
//        Center_V1 center = new Center_V1();

        var a = new Airport("a", new Coordinates(1, 1));
        var aa = new Airport("aa", new Coordinates(0, 0));
        var ab = new Airport("ab", new Coordinates(1, 2));
        var ac = new Airport("ac", new Coordinates(4, 0));
        var ad = new Airport("ad", new Coordinates(1, 4));
        var z = new Airport("z", new Coordinates(7, 8));
//        var za = new Airport("za", new Coordinates(9, 9));
//        var zb = new Airport("zb", new Coordinates(8, 7));
//        var zc = new Airport("zc", new Coordinates(5, 8));
//        var ma = new Airport("ma", new Coordinates(0, 7));
//        var mb = new Airport("mb", new Coordinates(6, 5));
//        var mc = new Airport("mc", new Coordinates(8, 3));

        a.addConnectionWith(aa, ab, ac, ad, z);
        aa.addConnectionWith(a, ab, ac, ad, z);
        ab.addConnectionWith(a, aa, ac, ad, z);
        ac.addConnectionWith(a, aa, ab, ad, z);
        ad.addConnectionWith(a, aa, ab, ac, z);
        z.addConnectionWith(aa, ab, ac, ad);
        // when
        Route route = center.findShortestRoute(a, z, List.of(a, aa, ab, ac, ad, z));
    }
}
