package com.example.eddie.fullscreengame;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import models.NineMenMorrisModel;
import models.Pawn;
import models.Point;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Gameboard extends View {
    private static NineMenMorrisModel model;
//    private static Pawn sidlogo;
    private static Paint p = new Paint();
    private ImageView sidlogo;
    // OBS! F|ljande siffror {r i enheten "tusendelar av sk}rmbredden"!
    // De ska ALLTID multipliceras med Math.min(getHeight(),getWidth())
    // och sen divideras med 1000. Gl|m inte!
    private static final int POINT_POSITION_TO_PIXEL = 125;
    private static final int CIRCLE_RADIUS = 20;
    private static final int CLICK_RADIUS= 60;
    private static final int Y_OFFSET_FOR_IMAGEVIEW = 235;

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("Gameboard drawing!");
        p.setColor(Color.GRAY);
        canvas.drawPaint(p);
        drawNineMenMorrisBoard(canvas);
//        sidlogo.draw(canvas);
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
        init();
    }

    public Gameboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Gameboard(Context context) {
        super(context);
        init();
    }

    private void init() {
        model=new NineMenMorrisModel();
        sidlogo = (ImageView)findViewById(R.id.imageView);
        p = new Paint();
    }

    public int validateClick(ImageView obj, int x, int y) {
        Point finger = new Point (x,y);
        System.out.println("Finger: "+finger);
        int CLICKRADIE = Math.min(getWidth(),getHeight())*CLICK_RADIUS/1000;
        for (Point p : model.getPoints()) {
            if (finger.distanceTo(abspos(p.getX()),abspos(p.getY())) < CLICKRADIE) {
                animateMovement(obj, abspos(p.getX()),abspos(p.getY()));
                return model.indexOf(p);
            }
        }
        return -1;
    }
    public void animateMovement(ImageView obj, float x, float y) {
        float finalY = y+Y_OFFSET_FOR_IMAGEVIEW*Math.min(getWidth(),getHeight())/1000;
        ObjectAnimator xA = ObjectAnimator.ofFloat(obj,"x",x);
        ObjectAnimator yA = ObjectAnimator.ofFloat(obj,"y",finalY);
        AnimatorSet anime = new AnimatorSet();
        anime.playTogether(xA,yA);
        anime.setInterpolator(new AnticipateOvershootInterpolator());
        anime.setDuration(1000);
        anime.start();
        System.out.println("Animation startad?");
    }
}
