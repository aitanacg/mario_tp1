package tp1.exceptions;
//mis cosas como reset, action... me devolvian null o false, ahora mando msgs de errores claros para el jugador

public class ActionParseException extends GameParseException {
    public ActionParseException(String message) {
        super(message);
    }

    public ActionParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
