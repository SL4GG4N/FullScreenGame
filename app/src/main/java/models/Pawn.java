package models;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Pawn {
    private Drawable image;
    private float x,y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Pawn(Drawable image) {
        this.image = image;
    }

    public void draw(Canvas c){
        Rect bound = new Rect((int)x,(int)y,(int)x+image.getIntrinsicWidth()/30,(int)y+image.getIntrinsicHeight()/30);
        image.setBounds(bound);
        image.draw(c);
    }
}
