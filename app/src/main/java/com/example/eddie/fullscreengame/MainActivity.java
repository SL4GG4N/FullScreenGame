package com.example.eddie.fullscreengame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import controllers.GameController;

public class MainActivity extends AppCompatActivity {

    public static Gameboard spelbrade;
    public static TextView infoPanel;

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
    }

    private class GameboardTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent me) {
            if (me.getAction()==MotionEvent.ACTION_DOWN) {
                toastText("Du träffade "+spelbrade.validateClick((int)me.getX(),(int)me.getY()));
            }
            return true;
        }
    }

    private void toastText(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}