package tp1.logic;

import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
/**
 * Interfaz completa del juego, formada por GStatus y GWorld
 */
public interface GameModel extends GameWorld, GameStatus {

    void addObject(String[] objWords) throws OffBoardException, ObjectParseException;//interpreta una linea del fichero, implementado en Game

}