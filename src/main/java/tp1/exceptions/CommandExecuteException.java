package tp1.exceptions;
//errores al ejecutar comandos
public class CommandExecuteException extends CommandException {
    public CommandExecuteException(String message) {
        super(message);
    }

    public CommandExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}