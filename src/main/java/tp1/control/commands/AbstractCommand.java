package tp1.control.commands;

public abstract class AbstractCommand implements Command {

    private final String name;
    private final String shortcut;
    private final String details;
    private final String help;

    public AbstractCommand(String name, String shortcut, String details, String help) {
        this.name = name;
        this.shortcut = shortcut;
        this.details = details;
        this.help = help;
    }

    protected boolean matchCommand(String name) {
        return this.name.equalsIgnoreCase(name) || this.shortcut.equalsIgnoreCase(name);
    }

    @Override
    public String helpText() {
        return details + " : " + help;
    }
}
