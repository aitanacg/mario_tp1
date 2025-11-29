package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class UpdateCommand extends NoParamsCommand {

    private static final String NAME = Messages.COMMAND_UPDATE_NAME;
    private static final String SHORTCUT = Messages.COMMAND_UPDATE_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_UPDATE_DETAILS;
    private static final String HELP = Messages.COMMAND_UPDATE_HELP;

    public UpdateCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException{
        try {
            game.updateTurn();//avanza un ciclo
            view.showGame();//dibujoo
        }
        catch (Exception e) {
            throw new CommandExecuteException("Failed to update the game", e);
        }
    }
}