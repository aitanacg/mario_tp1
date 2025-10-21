package tp1.logic;

/**
 * Represents the allowed actions in the game
 *
 */
public enum Action {
	LEFT(-1,0), RIGHT(1,0), DOWN(0,1), UP(0,-1), STOP(0,0);
	
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

	public  static Action parse(String s){
		if(s == null) throw new IllegalArgumentException("Action is null");
		String t = s.trim().toUpperCase();
        switch (t) {
            case "L": case "LEFT":  return LEFT;
            case "R": case "RIGHT": return RIGHT;
            case "U": case "UP":    return UP;
            case "D": case "DOWN":  return DOWN;
            case "S": case "STOP": 
            case "NONE": return STOP;
            default:
                throw new IllegalArgumentException("Unknown action: " + s);
        }
	}
	
	
}
