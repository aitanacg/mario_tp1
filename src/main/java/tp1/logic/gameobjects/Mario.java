package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.logic.Action;
import tp1.logic.Game;

public class Mario {

	public enum Facing{ LEFT, RIGHT};

	private final Game game;
	private Position pos;
	private int dx = +1;
	private Facing facing = Facing.RIGHT;
	private boolean big = false;  //si fuera true, ocuparia la tile de arriba
	private boolean falling;

	private int saltitosLeft = 0;

	
	public Mario(Game game, Position pos) {
		this.game = game;
		this.pos= pos;
	}
	public Position getPosition() {
   	 	return pos;
	}
	public Facing getFacing(){
		return facing;
	}
	public void setFacing(Facing f){
		this.facing = f;
	}

	public boolean occupies(Position p) { //si es big tmb el la de arriba
        if (p.equals(pos)) return true;
        return big && p.equals(pos.translate(0, -1));
   	}

	public boolean isBig() {
		return big;
	}

	public void setBig(boolean b) {
		this.big = b;
	}

	public String getIcon() {
		if (dx == 0) {
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
	 * Para el movimiento, Mario puede hacer 4 a la vez 
	 * Para subir, puede hacerlo 4 bloques, pero despues cae uno cada turno 
	 * no vuela :) pero si salta mucho
	 * para esto uso didVerticalAction y onGround, y los saltitosLeft para que no me vuele
	 */
	
	public void update() {
		boolean moved = false;
		boolean didVerticalAction = false;
		boolean onGround = isOnGround();
		if (onGround)
			saltitosLeft = 4;


		while (!game.getActions().isEmpty()) {
			Action act = game.getActions().extract();
	
			switch (act) {
				case LEFT:
					dx = -1;
					Position left = pos.translate(dx, 0);
					if (isInsideBoard(left) && !game.getGameObjectContainer().isSolidAt(left)) {
						pos = left;
						moved = true;
					}
					facing = Facing.LEFT;
					break;

				case RIGHT:
					dx = 1;
					Position right = pos.translate(dx, 0);
					if (isInsideBoard(right) && !game.getGameObjectContainer().isSolidAt(right)) {
						pos = right;
						moved = true;
					}
					facing = Facing.RIGHT;
					break;

				case UP:
					if (saltitosLeft > 0) {
						Position up = pos.translate(0, -1);
						Position up2 = big ? pos.translate(0, -2) : up;  //si mi mario es big pos que no se mate
						if (isInsideBoard(up2) && !game.getGameObjectContainer().isSolidAt(up2)) {
							pos = up;
							moved = true;
							saltitosLeft--;
							didVerticalAction = true;
						}
					}
					break;

				case DOWN:
					Position next = pos.translate(0, 1);
					while (isInsideBoard(next) && !game.getGameObjectContainer().isSolidAt(next)) {
						pos = next;
						next = pos.translate(0, 1);
					}
					dx = 0;
					moved = true;
					didVerticalAction = true;
					break;

				case STOP:
					dx = 0;
					break;
			}
		}

		//aplico gravedad SOLO SI NO HAY ACCION VERTICAL EEEE
		Position below = pos.translate(0, 1);
		boolean hasFloor = isInsideBoard(below) && game.getGameObjectContainer().isSolidAt(below);

		if (!didVerticalAction && !hasFloor) {
			pos = below;
			falling = true;

			if (!isInsideBoard(pos)) {

				game.marioDies();
			}
			return;
		}

		//si no se mueve lo muevo automaticamente lateralll
		if (!moved) {
			int r = pos.getRow();
			int c = pos.getCol();
			int nextC = c + dx;
			Position next = new Position(r, nextC);
			Position upNext = next.translate(0, -1); //arriba

			boolean hitsWall = (nextC < 0 || nextC >= Game.DIM_X);
			boolean landFront = !hitsWall && game.getGameObjectContainer().isSolidAt(next);
			boolean topBan = big && game.getGameObjectContainer().isSolidAt(upNext);

			if (hitsWall || landFront || topBan) {
				dx = -dx;
			} else {
				pos = next;
			}
		}

		//compruebo si le da a un bicho
		if (game.getGameObjectContainer().hasEnemyAt(pos)) {
			game.marioDies();
			return;
		}

		if (big) {  //si es grande tmb casilla de arriba
			Position up = pos.translate(0, -1);
			if (game.getGameObjectContainer().hasEnemyAt(up)) {
				game.marioDies();
				return;
			}
		}

		if (game.getGameObjectContainer().exitDoorAt(pos)) {
			game.setPlayerWon();
		}

		game.doInteractionsFrom(this);
		this.falling = !game.getGameObjectContainer().isSolidAt(pos.translate(0, 1));

	}

	public boolean interactWith(Goomba other) {
		if (pos.equals(other.getPosition()) ||
				(isBig() && pos.translate(0, -1).equals(other.getPosition()))) {

			boolean falling = this.falling;

			if (falling) {
				other.receiveInteraction(this);
			} else if (isBig()) {
				big = false; //se hace chiquito
				other.receiveInteraction(this);
			} else {
				game.marioDies(); //pierde vida y reinicia
				return true;
			}
			return true;
		}
		return false;
	}

	private boolean isOnGround() {
		Position below = pos.translate(0, 1);
		return game.getGameObjectContainer().isSolidAt(below);
	}

	private boolean isInsideBoard(Position p) {
		return p.getRow() >= 0 && p.getRow() < Game.DIM_Y &&
				p.getCol() >= 0 && p.getCol() < Game.DIM_X;
	}

}
