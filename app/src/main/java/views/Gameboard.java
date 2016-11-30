package views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import com.example.eddie.fullscreengame.R;

import controllers.LogicMessage;
import models.NineMenMorrisModel;
import models.Point;

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
    private static final int CLICK_RADIUS = 60;
    private static final int Y_OFFSET_FOR_IMAGEVIEW = 357;
    private ImageView[] pawnImages;
    private static Drawable background;
    private static int offset;
    private Activity par;

    @Override
    protected void onDraw(Canvas canvas) {
        p.setColor(Color.GRAY);
        canvas.drawPaint(p);
        drawNineMenMorrisBoard(canvas);
//        sidlogo.draw(canvas);
        super.onDraw(canvas);
    }

    private void drawNineMenMorrisBoard(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        background.setBounds(0, 0, getWidth(), getHeight());
        background.draw(canvas);
        // Draw the outline
        float[] outline = {0, 0, getWidth(), 0,
                getWidth(), 0, getWidth(), getHeight(),
                getWidth(), getHeight(), 0, getHeight(),
                0, getHeight(), 0, 0};
        paint.setStrokeWidth(16);
        canvas.drawLines(outline, paint);
        paint.setStrokeWidth(4);
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 24; i += 6) {
                canvas.drawLine(abspos(model.getPoint(i + j).getX()),
                        abspos(model.getPoint(i + j).getY()),
                        abspos(model.getPoint((i + j + 6) % 24).getX()),
                        abspos(model.getPoint((i + j + 6) % 24).getY()),
                        paint);
            }
        }
        for (int j = 3; j < 24; j += 6) {
            canvas.drawLine(abspos(model.getPoint(j).getX()),
                    abspos(model.getPoint(j).getY()),
                    abspos(model.getPoint(j + 2).getX()),
                    abspos(model.getPoint(j + 2).getY()),
                    paint);
        }
        float radius = Math.min(getWidth(), getHeight()) * CIRCLE_RADIUS / 1000;
        for (Point p : model.getPoints()) {
            canvas.drawCircle(abspos(p.getX()), abspos(p.getY()), radius, paint);
        }

    }

    private int abspos(int relpos) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        par.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int size = Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);
        return size * relpos * POINT_POSITION_TO_PIXEL / 1000;
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
        Point finger = new Point(x, y);
        int CLICKRADIE = Math.min(getWidth(), getHeight()) * CLICK_RADIUS / 1000;
        for (Point p : model.getPoints()) {
            if (finger.distanceTo(abspos(p.getX()), abspos(p.getY())) < CLICKRADIE) {
                return model.getPointIndex(p);
            }
        }
        return -1;
    }

    public void animateMovement(ImageView obj, float x, float y) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        par.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int shortside= Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);
        int offset = (Math.max(displaymetrics.heightPixels, displaymetrics.widthPixels) - shortside) / 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            y+=offset;
        else
            x += offset;
        System.out.println("Moving something to "+x+";"+y+")");
        ObjectAnimator xA = ObjectAnimator.ofFloat(obj, "x", x - obj.getWidth() / 2);
        ObjectAnimator yA = ObjectAnimator.ofFloat(obj, "y", y - obj.getWidth() / 2);
        AnimatorSet anime = new AnimatorSet();
        anime.playTogether(xA, yA);
        anime.setInterpolator(new AnticipateOvershootInterpolator());
        anime.setDuration(1000);
        anime.start();
    }

    public boolean move(int pawnToMove, int toPosition) {
        if (pawnToMove == model.getPawns().length) {

            // M}ste r{kna ut dessa detaljer h}r, f|r att Android tycker "because fuck you".

            DisplayMetrics displaymetrics = new DisplayMetrics();
            par.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int gameboardWidth = Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);

//            System.out.println("Gameboard moving all!"+gameboardWidth+")");
            for (int i = 0; i < pawnImages.length; i++) {
                int oldPosition = model.getPawn(i).getPosition();
//                System.out.println("Gameboard moving pawn "+i+" to "+oldPosition);
                if (oldPosition == LogicMessage.TO_STASH) {
//                    System.out.println("Moving pawn "+i+" to stash!");
                    animateMovement(pawnImages[i], gameboardWidth * 0.05f + (i % 9) * (int) (gameboardWidth * 0.09),
                            (gameboardWidth * 1.05f) + (i / 9) * (int) (gameboardWidth * 0.09));
                } else if (oldPosition==LogicMessage.TO_DISCARD_PILE) {
//                    System.out.println("Moving pawn "+i+" to hell!");
                    animateMovement(pawnImages[i], -100, -100);
                }
                else {
                    Point p = model.getPoint(model.getPawn(i).getPosition());
//                    System.out.println("Moving pawn "+i+" to "+p);
                    animateMovement(pawnImages[i], abspos(p.getX()), abspos(p.getY()));
                }
            }
            return true;
        }
        if (pawnToMove == -1) return false;
        if (toPosition == LogicMessage.HIGHLIGHT) return true;
        if (toPosition == LogicMessage.TO_DISCARD_PILE) {
            animateMovement(pawnImages[pawnToMove], -100, -100);
        } else {
            animateMovement(pawnImages[pawnToMove],
                            abspos(model.getPoint(toPosition).getX()),
                            abspos(model.getPoint(toPosition).getY()));
        }
        return true;
    }
    public void setPar(Activity context) {
        par = context;
    }

    public void setPawns(ImageView[] pawns) {
        this.pawnImages = pawns;
    }

    public void setModel(NineMenMorrisModel model) {
        this.model = model;
    }
}
