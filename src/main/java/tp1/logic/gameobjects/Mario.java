package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.logic.Action;
import tp1.logic.Game;

public class Mario {

	public enum Facing{ LEFT, RIGHT};

	private final Game game;
	private Position pos;
	private Facing facing = Facing.RIGHT;
	private boolean big = false;  //si fuera true, ocuparia la tile de arriba
    private boolean stopped = false; //para el icono STOP
    public int saltitosLeft = 0;
    private boolean jumping = false;
    private boolean falling = false;
	
	public Mario(Game game, Position pos) {
		this.game = game;
		this.pos= pos;
	}
    //basics
	public Position getPosition() {
   	 	return pos;
	}
	public Facing getFacing(){
		return facing;
	}
	public void setFacing(Facing f){
		this.facing = f;
	}
    public boolean isBig() {
        return big;
    }
    public void setBig(boolean b) {
        this.big = b;
    }
    public boolean isFalling() { return falling;}
    public void setFalling(boolean f) { this.falling = f;}
    public boolean isJumping() { return jumping;}
    public void setJumping(boolean j) { this.jumping = j;}

    public Game getGame() { return this.game; }

    //lo que ocupa
	public boolean occupies(Position p) { //si es big tmb el la de arriba
        if (p.equals(pos)) return true;
        return big && p.equals(pos.translate(0, -1));
   	}

	public String getIcon() {
        if (stopped) {
            return tp1.view.Messages.MARIO_STOP;
        } else {
            if (facing == Facing.LEFT) {
                return tp1.view.Messages.MARIO_LEFT;
            } else {
                return tp1.view.Messages.MARIO_RIGHT;
            }
        }
	}
	
	/**
	 *  Implements the automatic update	
	 * 
	 * Para el movimiento, Mario puede hacer un unico movimiento
     * no aplica ni gravedad, ni interacciones ni valida turnos o resta time (en Game)
	 */
	
	public void update() {
        int moves = 0; //cuenta movimientos

        while (!game.getActions().isEmpty() && moves < 4) {
            Action act = game.getActions().extract();
            if (act == null) break;

            paso(act);  //se mueve

            if (act != Action.STOP) {//stop no cuenta
                game.consumeTime(1);
                moves++;
            }
        }
    }

    //aplica una accion
    public boolean paso(Action a) {
        if (a == null) return false;

        if (a == Action.STOP) {
            stopped = true;
            return true;
        }

        stopped = false;
        //direccion que queremos
        if (a == Action.LEFT)  facing = Facing.LEFT;
        if (a == Action.RIGHT) facing = Facing.RIGHT;
        if (a == Action.LEFT || a == Action.RIGHT) {
            jumping = false;
            saltitosLeft = 0;
            falling = false;
        }

        if (a == Action.UP) {
            Position below = pos.translate(0, +1);
            //solo inicia salto si está tocando el suelo
            if (game.getGameObjectContainer().isSolidAt(below)) {
                jumping = true;
                falling = false;
                saltitosLeft = 4;
            }
            return true;
        }
        if (a == Action.DOWN) { //cae hasta suelo
            // cancela el salto para que no se me vaya flotando
            jumping = false;
            saltitosLeft = 0;
            falling = true;

            Position next = pos.translate(0, +1);
            while (isInsideBoard(next) && !game.getGameObjectContainer().isSolidAt(next)) {
                pos = next;
                next = pos.translate(0, +1);
            }
            falling = false;
            return true;
        }

        //mover si es ocupable
        Position next = pos.next(a);
        if (canOccupy(next)) {
            pos = next;
            return true;
        }

        //accion procesada
        return true;
    }

        //mario ocupa casilla base p?
    private boolean canOccupy(Position p) {
        if (!isInsideBoard(p)) return false;
        if (game.getGameObjectContainer().isSolidAt(p)) return false;

        if (big) {
            Position top = p.translate(0, -1);
            if (!isInsideBoard(top)) return false;
            if (game.getGameObjectContainer().isSolidAt(top)) return false;
        }
        return true;
    }

    private boolean isInsideBoard(Position p) {
        return p.getRow() >= 0 && p.getRow() < Game.DIM_Y
                && p.getCol() >= 0 && p.getCol() < Game.DIM_X;
    }

    public void _setPositionForPhysics(Position p) {
        this.pos = p;
    }
}



