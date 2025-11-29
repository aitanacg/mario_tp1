package tp1.logic;
//para el mundo

import tp1.exceptions.ActionParseException;
import tp1.exceptions.GameModelException;

public interface GameWorld {

    void addAction(Action action) throws ActionParseException;
    void updateTurn() throws GameModelException;
    void reset();
    void marioDies();
}