package controllers;

import models.NineMenMorrisModel;
import models.Point;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class GameLogic {

    private Gamestate state;
    private Gamestate previousGameState; // beh|vs n{r en kvarn avbryter normalt spel.
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
        model = new NineMenMorrisModel();
        state = Gamestate.PLACING_PAWNS;
        gameCounter = 0;
        blacksTurn = false;
        return new LogicMessage(LogicMessage.RESET_ALL, LogicMessage.RESET_ALL, LogicMessage.PLACE_PAWN, blacksTurn);
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
                    blacksTurn = !blacksTurn;
                    if (gameCounter < 18) returnMessage.setNextMove(LogicMessage.PLACE_PAWN);
                    else {
                        state = Gamestate.CHOOSING_PAWN;
                        returnMessage.setNextMove(LogicMessage.CHOOSE_PAWN);
                    }
                    if (isMill(p)) {
                        blacksTurn = !blacksTurn;
                        previousGameState = state;
                        state = Gamestate.PAWN_REMOVAL;
                        returnMessage.setNextMove(LogicMessage.REMOVE_PAWN);
                    }
                    returnMessage.setBlacksTurn(blacksTurn);
                }
            }
            break;
            case CHOOSING_PAWN: {
                if ((blacksTurn && model.getPoint(p).getStatus() != Point.Status.BLACK) ||
                        (!blacksTurn && model.getPoint(p).getStatus() != Point.Status.WHITE))
                    throw new GameLogicException("Illegal pawn to select.");
                if (((blacksTurn && !blackFlying) || (!blacksTurn && whiteFlying))
                        && blockedPawn(p)) throw new GameLogicException("Pawn is blocked.");
                placeToMoveFrom = p;
                returnMessage = new LogicMessage(LogicMessage.HIGHLIGHT, p, LogicMessage.MOVE_PAWN, blacksTurn);
                state = Gamestate.MOVING_PAWNS;
            }
            break;
            case MOVING_PAWNS: {
                if (!model.getPoint(p).isEmpty()) throw new GameLogicException("Occupied space");
                if (!legalMove(placeToMoveFrom, p)) throw new GameLogicException("Illegal move.");
                returnMessage = new LogicMessage(placeToMoveFrom, p);
                model.getPoint(p).setStatus(model.getPoint(placeToMoveFrom).getStatus());
                model.getPoint(placeToMoveFrom).setStatus(Point.Status.EMPTY);
                blacksTurn = !blacksTurn;
                state = Gamestate.CHOOSING_PAWN;
                returnMessage.setNextMove(LogicMessage.CHOOSE_PAWN);
                if (isMill(p)) {
                    blacksTurn=!blacksTurn;
                    previousGameState = state;
                    state = Gamestate.PAWN_REMOVAL;
                    returnMessage.setNextMove(LogicMessage.REMOVE_PAWN);
                }
                returnMessage.setBlacksTurn(blacksTurn);
            }
            break;
            case PAWN_REMOVAL: {
                if (model.getPoint(p).isEmpty()) throw new GameLogicException("Choose a pawn.");
                if ((blacksTurn && model.getPoint(p).getStatus() == Point.Status.BLACK)
                        || !blacksTurn && model.getPoint(p).getStatus() == Point.Status.WHITE)
                    throw new GameLogicException("Choose a pawns of opponent's colour.");
                if (isMill(p) && !allOpponentsPawnsAreMill(blacksTurn))
                    throw new GameLogicException("Cannot choose a milled pawn to remove when there are unmilled pawns.");
                returnMessage = new LogicMessage(p, LogicMessage.TO_DISCARD_PILE);
                model.getPoint(p).setStatus(Point.Status.EMPTY);
                blacksTurn = !blacksTurn;
                state = previousGameState;
                if (state == Gamestate.PLACING_PAWNS)
                    returnMessage.setNextMove(LogicMessage.PLACE_PAWN);
                if (state == Gamestate.CHOOSING_PAWN)
                    returnMessage.setNextMove(LogicMessage.CHOOSE_PAWN);
                returnMessage.setBlacksTurn(blacksTurn);
                if (blacksTurn && numberOfPawnsLeft(blacksTurn)<4) blackFlying=true;
                if (!blacksTurn && numberOfPawnsLeft(blacksTurn)<4) whiteFlying=true;
/*                if (numberOfPawnsLeft(true)<3 || numberOfPawnsLeft(false)<3)
                    returnMessage = new LogicMessage(0,0,LogicMessage.GAME_OVER,blacksTurn);*/
            }
        }
        return returnMessage;
    }

    private int numberOfPawnsLeft(boolean blacksTurn) {
        Point.Status player;
        if (blacksTurn) player = Point.Status.BLACK;
        else player = Point.Status.WHITE;


        int noOfPawns=0;
        for (Point p : model.getPoints()) if (p.getStatus()==player) noOfPawns++;
        return noOfPawns;
    }

    private boolean blockedPawn(int pos) {
        switch (pos) {
            case 0:return noneAreEmpty(3, 21);
            case 1:return noneAreEmpty(4, 22);
            case 2:return noneAreEmpty(23, 5);
            case 3:return noneAreEmpty(0, 6, 4);
            case 4:return noneAreEmpty(1, 3, 5, 7);
            case 5:return noneAreEmpty(2, 4, 8);
            case 6:return noneAreEmpty(3, 9);
            case 7:return noneAreEmpty(4, 10);
            case 8:return noneAreEmpty(5, 11);
            case 9:return noneAreEmpty(6, 10, 12);
            case 10:return noneAreEmpty(7, 9, 11, 13);
            case 11:return noneAreEmpty(8, 10, 14);
            case 12:return noneAreEmpty(9, 15);
            case 13:return noneAreEmpty(10, 16);
            case 14:return noneAreEmpty(11, 17);
            case 15:return noneAreEmpty(12, 16, 18);
            case 16:return noneAreEmpty(13, 15, 17, 19);
            case 17:return noneAreEmpty(14, 16, 20);
            case 18:return noneAreEmpty(15, 21);
            case 19:return noneAreEmpty(16, 22);
            case 20:return noneAreEmpty(17, 23);
            case 21:return noneAreEmpty(0, 18, 22);
            case 22:return noneAreEmpty(1, 19, 21, 23);
            case 23:return noneAreEmpty(2, 20, 22);
            default: return true;
        }
    }

    private boolean noneAreEmpty(int... positions) {
        for (int i : positions) if (model.getPoint(i).isEmpty()) return false;
        return true;
    }

    private boolean allOpponentsPawnsAreMill(boolean blacksTurn) {
        Point.Status player;
        if (blacksTurn) player = Point.Status.WHITE;
        else player = Point.Status.BLACK;
        for (int i = 0; i<24; i++) {
            if (model.getPoint(i).getStatus()!=player) continue;
            if (!isMill(i)) return false;
        }
        return true;
    }

    private boolean legalMove(int from, int to) {
        if ((blacksTurn && blackFlying) || (!blacksTurn && whiteFlying)) return true;
        switch (to) {
            case 0:return (from == 3 || from == 21);
            case 1:return (from == 4 || from == 22);
            case 2:return (from == 5 || from == 23);
            case 3:return (from == 0 || from == 6 || from == 4);
            case 4:return (from == 3 || from == 5 || from == 1 || from == 7);
            case 5:return (from == 2 || from == 4 || from == 8);
            case 6:return (from == 3 || from == 9);
            case 7:return (from == 4 || from == 10);
            case 8:return (from == 5 || from == 11);
            case 9:return (from == 10 || from == 6 || from == 12);
            case 10:return (from == 9 || from == 11 || from == 7 || from == 13);
            case 11:return (from == 10 || from == 14 || from == 8);
            case 12:return (from == 15 || from == 9);
            case 13:return (from == 10 || from == 16);
            case 14:return (from == 11 || from == 17);
            case 15:return (from == 12 || from == 16 || from == 18);
            case 16:return (from == 13 || from == 15 || from == 19 || from == 17);
            case 17:return (from == 16 || from == 14 || from == 20);
            case 18:return (from == 15 || from == 21);
            case 19:return (from == 16 || from == 22);
            case 20:return (from == 17 || from == 23);
            case 21:return (from == 0 || from == 18 || from == 22);
            case 22:return (from == 21 || from == 1 || from == 19 || from == 23);
            case 23:return (from == 2 || from == 20 || from == 22);
        }
        return false;
    }

    private boolean isMill(int pos) {

        if ((pos == 0 || pos == 3 || pos == 6)
                && model.getPoint(0).getStatus() == model.getPoint(3).getStatus()
                && model.getPoint(3).getStatus() == model.getPoint(6).getStatus()) {
            return true;
        } else if ((pos == 1 || pos == 4 || pos == 7)
                && model.getPoint(1).getStatus() == model.getPoint(4).getStatus()
                && model.getPoint(4).getStatus() == model.getPoint(7).getStatus()) {
            return true;
        } else if ((pos == 2 || pos == 5 || pos == 8)
                && model.getPoint(2).getStatus() == model.getPoint(5).getStatus()
                && model.getPoint(5).getStatus() == model.getPoint(8).getStatus()) {
            return true;
        } else if ((pos == 6 || pos == 9 || pos == 12)
                && model.getPoint(6).getStatus() == model.getPoint(9).getStatus()
                && model.getPoint(9).getStatus() == model.getPoint(12).getStatus()) {
            return true;
        } else if ((pos == 7 || pos == 10 || pos == 13)
                && model.getPoint(7).getStatus() == model.getPoint(10).getStatus()
                && model.getPoint(10).getStatus() == model.getPoint(13).getStatus()) {
            return true;
        } else if ((pos == 8 || pos == 11 || pos == 14)
                && model.getPoint(8).getStatus() == model.getPoint(11).getStatus()
                && model.getPoint(11).getStatus() == model.getPoint(14).getStatus()) {
            return true;
        } else if ((pos == 12 || pos == 15 || pos == 18)
                && model.getPoint(12).getStatus() == model.getPoint(15).getStatus()
                && model.getPoint(15).getStatus() == model.getPoint(18).getStatus()) {
            return true;
        } else if ((pos == 13 || pos == 16 || pos == 19)
                && model.getPoint(13).getStatus() == model.getPoint(16).getStatus()
                && model.getPoint(16).getStatus() == model.getPoint(19).getStatus()) {
            return true;
        } else if ((pos == 14 || pos == 17 || pos == 20)
                && model.getPoint(14).getStatus() == model.getPoint(17).getStatus()
                && model.getPoint(17).getStatus() == model.getPoint(20).getStatus()) {
            return true;
        } else if ((pos == 0 || pos == 21 || pos == 18)
                && model.getPoint(0).getStatus() == model.getPoint(21).getStatus()
                && model.getPoint(21).getStatus() == model.getPoint(18).getStatus()) {
            return true;
        } else if ((pos == 1 || pos == 22 || pos == 19)
                && model.getPoint(1).getStatus() == model.getPoint(22).getStatus()
                && model.getPoint(22).getStatus() == model.getPoint(19).getStatus()) {
            return true;
        } else if ((pos == 2 || pos == 23 || pos == 20)
                && model.getPoint(2).getStatus() == model.getPoint(23).getStatus()
                && model.getPoint(23).getStatus() == model.getPoint(20).getStatus()) {
            return true;
        } else if ((pos == 21 || pos == 22 || pos == 23)
                && model.getPoint(21).getStatus() == model.getPoint(22).getStatus()
                && model.getPoint(22).getStatus() == model.getPoint(23).getStatus()) {
            return true;
        } else if ((pos == 3 || pos == 4 || pos == 5)
                && model.getPoint(3).getStatus() == model.getPoint(4).getStatus()
                && model.getPoint(4).getStatus() == model.getPoint(5).getStatus()) {
            return true;
        } else if ((pos == 9 || pos == 10 || pos == 11)
                && model.getPoint(9).getStatus() == model.getPoint(10).getStatus()
                && model.getPoint(10).getStatus() == model.getPoint(11).getStatus()) {
            return true;
        } else if ((pos == 15 || pos == 16 || pos == 17)
                && model.getPoint(15).getStatus() == model.getPoint(16).getStatus()
                && model.getPoint(16).getStatus() == model.getPoint(17).getStatus()) {
            return true;
        }
        return false;
    }

    private enum Gamestate {
        IDLE, PLACING_PAWNS, PAWN_REMOVAL, CHOOSING_PAWN, MOVING_PAWNS;
    }
}
