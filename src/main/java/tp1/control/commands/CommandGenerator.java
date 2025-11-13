package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

public class CommandGenerator {

    private static final List<Command> AVAILABLE_COMMANDS = Arrays.asList(
            new ActionCommand(),
            new UpdateCommand(),
            new ResetCommand(),
            new HelpCommand(),
            new ExitCommand()
    );

    public static Command parse(String[] words) {
        for (Command c : AVAILABLE_COMMANDS) {
            Command parsed = c.parse(words);
            if (parsed != null) return parsed;
        }
        return null;
    }

    public static String commandHelp() {
        StringBuilder sb = new StringBuilder();
        for (Command c : AVAILABLE_COMMANDS) {
            sb.append(c.helpText()).append("\n");
        }
        return sb.toString();
    }
}
