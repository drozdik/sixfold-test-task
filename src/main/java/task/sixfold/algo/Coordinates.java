package task.sixfold.algo;

public class Coordinates {
    private int x, y;

    public Coordinates(int x, int y) {
/*
        if (x < 0 || y < 0 || x > 9 || y > 9) {
            throw new RuntimeException(String.format("Invalid coordinates for a grid 10x10 %d %d", x, y));
        }
*/
        this.x = x;
        this.y = y;
    }

    public double distanceFrom(Coordinates another) {
        double distance = Math.sqrt(
                Math.pow(this.x - another.x, 2) + Math.pow(this.y - another.y, 2)
        );
//        System.out.println(String.format("Calculated distance is %f", distance));
        return distance;
    }
}
