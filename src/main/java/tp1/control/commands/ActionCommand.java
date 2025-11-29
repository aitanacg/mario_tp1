package tp1.control.commands;

import tp1.exceptions.ActionParseException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.CommandExecuteException;
import tp1.logic.GameModel;
import tp1.logic.Action;
import tp1.view.GameView;
import tp1.view.Messages;

public class ActionCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_ACTION_NAME;
    private static final String SHORTCUT = Messages.COMMAND_ACTION_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_ACTION_DETAILS;
    private static final String HELP = Messages.COMMAND_ACTION_HELP;

    private String[] actions; //secuencia que introduzco como user

    public ActionCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    private ActionCommand(String[] actions) {
        this();
        this.actions = actions;
    }

    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        if (!matchCommand(commandWords[0]))
            return null;

        if (commandWords.length < 2)
            throw new CommandParseException(Messages.COMMAND_PARAMETERS_MISSING);

        String[] acts = new String[commandWords.length - 1];
        System.arraycopy(commandWords, 1, acts, 0, acts.length);

        return new ActionCommand(acts);
    }

    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {

        try {
            for (String s : actions) {
                Action act = Action.parse(s); //pot llencar actionparsecommand
                game.addAction(act);
            }

            game.updateTurn();//avanzo turno
            view.showGame();//dibujo
        }
        catch (Exception e) { //actionparseexc
            throw new CommandExecuteException(
                    Messages.ERROR_COMMAND_EXECUTE,
                    e
            );
        }
    }
}
