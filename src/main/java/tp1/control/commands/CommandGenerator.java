package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.view.Messages;

import java.util.Arrays;
import java.util.List;

public class CommandGenerator {

    private static final List<Command> AVAILABLE_COMMANDS = Arrays.asList(
            new ActionCommand(),
            new UpdateCommand(),
            new ResetCommand(),
            new HelpCommand(),
            new ExitCommand(),
            new AddObjectCommand()
    );

    public static Command parse(String[] words) throws CommandParseException {
        if (words.length == 0 || words[0].isBlank()) { //si es enter o espacios
            throw new CommandParseException(Messages.UNKNOWN_COMMAND.formatted(""));
        }
        for (Command c : AVAILABLE_COMMANDS) { //intento que todos lo parseen
            Command parsed = c.parse(words);
            if (parsed != null) return parsed;
        }
        throw new CommandParseException(Messages.UNKNOWN_COMMAND.formatted(words[0])); //nadie sabie quien es :(
    }

    public static String commandHelp() {
        StringBuilder sb = new StringBuilder();
        for (Command c : AVAILABLE_COMMANDS) {
            sb.append(c.helpText()).append("\n");
        }
        return sb.toString();
    }
}
