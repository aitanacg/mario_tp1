package tp1.logic;
//para el mundo, lo que usan los objects

import tp1.exceptions.ActionParseException;
import tp1.exceptions.GameModelException;

public interface GameWorld {

    void addAction(Action action) throws ActionParseException;
    void updateTurn() throws GameModelException;
    void reset();
    void marioDies();
    //nuevo al cambiar game por GameWorld
    ActionList getActions();
    GameObjectContainer getGameObjectContainer();
    void addPoints(int pts);
    void consumeTime(int t);
    void setPlayerWon();
    void BombExploded();
    boolean hasBombExploded();
    void clearBombExploded();
    void setLavaActive();
    boolean isLavaActive();
    void clearLava();
}