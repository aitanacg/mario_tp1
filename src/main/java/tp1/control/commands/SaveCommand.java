package tp1.control.commands;
//cuarda config actual en un archivo txt
import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.Game;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class SaveCommand extends AbstractCommand {

    private static final String NAME = "save";
    private static final String SHORTCUT = "s";
    private static final String DETAILS = "[s]ave <fileName>";
    private static final String HELP = "save the actual configuration in text file <fileName>";

    private String fileName;

    public SaveCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    } //cuando creo lista comms disp

    private SaveCommand(String fileName) {
        this();
        this.fileName = fileName;
    }

    @Override
    public Command parse(String[] words) throws CommandParseException {

        if (!matchCommand(words[0])) return null; //s o save
        if (words.length != 2) throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);//dues paraules

        return new SaveCommand(words[1]);//fileName
    }

    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        try {
            ((Game) game).save(fileName);//cast pq GModel no tiene save pero game si
            view.showMessage("Game saved in " + fileName);
        }
        catch (Exception e) {
            throw new CommandExecuteException(
                    Messages.ERROR_SAVING.formatted(fileName), e
            );
        }
    }
}