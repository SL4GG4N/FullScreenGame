package controllers;

import models.NineMenMorrisModel;

/**
 * Created by simonlundstrom on 25/11/16.
 */

public class GameLogic {

    private Gamestate state;

    private static NineMenMorrisModel model;

    public GameLogic() {
        model = new NineMenMorrisModel();
        state = Gamestate.IDLE;
    }

    public static NineMenMorrisModel getModel() {
        return model;
    }

    public LogicMessage startNewGame() {
        state=Gamestate.WHITE_TO_PLACE;
        return new LogicMessage(LogicMessage.WHITE_PLACE,-1,-1);
    }

    public LogicMessage handleClick(int p) {
        switch(state) {
            case IDLE: return new LogicMessage(LogicMessage.ERROR_GAME_IDLE,-1,-1);
            default: return new LogicMessage(LogicMessage.ERROR_UNKNOWN_STATE,-1,-1);
        }
    }

    private enum Gamestate {
        IDLE,WHITE_TO_PLACE,BLACK_TO_PLACE,WHITE_TO_REMOVE,BLACK_TO_REMOVE,
        WHITE_TO_MOVE_FROM,WHITE_TO_MOVE_TO,BLACK_TO_MOVE_FROM,BLACK_TO_MOVE_TO;
    }
}
