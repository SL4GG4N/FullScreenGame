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
        paint.setStrokeWidth(4);
        // TODO: 25/11/16 Automatisera 
        drawLine(canvas,1,1,7,1,paint);
        drawLine(canvas,7,1,7,7,paint);
        drawLine(canvas,1,1,1,7,paint);
        drawLine(canvas,1,7,7,7,paint);
        drawLine(canvas,2,2,6,2,paint);
        drawLine(canvas,2,6,6,6,paint);
        drawLine(canvas,2,2,2,6,paint);
        drawLine(canvas,6,2,6,6,paint);
        drawLine(canvas,3,3,5,3,paint);
        drawLine(canvas,5,3,5,5,paint);
        drawLine(canvas,3,3,3,5,paint);
        drawLine(canvas,3,5,5,5,paint);
        drawLine(canvas,4,1,4,3,paint);
        drawLine(canvas,4,7,4,5,paint);
        drawLine(canvas,1,4,3,4,paint);
        drawLine(canvas,5,4,7,4,paint);
        float radius = Math.min(getWidth(),getHeight())*0.02f;
        drawCircle(canvas,1,1,radius,paint);
        drawCircle(canvas,4,1,radius,paint);
        drawCircle(canvas,7,1,radius,paint);
        drawCircle(canvas,2,2,radius,paint);
        drawCircle(canvas,4,2,radius,paint);
        drawCircle(canvas,6,2,radius,paint);
        drawCircle(canvas,3,3,radius,paint);
        drawCircle(canvas,4,3,radius,paint);
        drawCircle(canvas,5,3,radius,paint);
        drawCircle(canvas,1,4,radius,paint);
        drawCircle(canvas,2,4,radius,paint);
        drawCircle(canvas,3,4,radius,paint);
        drawCircle(canvas,5,4,radius,paint);
        drawCircle(canvas,6,4,radius,paint);
        drawCircle(canvas,7,4,radius,paint);
        drawCircle(canvas,3,5,radius,paint);
        drawCircle(canvas,4,5,radius,paint);
        drawCircle(canvas,5,5,radius,paint);
        drawCircle(canvas,2,6,radius,paint);
        drawCircle(canvas,4,6,radius,paint);
        drawCircle(canvas,6,6,radius,paint);
        drawCircle(canvas,1,7,radius,paint);
        drawCircle(canvas,4,7,radius,paint);
        drawCircle(canvas,7,7,radius,paint);
    }

    private void drawLine(Canvas c, int fromX, int fromY, int toX, int toY, Paint p) {
        c.drawLine(abspos(fromX),abspos(fromY),abspos(toX),abspos(toY),p);
    }
    private void drawCircle(Canvas c, int x, int y, float rad, Paint p) {
        c.drawCircle(abspos(x),abspos(y),rad, p);
    }
    private int abspos(int relpos) {
        return Math.min(getWidth(),getHeight())*relpos*125/1000;
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
