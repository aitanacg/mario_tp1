package tp1.logic;

public interface GameWorld {

    void addAction(Action action);
    void updateTurn();
    void reset();
    void marioDies();
}