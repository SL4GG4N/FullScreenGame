package controllers;

import models.NineMenMorrisModel;
import models.Point;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class GameLogic {

    private Gamestate state;
    private NineMenMorrisModel model;
    private boolean blacksTurn;
    private boolean blackFlying;
    private boolean whiteFlying;
    private int gameCounter;
    private int placeToMoveFrom;

    public GameLogic() {
        model = new NineMenMorrisModel();
        state = Gamestate.IDLE;
    }

    public NineMenMorrisModel getModel() {
        return model;
    }

    public LogicMessage startNewGame() {
        state = Gamestate.PLACING_PAWNS;
        gameCounter = 0;
        blacksTurn = false;
        return new LogicMessage(-1, -1, LogicMessage.PLACE_PAWN, blacksTurn);
    }

    //ALL spellogik
    public LogicMessage handleClick(int p) throws GameLogicException {
        LogicMessage returnMessage = null;
        switch (state) {
            case IDLE:
                throw new GameLogicException("No game running.");
            case PLACING_PAWNS: {
                if (!model.getPoint(p).isEmpty()) throw new GameLogicException("Occupied place.");
                else {
                    gameCounter++;
                    if (blacksTurn) {
                        returnMessage = new LogicMessage(LogicMessage.FROM_BLACK_STASH, p);
                        model.getPoint(p).setStatus(Point.Status.BLACK);
                    } else {
                        returnMessage = new LogicMessage(LogicMessage.FROM_WHITE_STASH, p);
                        model.getPoint(p).setStatus(Point.Status.WHITE);
                    }
                    if (gameCounter < 18) returnMessage.setNextMove(LogicMessage.PLACE_PAWN);
                    else {
                        returnMessage.setNextMove(LogicMessage.CHOOSE_PAWN);
                        state = Gamestate.CHOOSING_PAWN;
                    }
                    blacksTurn = !blacksTurn;
                    returnMessage.setBlacksTurn(blacksTurn);
                }
            }
            break;
            case CHOOSING_PAWN: {
                if ((blacksTurn && model.getPoint(p).getStatus() != Point.Status.BLACK) ||
                        (!blacksTurn && model.getPoint(p).getStatus() != Point.Status.WHITE))
                    throw new GameLogicException("Illegal pawn to select.");
                else {
                    placeToMoveFrom = p;
                    returnMessage = new LogicMessage(LogicMessage.HIGHLIGHT, p, LogicMessage.MOVE_PAWN, blacksTurn);
                }
                state = Gamestate.MOVING_PAWNS;
            }
            break;
            case MOVING_PAWNS: {
                if (!model.getPoint(p).isEmpty()) throw new GameLogicException("Occupied space");
                if (!legalMove(placeToMoveFrom,p)) throw new GameLogicException("Illegal move.");
                returnMessage = new LogicMessage(placeToMoveFrom, p);
                model.getPoint(p).setStatus(model.getPoint(placeToMoveFrom).getStatus());
                model.getPoint(placeToMoveFrom).setStatus(Point.Status.EMPTY);
                blacksTurn = !blacksTurn;
                returnMessage.setBlacksTurn(blacksTurn);
                returnMessage.setNextMove(LogicMessage.CHOOSE_PAWN);
                state = Gamestate.CHOOSING_PAWN;
            }
        }
        return returnMessage;
    }

    public boolean legalMove(int from, int to) {
        if ((blacksTurn && blackFlying) || (!blacksTurn && whiteFlying)) return true;
        switch (to) {
            case 1:
                return (from == 4 || from == 22);
            case 2:
                return (from == 5 || from == 23);
            case 3:
                return (from == 6 || from == 24);
            case 4:
                return (from == 1 || from == 7 || from == 5);
            case 5:
                return (from == 4 || from == 6 || from == 2 || from == 8);
            case 6:
                return (from == 3 || from == 5 || from == 9);
            case 7:
                return (from == 4 || from == 10);
            case 8:
                return (from == 5 || from == 11);
            case 9:
                return (from == 6 || from == 12);
            case 10:
                return (from == 11 || from == 7 || from == 13);
            case 11:
                return (from == 10 || from == 12 || from == 8 || from == 14);
            case 12:
                return (from == 11 || from == 15 || from == 9);
            case 13:
                return (from == 16 || from == 10);
            case 14:
                return (from == 11 || from == 17);
            case 15:
                return (from == 12 || from == 18);
            case 16:
                return (from == 13 || from == 17 || from == 19);
            case 17:
                return (from == 14 || from == 16 || from == 20 || from == 18);
            case 18:
                return (from == 17 || from == 15 || from == 21);
            case 19:
                return (from == 16 || from == 22);
            case 20:
                return (from == 17 || from == 23);
            case 21:
                return (from == 18 || from == 24);
            case 22:
                return (from == 1 || from == 19 || from == 23);
            case 23:
                return (from == 22 || from == 2 || from == 20 || from == 24);
            case 24:
                return (from == 3 || from == 21 || from == 23);
        }
        return false;
    }

    private enum Gamestate {
        IDLE, PLACING_PAWNS, PAWN_REMOVAL, CHOOSING_PAWN, MOVING_PAWNS;
    }
}
