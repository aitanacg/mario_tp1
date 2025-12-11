package tp1.logic;
/**
 * Status del juego
 */
public interface GameStatus {

    int remainingTime();
    int points();
    int numLives();
    boolean isFinished();
    boolean playerWins();
    boolean playerLoses();
}