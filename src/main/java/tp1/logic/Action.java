package tp1.logic;

import tp1.exceptions.ActionParseException;
import tp1.view.Messages;
/**
 * Acciones permitidas en el juego
 */
public enum Action {
	LEFT(-1,0), RIGHT(1,0), DOWN(0,1), UP(0,-1), STOP(0,0),
    RIGHTUP(1, -1), RIGHTDOWN(1, +1), LEFTUP(-1, -1), LEFTDOWN(-1, +1);
	
	private final int x;
	private final int y;
	
	private Action(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

    public static Action parse(String s) throws ActionParseException {
        if (s == null) {
            throw new ActionParseException("Action is null");
        }
        String t = s.trim().toUpperCase();
        switch (t) {
            case "L": case "LEFT": return LEFT;
            case "R": case "RIGHT": return RIGHT;
            case "U": case "UP": return UP;
            case "D": case "DOWN": return DOWN;
            case "RU": case "RIGHTUP": return RIGHTUP;
            case "RD": case "RIGHTDOWN": return RIGHTDOWN;
            case "LU": case "LEFTUP": return LEFTUP;
            case "LD": case "LEFTDOWN": return LEFTDOWN;
            case "S": case "STOP":
            case "NONE": return STOP;
            default:
                throw new ActionParseException(Messages.UNKNOWN_ACTION.formatted(s)
                );
        }
    }
    public boolean isOpposite(Action other) {

        // cardinales
        if (this == LEFT && other == RIGHT) return true;
        if (this == RIGHT && other == LEFT) return true;
        if (this == UP && other == DOWN) return true;
        if (this == DOWN && other == UP) return true;

        // diagonales opuestas
        if (this == RIGHTUP && other == LEFTDOWN) return true;
        if (this == LEFTDOWN && other == RIGHTUP) return true;
        if (this == RIGHTDOWN && other == LEFTUP) return true;
        if (this == LEFTUP && other == RIGHTDOWN) return true;

        return false;
    }

}