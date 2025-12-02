package tp1.exceptions;
//se lanza en los parse() de los comandos, si no tiene parametros correctos, mal escrito el comm o hay palabras de mas
//se captura en el controller
public class CommandParseException extends CommandException {
    public CommandParseException(String message) {
        super(message);
    }

    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }
}