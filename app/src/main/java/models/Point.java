package models;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Point{
    private int x,y;
    public Status status;

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

    public float distanceTo(int x, int y) {
        return (float)Math.sqrt(Math.pow(this.x-x,2)+Math.pow(this.y-y,2));
    }

    public String toString() {
        return "("+x+";"+y+")";
    }

    public enum Status{
        EMPTY,WHITE,BLACK;
    }
}
