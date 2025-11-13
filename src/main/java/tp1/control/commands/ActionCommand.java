package tp1.control.commands;

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
    public Command parse(String[] commandWords) {
        if (commandWords.length >= 2 && matchCommand(commandWords[0])) {
            String[] acts = new String[commandWords.length - 1];
            System.arraycopy(commandWords, 1, acts, 0, acts.length);
            return new ActionCommand(acts);
        }
        return null;
    }

    @Override
    public void execute(GameModel game, GameView view) {
        if (actions == null || actions.length == 0) {
            view.showError(Messages.COMMAND_PARAMETERS_MISSING);
            return;
        }

        for (String s : actions) {
            Action act = Action.parse(s);
            if (act == null) {
                view.showError(Messages.UNKNOWN_ACTION.formatted(s));
                return;
            }
            game.addAction(act);
        }

        game.updateTurn(); //hace turno
        view.showGame(); //dibuja
    }
}
