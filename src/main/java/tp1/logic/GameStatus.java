package tp1.logic;

public interface GameStatus {

    int remainingTime();
    int points();
    int numLives();
    boolean isFinished();
    boolean playerWins();
    boolean playerLoses();
}