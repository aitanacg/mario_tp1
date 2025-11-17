package tp1.logic;

//para el status del juego, tipo todo lo extra

public interface GameStatus {

    int remainingTime();
    int points();
    int numLives();
    boolean isFinished();
    boolean playerWins();
    boolean playerLoses();
}