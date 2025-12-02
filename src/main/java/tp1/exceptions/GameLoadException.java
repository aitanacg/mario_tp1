package tp1.exceptions;
//ficheritos

public class GameLoadException extends GameModelException {
    public GameLoadException(String msg) { super(msg); }
    public GameLoadException(String msg, Throwable cause) { super(msg, cause); }
}