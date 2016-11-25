package com.example.eddie.fullscreengame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Gameboard extends View {

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("View says: X="+getX());
        System.out.println("View says: Y="+getY());
        System.out.println("View says: X="+getHeight());
        System.out.println("View says: Y="+getWidth());
        super.onDraw(canvas);
    }

    public Gameboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Gameboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Gameboard(Context context) {
        super(context);
    }
}
