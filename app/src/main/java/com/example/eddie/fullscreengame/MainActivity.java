package com.example.eddie.fullscreengame;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import controllers.GameLogic;
import controllers.GameLogicException;
import controllers.LogicMessage;
import models.Pawn;
import models.Point;
import views.Gameboard;

public class MainActivity extends AppCompatActivity {

    public static Gameboard spelbrade;
    public static GameLogic logik;
    private TextView infoPanel;
    public ImageView[] pawnImages;
    boolean portrait;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        System.out.println("Saving stuff");
        outState.putSerializable("pawns", logik.getModel().getPawns());
        outState.putSerializable("points", logik.getModel().getPoints());
        outState.putSerializable("state", logik.getState());
        outState.putBoolean("blacksturn",logik.isBlacksTurn());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // Omforma br{det till en fyrkant och sätt dit lyssnare.
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int size = Math.min(displaymetrics.heightPixels, displaymetrics.widthPixels);
        spelbrade = (Gameboard) findViewById(R.id.Gameboard);
        spelbrade.setPar(this);
        spelbrade.getLayoutParams().height = size;
        spelbrade.getLayoutParams().width = size;
        spelbrade.invalidate();
        spelbrade.setOnTouchListener(new GameboardTouchListener());
        // Initialisera textf{ltet.
        infoPanel = (TextView) findViewById(R.id.infoPane);

        // Alla pj{ser, och s{tt dem i spelbr{det.
        pawnImages = new ImageView[18];
        pawnImages[0] = (ImageView) findViewById(R.id.pawn);
        for (int i = 0; i < 18; i++) {
            if (pawnImages[i] == null) pawnImages[i] = new ImageView(this);
            if (i < 9) pawnImages[i].setImageResource(R.drawable.white_pawn);
            else pawnImages[i].setImageResource(R.drawable.black_pawn);
            pawnImages[i].setLayoutParams(pawnImages[0].getLayoutParams());
            pawnImages[i].setMaxHeight((int) (size * 0.09));
            pawnImages[i].setMaxWidth((int) (size * 0.09));
            pawnImages[i].setAdjustViewBounds(true);
            if (i > 0) addContentView(pawnImages[i], pawnImages[i].getLayoutParams());
        }
        spelbrade.setPawns(pawnImages);

        // Knapp f|r nytt spel, eftersom vi inte har n}gon OptionsMenu.
        Button newGame;
        if (portrait) newGame = (Button) findViewById(R.id.menuP);
        else newGame = (Button) findViewById(R.id.menuL);
        newGame.setOnClickListener(new ClickNewGameListener());
        registerForContextMenu(newGame);

        // Kolla om spelet just laddats om fr}n att ha v{nt p} sig.
        if (savedInstanceState!=null) System.out.println("Pawns are " + savedInstanceState.getSerializable("pawns"));
        if (savedInstanceState != null && savedInstanceState.getSerializable("pawns") != null) {
            toastText("Reloading game.");
            logik = new GameLogic((Pawn[])savedInstanceState.getSerializable("pawns"),
                    (Point[]) savedInstanceState.getSerializable("points"),
                    (GameLogic.Gamestate) savedInstanceState.getSerializable("state"),
                    savedInstanceState.getBoolean("blacksturn"));

        } else {
            logik = new GameLogic();
            System.out.println("creating new Logic from scratch.");
        }
        // S{tt logiken och flytta pj{serna dit de ska.
        spelbrade.setModel(logik.getModel());
        spelbrade.move(pawnImages.length, 0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Menu");
        menu.add(0, v.getId(), 0, "new game");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "new game") {
            obeyLogic(logik.startNewGame());
        } else return false;
        return true;
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
            break;
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

        success = spelbrade.move(message.getPawnToMove(), message.getMoveTo());
        if (!success) infoPanel.setText("GAMEBOARD COULD NOT PERFORM TASK...");
        else infoPanel.setText(string.toString());
    }

    private class ClickNewGameListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            openContextMenu(view);
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