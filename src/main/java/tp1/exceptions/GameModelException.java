package tp1.exceptions;
//errores del modelo y subclases, en la logica, vienen de Game y GameModel...
//capturadas en LoadCommand execute()

public class GameModelException extends Exception {
    public GameModelException(String message) {
        super(message);
    }

    public GameModelException(String message, Throwable cause) {
        super(message, cause);
    }
}