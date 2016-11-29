package controllers;

/**
 * Created by simonlundstrom on 26/11/16.
 */

public class LogicMessage {
    private int pawnToMove,moveTo, nextMove;
    private boolean blacksTurn;
    // MessageCodes
    public static final int PLACE_PAWN =1;
    public static final int REMOVE_PAWN=3;
    public static final int CHOOSE_PAWN=5;
    public static final int MOVE_PAWN=7;
    public static final int GAME_OVER=4;
    // ToFrom statements (0--23 {r platser p} br{det)
    public static final int FROM_WHITE_STASH = -2;
    public static final int FROM_BLACK_STASH = -3;
    public static final int TO_DISCARD_PILE = -4;
    public static final int HIGHLIGHT = -5;
    public static final int TO_STASH = -1;

    void setNextMove(int code) {
        nextMove =code;
    }

    public int getNextMove() {
        return nextMove;
    }

    public void setBlacksTurn(boolean blacksTurn) {
        this.blacksTurn = blacksTurn;
    }

    public boolean isBlacksTurn() {
        return blacksTurn;
    }

    public int getPawnToMove() {
        return pawnToMove;
    }

    public int getMoveTo() {
        return moveTo;
    }

    public LogicMessage(int pawnToMove, int moveTo) {
        this.pawnToMove = pawnToMove;
        this.moveTo = moveTo;
    }

    LogicMessage(int pawnToMove, int moveTo, int nextMove, boolean blacksTurn) {
        this.nextMove = nextMove;
        this.pawnToMove = pawnToMove;
        this.moveTo = moveTo;
        this.blacksTurn = blacksTurn;
    }
}