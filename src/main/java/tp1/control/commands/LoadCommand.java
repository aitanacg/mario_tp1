package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.GameLoadException;
import tp1.logic.Game;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class LoadCommand extends AbstractCommand {

    private static final String NAME = "load";
    private static final String SHORTCUT = "l";
    private static final String DETAILS = "[l]oad <fileName>";
    private static final String HELP = "load the game configuration from text file <fileName>";

    private String fileName;

    public LoadCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    private LoadCommand(String fileName) {
        this();
        this.fileName = fileName;
    }

    @Override
    public Command parse(String[] words) throws CommandParseException { //nomes accepta dues paraules, o llenca exc

        if (!matchCommand(words[0])) return null; //l o load
        if (words.length != 2) throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);

        return new LoadCommand(words[1]);
    }

    @Override
    public void execute(GameModel gameModel, GameView view) throws CommandExecuteException {

        Game game = (Game) gameModel;
        try {
            game.load(fileName); //llama a load y si falla algo command execute exception
            view.showGame();
        }
        catch (GameLoadException gle) {
            throw new CommandExecuteException(
                    Messages.ERROR_COMMAND_EXECUTE, gle
            );
        }
        catch (Exception e) {
            throw new CommandExecuteException(
                    Messages.ERROR_COMMAND_EXECUTE, e
            );
        }
    }
}