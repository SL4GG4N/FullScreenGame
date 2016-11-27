package controllers;

/**
 * Created by simonlundstrom on 26/11/16.
 */

public class LogicMessage {
    private int messageCode,moveFrom,moveTo;
    public static final int WHITE_PLACE=1;
    public static final int BLACK_PLACE=2;
    public static final int WHITE_REMOVE=3;
    public static final int BLACK_REMOVE=4;
    public static final int WHITE_CHOOSE_PAWN=5;
    public static final int BLACK_CHOOSE_PAWN=6;
    public static final int WHITE_MOVE=7;
    public static final int BLACK_MOVE=8;
    public static final int ERROR_OCCUPIED_PLACE=9;
    public static final int ERROR_CHOOSE_PAWN=10;
    public static final int ERROR_GAME_IDLE=11;
    public static final int ERROR_UNKNOWN_STATE=12;

    public int getMessageCode() {
        return messageCode;
    }

    public int  getMoveFrom() {
        return moveFrom;
    }

    public int  getMoveTo() {
        return moveTo;
    }

    public LogicMessage(int messageCode, int moveFrom, int moveTo) {
        this.messageCode = messageCode;
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }
}