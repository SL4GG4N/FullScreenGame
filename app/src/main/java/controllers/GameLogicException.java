package controllers;

import java.lang.Exception;

/**
 * Created by simonlundstrom on 28/11/16.
 */

public class GameLogicException extends Exception {
    public GameLogicException(String message) {
        super(message);
    }
}
