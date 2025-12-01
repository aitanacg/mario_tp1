package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.Game;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mushroom extends MovingObject {

    //const vacio para factoria
    protected Mushroom() {
        super(null, null);
        this.dir = Action.RIGHT;
    }

    //constructor
    public Mushroom(Game game, Position pos) {
        super(game, pos);
        this.dir = Action.RIGHT; //default derecha
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getIcon() {
        return Messages.MUSHROOM;
    }

    @Override
    public void update() {

        //gravedad igual que goomba jsjs
        Position below = position.down();
        if (!game.getGameObjectContainer().isSolidAt(below)) {
            position = below;
            if (position.getRow() >= Game.DIM_Y) {
                die();
            }
            return;
        }

        //mov horizontal con rebote como goomba vamos
        Position nextPos = position.next(dir);

        boolean hitsWall = !nextPos.isInBounds(Game.DIM_X, Game.DIM_Y);
        boolean solidAhead = game.getGameObjectContainer().isSolidAt(nextPos);

        if (hitsWall || solidAhead) {
            flipDirection();
            return;
        }

        position = nextPos;
    }

    private void flipDirection() {
        if (dir == Action.LEFT) dir = Action.RIGHT;
        else dir = Action.LEFT;
    }

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Mario m) {

        if (!m.isBig()) {
            m.setBig(true);
            game.addPoints(50); //no dice que me de points pero yo quiero puntos asi que eso
        }
        die(); //siempre se muere

        return true;
    }

    //a los otros los ignora
    @Override public boolean receiveInteraction(Goomba g) { return false; }
    @Override public boolean receiveInteraction(Land l) { return false; }
    @Override public boolean receiveInteraction(ExitDoor d) { return false; }

    //FACTORIA
    @Override
    public GameObject parse(String[] words, Game game) {

        if (!GameObject.matchesType(words[1], "MUSHROOM", "MUSH")) return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new Mushroom(game, pos);
    }

    @Override
    public String toString() { //para el save como todos los otros
        Position p = this.position;
        return "(" + p.getRow() + "," + p.getCol() + ") Mushroom";
    }

    @Override
    public GameObject copy(Game newGame) { //como los otros, para el FGC, para load y save
        Mushroom m = new Mushroom(newGame, new Position(position.getRow(), position.getCol()));
        m.setDir(this.dir);
        return m;
    }
}