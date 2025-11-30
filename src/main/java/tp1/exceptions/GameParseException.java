package tp1.exceptions;
//errores al escribir comandos
public class GameParseException extends GameModelException {
    public GameParseException(String message) {
        super(message);
    }

    public GameParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
