package controllers;

/**
 * Created by simonlundstrom on 26/11/16.
 */

public class LogicMessage {
    private int moveFrom,moveTo, nextMove;
    private boolean blacksTurn;
    // MessageCodes
    public static final int PLACE_PAWN =1;
    public static final int REMOVE_PAWN=3;
    public static final int CHOOSE_PAWN=5;
    public static final int MOVE_PAWN=7;
    // ToFrom statements (0--23 {r platser p} br{det)
    public static final int FROM_WHITE_STASH = -2;
    public static final int FROM_BLACK_STASH = -3;
    public static final int TO_DISCARD_PILE = -4;
    public static final int HIGHLIGHT = -5;

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

    public int  getMoveFrom() {
        return moveFrom;
    }

    public int  getMoveTo() {
        return moveTo;
    }

    public LogicMessage(int moveFrom, int moveTo) {
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }

    LogicMessage(int moveFrom, int moveTo, int nextMove, boolean blacksTurn) {
        this.nextMove = nextMove;
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
        this.blacksTurn = blacksTurn;
    }
}