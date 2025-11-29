package tp1.logic.gameobjects;
//clase abstracta de obj que comparten posicion, gravedad, direccion horizontal y dir del mov permitido
import tp1.logic.Action;
import tp1.logic.Game;
import tp1.logic.Position;

public abstract class MovingObject extends GameObject {

    //protected int dirX = 0;
    protected Action dir = Action.STOP;

    protected boolean falling = false;

    //public void setDirX(int dx) {
    //    this.dirX = dx;
    //}

    public void setDir(Action a) {
        this.dir = a;
    }

    public Action getDir() {
        return this.dir;
    }

    public MovingObject(Game game, Position pos) {
        super(game, pos);
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    protected Position nextPosition() {
        //return position.translate(dirX, 0);
        return position.next(dir);

    }

    protected boolean canMoveTo(Position p) {
        //si pos es valida pa moverme
        return p.isInBounds(Game.DIM_X, Game.DIM_Y)
                && !game.getGameObjectContainer().isSolidAt(p);
    }
}