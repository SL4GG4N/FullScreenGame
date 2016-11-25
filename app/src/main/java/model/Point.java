package model;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Point{
    int x,y;
    Status status;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.status=Status.EMPTY;
    }
    public enum Status{
        EMPTY,WHITE,BLACK;
    }
}
