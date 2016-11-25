package com.example.eddie.fullscreengame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Gameboard extends View {

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        canvas.drawPaint(p);
        nineMenMorrisBoard(canvas);
        super.onDraw(canvas);
    }

    private void nineMenMorrisBoard(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(absPos(0.1),absPos(0.1),absPos(0.8),absPos(0.9),paint);
    }

    private int absPos(double relPos) {
        return (int)(relPos*getWidth());
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
