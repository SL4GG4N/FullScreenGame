package models;

import java.io.Serializable;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Point implements Serializable{
    private int x,y;

    public Status status;

    public Status getStatus() {
        return status;
    }

    public boolean isEmpty() {
        return (status==Status.EMPTY);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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

    public enum Status implements Serializable{
        EMPTY,WHITE,BLACK;
    }
}
