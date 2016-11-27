package views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by simonlundstrom on 28/11/16.
 */

public class PawnView extends ImageView {
    private int place;

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public PawnView(Context context) {
        super(context);
        place=-1;
    }

    public PawnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        place=-1;
    }

    public PawnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        place=-1;
    }

    public PawnView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        place=-1;
    }
}
