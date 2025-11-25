package tp1.control;
//traduce los commandos a acciones de actionlist
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
            String[] words = view.getPrompt();//lee entrada user
            Command command = CommandGenerator.parse(words); //busca com

            if (command != null) {
                command.execute(game, view);//ejecuta com
            } else {
                view.showError(Messages.UNKNOWN_COMMAND.formatted(String.join(" ", words)));
            }
        }

        view.showEndMessage();
    }
}
