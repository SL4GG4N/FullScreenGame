package views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import com.example.eddie.fullscreengame.R;

import controllers.LogicMessage;
import models.NineMenMorrisModel;
import models.Point;
import views.PawnView;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class Gameboard extends View {
    private static NineMenMorrisModel model;
    private static Paint p = new Paint();
    // OBS! F|ljande siffror {r i enheten "tusendelar av sk}rmbredden"!
    // De ska ALLTID multipliceras med Math.min(getHeight(),getWidth())
    // och sen divideras med 1000. Gl|m inte!
    private static final int POINT_POSITION_TO_PIXEL = 125;
    private static final int CIRCLE_RADIUS = 20;
    private static final int CLICK_RADIUS= 60;
    private static final int Y_OFFSET_FOR_IMAGEVIEW = 214;
    private PawnView[] pawns;
    private static Drawable background;

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
        background.setBounds(0,0,getWidth(),getHeight());
        background.draw(canvas);
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
        p = new Paint();
        background = getResources().getDrawable(R.drawable.wooden_surface);
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
    public void animateMovement(ImageView obj, float x, float y) {
        float finalY = y+Y_OFFSET_FOR_IMAGEVIEW*Math.min(getWidth(),getHeight())/1000;
        ObjectAnimator xA = ObjectAnimator.ofFloat(obj,"x",x-obj.getWidth()/2);
        ObjectAnimator yA = ObjectAnimator.ofFloat(obj,"y",finalY-obj.getWidth()/2);
        AnimatorSet anime = new AnimatorSet();
        anime.playTogether(xA,yA);
        anime.setInterpolator(new AnticipateOvershootInterpolator());
        anime.setDuration(1000);
        anime.start();
        System.out.println("Animation startad?");
    }

    public boolean move(int fromPosition, int toPosition) {
        PawnView pawnToMove=null;
        if (fromPosition==LogicMessage.RESET_ALL) {
            int gameboardWidth=Math.min(getWidth(),getHeight());
            for (int i=0; i<pawns.length; i++) {
                animateMovement(pawns[i],gameboardWidth*0.05f+ (i % 9) * (int)(gameboardWidth*0.09),
                        (gameboardWidth*1.05f) + (i / 9) * (int)(gameboardWidth*0.09));
            }
            return true;
        }

        if (fromPosition==LogicMessage.HIGHLIGHT)
            return true;
        if (fromPosition== LogicMessage.FROM_WHITE_STASH) {
            for (int i=0;i<9;i++){
                if (pawns[i].getPlace()==-1) pawnToMove = pawns[i];
            }
        }
        else if (fromPosition==LogicMessage.FROM_BLACK_STASH) {
            for (int i=9; i<18;i++) {
                if (pawns[i].getPlace()==-1) pawnToMove = pawns[i];
            }
        }
        else {
            for (PawnView pawn : pawns) if (pawn.getPlace()==fromPosition) pawnToMove = pawn;
        }
        if (pawnToMove==null) return false;
        pawnToMove.setPlace(toPosition);
        if (toPosition == LogicMessage.TO_DISCARD_PILE) {
            animateMovement(pawnToMove,-100,-100);
        }
        else {
            animateMovement(pawnToMove, abspos(model.getPoint(toPosition).getX()), abspos(model.getPoint(toPosition).getY()));
        }
        return true;
    }

    public void setPawns(PawnView[] pawns) {
        this.pawns=pawns;
    }

    public void setModel(NineMenMorrisModel model) {
        this.model = model;
    }
}
