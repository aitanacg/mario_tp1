package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Brick extends MovingObject {

    protected Brick() {
        super();
    }
    //constructor vacio para factoria

    public Brick(GameWorld game, Position pos) {
        super(game, pos);
        this.falling = true;
    }

    @Override
    public void update() {
        //gravedad
        Position below = position.down();
        if (below.isInBounds(Game.DIM_Y, Game.DIM_X)
                && !game.getGameObjectContainer().isSolidAt(below)) {
            position = below;
            falling = true;
        }
        else {
            falling = false;
        }

    }

    @Override
    public boolean isSolid() {
        return true; //suelo
    }

    @Override
    public String getIcon() {
        return Messages.LAND;
    }

    ////===================INTERACTIONS (DOUBLE DISPATCH)====================================
    @Override
    public boolean interactWith(GameItem other) {
        if (other.isInPosition(position)) {
            return other.receiveInteraction(this);
        }
        return false;
    }

    @Override
    public boolean receiveInteraction(Mario m) {
        if (this.falling && position.equals(m.getPosition())) {

            if (!m.isBig()) {
                game.marioDies();
            }
            else {
                m.setBig(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean receiveInteraction(Goomba g) {
        if (this.falling && position.equals(g.getPosition())) {
            g.die();
            return true;
        }
        return false;
    }

    @Override
    public boolean receiveInteraction(ExitDoor d) {
        return false;//same
    }

    @Override
    public boolean receiveInteraction(Land l) {
        return false;//same
    }

    @Override
    public boolean receiveInteraction(Mushroom m) { return false; }

    @Override
    public boolean receiveInteraction(SolidIsLava sl) {
        return false;
    }

    ////=====================FACTORIA==========================================
    public GameObject parse(String[] words, GameWorld game) {
        if (!GameObject.matchesType(words[1], "BRICK", "BR"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new Brick(game, pos);
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //savee
        Position p = this.position;
        return "(" + p.getRow() + "," + p.getCol() + ") Brick";
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public GameObject copy(GameWorld newGame) {//para load/save, evito refs compartidas (FGC)
        return new Brick(newGame, new Position(position.getRow(), position.getCol()));
    }
}