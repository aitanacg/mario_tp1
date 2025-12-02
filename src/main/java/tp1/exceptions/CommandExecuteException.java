package tp1.exceptions;
//errores al ejecutar comandos, en execute(), no se guarda, no se cara, no existe fichero, no se
//la captura el controller
public class CommandExecuteException extends CommandException {
    public CommandExecuteException(String message) {
        super(message);
    }

    public CommandExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}