package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import models.Pawn;
import models.Point;

/**
 * Created by simonlundstrom on 18/11/16.
 */

public class FileHandler {
    public static boolean saveToDisk(GameLogic logic, OutputStream ut) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        ObjectOutputStream objektskriv = new ObjectOutputStream(ut);
        objektskriv.writeObject(logic.getModel().getPawns());
        objektskriv.writeObject(logic.getModel().getPoints());
        objektskriv.writeObject(logic.getState());
        objektskriv.writeBoolean(logic.isBlacksTurn());
        objektskriv.close();
        ut.close();
        return true;
    }

    public static GameLogic loadFromDisk(InputStream in) throws IOException,ClassNotFoundException {
        ObjectInputStream objektlas = new ObjectInputStream(in);
        Pawn[] pawns = (Pawn[])objektlas.readObject();
        Point[] points = (Point[])objektlas.readObject();
        GameLogic.Gamestate state = (GameLogic.Gamestate)objektlas.readObject();
        boolean blacksTurn = objektlas.readBoolean();
        GameLogic retur = new GameLogic(pawns,points,state,blacksTurn);
        objektlas.close();
        in.close();
        return retur;
    }
}
