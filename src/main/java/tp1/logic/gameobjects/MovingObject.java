package tp1.logic.gameobjects;
//clase abstracta de obj que comparten posicion, gravedad, direccion horizontal y dir del mov permitido
import tp1.logic.Action;
import tp1.logic.Game;
import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class MovingObject extends GameObject {

    protected Action dir = Action.STOP;
    protected boolean falling = false;
    public void setDir(Action a) {
        this.dir = a;
    }
    public Action getDir() {
        return this.dir;
    }

    public MovingObject(GameWorld game, Position pos) {
        super(game, pos);
    }
    protected MovingObject() {
        super();
        this.dir = Action.STOP;
        this.falling = false;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    protected Position nextPosition() {
        return position.next(dir);
    }

    protected boolean canMoveTo(Position p) { //si pos es valida pa moverme
        return p.isInBounds(Game.DIM_Y, Game.DIM_X) && !game.getGameObjectContainer().isSolidAt(p);
    }

    ////=====================MOV AUTOMATICO (goomba, mushroom)==========================================
    protected void autoMove() {
        if (game == null) return;
        //gravedad
        Position below = position.down();
        if (!game.getGameObjectContainer().isSolidAt(below)) {
            position = below;
            if (position.getRow() >= Game.DIM_Y) die();//si cae fuera del tablero muere
            return;
        }

        //mov horizontal con rebotito
        Position nextPos = position.next(dir);

        boolean hitsWall = !nextPos.isInBounds(Game.DIM_Y, Game.DIM_X);
        boolean solidAhead = game.getGameObjectContainer().isSolidAt(nextPos);
        // rebote
        if (hitsWall || solidAhead) {
            flipDirection();
            return;
        }

        position = nextPos;
    }

    protected void flipDirection() {
        if (dir == Action.LEFT) dir = Action.RIGHT;
        else dir = Action.LEFT;
    }
}