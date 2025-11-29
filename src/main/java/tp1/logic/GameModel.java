package tp1.logic;

import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;

public interface GameModel extends GameWorld, GameStatus {
    //interfaz completa
    //junto las dos de arriba asi las puedo usar en game
    void addObject(String[] objWords) throws OffBoardException, ObjectParseException;
}