package com.example.eddie.fullscreengame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import controllers.GameLogic;
import controllers.LogicMessage;

public class MainActivity extends AppCompatActivity {

    public static Gameboard spelbrade;
    public static GameLogic logik;
    private TextView infoPanel;
    public ImageView[] pawns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logik = new GameLogic();

        // Omforma br{det till en fyrkant och sätt dit lyssnare.
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int size = Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);
        spelbrade = (Gameboard)findViewById(R.id.Gameboard);
        spelbrade.getLayoutParams().height = size;
        spelbrade.getLayoutParams().width = size;
        spelbrade.invalidate();
        spelbrade.setOnTouchListener(new GameboardTouchListener());

        // Initialisera textf{ltet.
        infoPanel = (TextView)findViewById(R.id.infoPane);
        infoPanel.setText("Här ska det stå nåt");

        // Alla pj{ser.
        pawns = new ImageView[18];
        pawns[0]= (ImageView)findViewById(R.id.pawn);
        for (int i=0; i<18; i++) {
            if (pawns[i]==null) pawns[i]= new ImageView(this);
            pawns[i].setImageResource(R.drawable.sidlogga);
            pawns[i].setLayoutParams(pawns[0].getLayoutParams());
            pawns[i].setX((i%9)*100);
            pawns[i].setY(1300+(i/9)*100);
            pawns[i].setMaxHeight(100);
            pawns[i].setMaxWidth(100);
            pawns[i].setAdjustViewBounds(true);
            if (i>0) addContentView(pawns[i],pawns[i].getLayoutParams());
        }
        spelbrade.setPawns(pawns);
        spelbrade.setModel(logik.getModel());
    }

    // Tar emot svaren
    private void obeyLogic(LogicMessage message) {
        switch (message.getMessageCode()) {
            case LogicMessage.ERROR_GAME_IDLE: infoPanel.setText(getResources().getString(R.string.game_idle));
            break;
        }
    }

    // Om n}gon klickar p} spelbr{det, valideras det av spelbr{det,
    // och om detta returnerar noll eller |ver, skickas det in i spellogiken.
    // Kanske skulle man skicka in det i spellogiken direkt, men jag vet inte.
    private class GameboardTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent me) {
            if (me.getAction()==MotionEvent.ACTION_DOWN) {
                int p = spelbrade.validateClick((int)me.getX(),(int)me.getY());
                infoPanel.setText("Du träffade "+ p);
                if (p>=0) {
                    System.out.println("MainActivity såg en träff.");
                    obeyLogic(logik.handleClick(p));
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