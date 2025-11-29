package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.Game;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class ResetCommand extends AbstractCommand{

    private static final String NAME = Messages.COMMAND_RESET_NAME;
    private static final String SHORTCUT = Messages.COMMAND_RESET_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_RESET_DETAILS;
    private static final String HELP = Messages.COMMAND_RESET_HELP;

    private Integer levelToReset = null;

    public ResetCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    private ResetCommand(Integer level) {
        this();
        this.levelToReset = level;
    }

    @Override
    public Command parse(String[] words) throws CommandParseException{

        if (!matchCommand(words[0]))
            return null;

        //reset sin argumentos
        if (words.length == 1)
            return new ResetCommand(null);

        //reset con level
        if (words.length == 2) {
            try {
                Integer lvl = Integer.parseInt(words[1]);
                return new ResetCommand(lvl);
            }
            catch (NumberFormatException e) {
                throw new CommandParseException(Messages.LEVEL_NOT_A_NUMBER_ERROR.formatted(words[1]), e);
                //return null; //si  no es un numero no lo quiero
            }
        }

        //return null; //si hay algo mas pos tampoco
        throw new CommandParseException("Incorrect number of arguments for reset command");
    }

    @Override
    public void execute(GameModel gameModel, GameView view) throws CommandExecuteException{
        try {
            Game game = (Game) gameModel;

            if (levelToReset == null)
                game.reset();
            else
                game.reset(levelToReset);

            view.showMessage("Game reset!");
            view.showGame();
        }
        catch (Exception e) {
            throw new CommandExecuteException("Failed to reset game", e);
        }
    }
}
