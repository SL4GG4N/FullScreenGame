package com.example.eddie.fullscreengame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import models.NineMenMorrisModel;
import models.Point;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Gameboard extends View {
    private static NineMenMorrisModel model;
    // OBS! F|ljande siffror {r i enheten "tusendelar av sk}rmbredden"!
    // De ska ALLTID multipliceras med Math.min(getHeight(),getWidth())
    // och sen divideras med 1000. Gl|m inte!
    private static final int POINT_POSITION_TO_PIXEL = 125;
    private static final int CIRCLE_RADIUS = 20;
    private static final int CLICK_RADIUS= 60;

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        canvas.drawPaint(p);
        drawNineMenMorrisBoard(canvas);
        super.onDraw(canvas);
    }

    private void drawNineMenMorrisBoard(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        // Draw the outline
        float[] outline = {0,0,getWidth(),0,
                           getWidth(),0,getWidth(),getHeight(),
                           getWidth(),getHeight(),0,getHeight(),
                           0,getHeight(),0,0};
        paint.setStrokeWidth(16);
        canvas.drawLines(outline,paint);
        paint.setStrokeWidth(4);
        for (int j = 0; j < 3; j++) {
            for (int i=0; i<24; i+=6) {
                canvas.drawLine(abspos(model.getPoint(i+j).getX()),
                                abspos(model.getPoint(i+j).getY()),
                                abspos(model.getPoint((i+j+6)%24).getX()),
                                abspos(model.getPoint((i+j+6)%24).getY()),
                                paint);
            }
        }
        for (int j=3; j<24; j+=6) {
            canvas.drawLine(abspos(model.getPoint(j).getX()),
                            abspos(model.getPoint(j).getY()),
                            abspos(model.getPoint(j+2).getX()),
                            abspos(model.getPoint(j+2).getY()),
                            paint);
        }
        float radius = Math.min(getWidth(), getHeight()) * CIRCLE_RADIUS / 1000;
        for (Point p : model.getPoints()) {
            canvas.drawCircle(abspos(p.getX()), abspos(p.getY()), radius, paint);
        }
    }

    private int abspos(int relpos) {
        return Math.min(getWidth(), getHeight()) * relpos * POINT_POSITION_TO_PIXEL / 1000;
    }

    public Gameboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = new NineMenMorrisModel();
    }

    public Gameboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        model = new NineMenMorrisModel();
    }

    public Gameboard(Context context) {
        super(context);
        model = new NineMenMorrisModel();
    }

    public int validateClick(int x, int y) {
        Point finger = new Point (x,y);
        System.out.println("Finger: "+finger);
        int CLICKRADIE = Math.min(getWidth(),getHeight())*CLICK_RADIUS/1000;
        for (Point p : model.getPoints()) {
            if (finger.distanceTo(abspos(p.getX()),abspos(p.getY())) < CLICKRADIE) {
                return model.indexOf(p);
            }
        }
        return -1;
    }
}
