package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {

    protected Land() {
        super();
    }//constructor vacio para factoria

    public Land(GameWorld game, Position pos) {
        super(game, pos);
    }

    @Override
    public void update() {

    }//no fa res

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
        return false; //A land no le importa mario
    }

    @Override
    public boolean receiveInteraction(Goomba g) {
        return false;//same
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

    ////=====================FACTORIA==========================================
    public GameObject parse(String[] words, GameWorld game) {
        if (!GameObject.matchesType(words[1], "LAND", "L"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new Land(game, pos);
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //savee
        Position p = this.position;
        return "(" + p.getRow() + "," + p.getCol() + ") Land";
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public GameObject copy(GameWorld newGame) {//para load/save, evito refs compartidas (FGC)
        return new Land(newGame, new Position(position.getRow(), position.getCol()));
    }
}