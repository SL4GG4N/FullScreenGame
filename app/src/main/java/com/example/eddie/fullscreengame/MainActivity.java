package com.example.eddie.fullscreengame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static Gameboard spelbrade;
    public TextView infoPanel;
    public ImageView sidView;
    public ImageView sidView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int size = Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);

        spelbrade = (Gameboard)findViewById(R.id.Gameboard);
        spelbrade.getLayoutParams().height = size;
        spelbrade.getLayoutParams().width = size;
        spelbrade.invalidate();
        spelbrade.setOnTouchListener(new GameboardTouchListener());
        infoPanel = (TextView)findViewById(R.id.textView);
        infoPanel.setText("Här ska det stå nåt");
        sidView = (ImageView)findViewById(R.id.imageView);
        sidView.setX(0);
        sidView.setY(0);
        sidView.setMaxHeight(100);
        sidView.setMaxWidth(100);
        sidView.setAdjustViewBounds(true);
        sidView2 = new ImageView(this);
        sidView2.setImageResource(R.drawable.sidlogga);
        sidView2.setLayoutParams(sidView.getLayoutParams());
        sidView2.setX(10);
        sidView2.setY(10);
        sidView2.setMaxHeight(100);
        sidView2.setMaxWidth(100);
        sidView2.setAdjustViewBounds(true);
        addContentView(sidView2,sidView2.getLayoutParams());

    }

    private class GameboardTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent me) {
            if (me.getAction()==MotionEvent.ACTION_DOWN) {
                int p = spelbrade.validateClick(sidView, (int)me.getX(),(int)me.getY());
                infoPanel.setText("Du träffade "+ p);
                if (p>=0) {
                    System.out.println("MainActivity såg en träff.");
                }
            }
            return true;
        }
    }

    private void toastText(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}