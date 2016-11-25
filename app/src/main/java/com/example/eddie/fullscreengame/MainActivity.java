package com.example.eddie.fullscreengame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button invisibleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int size = Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);

        Gameboard spelbrade = (Gameboard)findViewById(R.id.Gameboard);
        spelbrade.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        spelbrade.invalidate();
    }

    private void toastText(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
