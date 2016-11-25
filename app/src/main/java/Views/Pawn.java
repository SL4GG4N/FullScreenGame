package Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.Button;

/**
 * Created by Eddie on 2016-11-22.
 */

public class Pawn extends Button{
    public Pawn(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(50,50,50, new Paint());
    }
}
