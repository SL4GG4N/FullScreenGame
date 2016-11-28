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
                if (!model.getPoint(p).isEmpty()) throw new GameLogicException(state.toString());
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
                    throw new GameLogicException(state.toString());
                else {
                    placeToMoveFrom = p;
                    returnMessage = new LogicMessage(LogicMessage.HIGHLIGHT, p, LogicMessage.MOVE_PAWN, blacksTurn);
                }
                state = Gamestate.MOVING_PAWNS;
            }
            break;
            case MOVING_PAWNS: {
                if (!model.getPoint(p).isEmpty()) throw new GameLogicException(state.toString());
                else
                    returnMessage = new LogicMessage(placeToMoveFrom, p);
                blacksTurn = !blacksTurn;
                returnMessage.setBlacksTurn(blacksTurn);
                returnMessage.setNextMove(LogicMessage.CHOOSE_PAWN);
                state = Gamestate.CHOOSING_PAWN;
            }
        }
        return returnMessage;
    }

    private enum Gamestate {
        IDLE, PLACING_PAWNS, PAWN_REMOVAL, CHOOSING_PAWN, MOVING_PAWNS;
    }
}
