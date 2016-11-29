package com.example.eddie.fullscreengame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import controllers.GameLogic;
import controllers.GameLogicException;
import controllers.LogicMessage;
import views.Gameboard;
import views.PawnView;

public class MainActivity extends AppCompatActivity {

    public static Gameboard spelbrade;
    public static GameLogic logik;
    private TextView infoPanel;
    public PawnView[] pawns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logik = new GameLogic();

        // Omforma br{det till en fyrkant och sätt dit lyssnare.
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int size = Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);
        int yOffset = (Math.max(displaymetrics.heightPixels,displaymetrics.widthPixels)-size)/2;
        System.out.println("Y-OFFSET="+yOffset);
        spelbrade = (Gameboard) findViewById(R.id.Gameboard);
        spelbrade.getLayoutParams().height = size;
        spelbrade.getLayoutParams().width = size;
        spelbrade.invalidate();
        spelbrade.setOnTouchListener(new GameboardTouchListener());

        // Initialisera textf{ltet.
        infoPanel = (TextView) findViewById(R.id.infoPane);

        // Alla pj{ser.
        pawns = new PawnView[18];
        pawns[0] = (PawnView) findViewById(R.id.pawn);
        for (int i = 0; i < 18; i++) {
            if (pawns[i] == null) pawns[i] = new PawnView(this);
            if (i < 9) pawns[i].setImageResource(R.drawable.white_pawn);
            else pawns[i].setImageResource(R.drawable.black_pawn);
            pawns[i].setLayoutParams(pawns[0].getLayoutParams());
            pawns[i].setMaxHeight((int)(size*0.09));
            pawns[i].setMaxWidth((int)(size*0.09));
            pawns[i].setAdjustViewBounds(true);
            if (i > 0) addContentView(pawns[i], pawns[i].getLayoutParams());
        }
        spelbrade.setPawns(pawns);
        spelbrade.setModel(logik.getModel());
        spelbrade.move(LogicMessage.RESET_ALL,LogicMessage.RESET_ALL);
    }

    // Tar emot svaren
    private void obeyLogic(LogicMessage message) {
        boolean success = true;
        StringBuilder string = new StringBuilder();
        if (message.isBlacksTurn()) string.append(getString(R.string.black_player));
        else string.append(getString(R.string.white_player));
        string.append(", ");
        switch (message.getNextMove()) {
            case LogicMessage.GAME_OVER: {
                string.append(getString(R.string.win));
            }
            case LogicMessage.PLACE_PAWN: {
                string.append(getString(R.string.place));
            }
            break;
            case LogicMessage.CHOOSE_PAWN: {
                string.append(getString(R.string.choose));
            }
            break;
            case LogicMessage.MOVE_PAWN: {
                string.append(getString(R.string.move));
            }
            break;
            case LogicMessage.REMOVE_PAWN: {
                string.append(getString(R.string.remove));
            }
            break;
            default:
                string.append("UNKNOWN NEXT MOVE...");
        }
        if (message.getNextMove()!=LogicMessage.GAME_OVER)
            success= spelbrade.move(message.getMoveFrom(), message.getMoveTo());
        if (!success) infoPanel.setText("GAMEBOARD COULD NOT PERFORM TASK...");
        else infoPanel.setText(string.toString());
    }

    /**
     * Overriding this Activity's onCreateOptionsMenu method. A ContextMenu for
     * managing To-DoItems (remove)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Overriding the Activity's onOptionsItemSelected method. This is where we
     * define what actions to take when a option menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_game:
                //TODO: restart game code here
                obeyLogic(logik.startNewGame());
                return true;
            default:
                // Other alternatives -> default behavior
                return super.onOptionsItemSelected(item);
        }
    }

    // Om n}gon klickar p} spelbr{det, valideras det av spelbr{det,
    // och om detta returnerar noll eller |ver, skickas det in i spellogiken.
    // Kanske skulle man skicka in det i spellogiken direkt, men jag vet inte.
    private class GameboardTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent me) {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                int p = spelbrade.validateClick((int) me.getX(), (int) me.getY());
//                infoPanel.setText("Du träffade " + p);
                if (p >= 0) {
                    System.out.println("MainActivity såg en träff på "+p);
                    try {
                        obeyLogic(logik.handleClick(p));
                    } catch (GameLogicException gle) {
                        toastText(gle.getMessage());
                    }
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