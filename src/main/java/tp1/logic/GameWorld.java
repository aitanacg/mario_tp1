package tp1.logic;
//para el mundo

public interface GameWorld {

    void addAction(Action action);
    void updateTurn();
    void reset();
    void marioDies();
}