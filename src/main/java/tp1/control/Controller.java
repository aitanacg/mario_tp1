package tp1.control;
//traduce los commandos a acciones de actionlist
import tp1.exceptions.CommandException;
import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.Game;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;
import tp1.control.commands.Command;
import tp1.control.commands.CommandGenerator;

/**
 *  Accepts user input and coordinates the game execution logic (refactored with Command pattern)
 */
public class Controller {

    private final GameModel game;
    private final GameView view;

    public Controller(GameModel game, GameView view) {
        this.game = game;
        this.view = view;
    }

    public void run() {
        view.showWelcome();
        view.showGame();

        while (!game.isFinished()) {
            try {
                String[] words = view.getPrompt();
                Command command = CommandGenerator.parse(words); //me identifica el comando
                command.execute(game, view); //me ejecuta el comando
                if (game.hasBombExploded()) {
                    System.out.println(Messages.BOMB_EXPLODED);
                    game.clearBombExploded();
                }
                view.showGame();//si no hay eXCepciones
            }
            catch (CommandException e) { //one & onlyy excepcion
                view.showError(e.getMessage());//"error executing load command" o lo que sea

                Throwable causa = e.getCause();//me hago detective y busco causa, por ej, si la file config es popo
                while (causa != null) {
                    view.showError(causa.getMessage());
                    causa = causa.getCause();
                }
            }
        }
        view.showEndMessage();
    }
}