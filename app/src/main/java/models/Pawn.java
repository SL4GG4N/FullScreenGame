package models;

import java.io.Serializable;

/**
 * Created by simonlundstrom on 29/11/16.
 */

public class Pawn implements Serializable {
    int position;

    public Pawn() {
        position =-1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
