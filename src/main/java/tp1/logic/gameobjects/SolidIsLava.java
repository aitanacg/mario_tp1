package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class SolidIsLava extends GameObject{
    private int counter = 4;

    protected SolidIsLava() {
        super();
    }//constructor vacio para factoria

    public SolidIsLava(GameWorld game, Position pos) {
        super(game, pos);
        alive = true;
    }

    @Override
    public void update() {
        counter--;
        if (counter == 0) {
            game.setLavaActive();   // activa lava 1 turno
        }
        if (counter < 0)
            counter = 4;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public String getIcon() {
        return Messages.LAVA;
    }

    ////===================INTERACTIONS (DOUBLE DISPATCH)====================================
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
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
        if (!GameObject.matchesType(words[1], "SOLIDISLAVA", "SL"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new SolidIsLava(game, pos);
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //savee
        Position p = this.position;
        return "(" + p.getRow() + "," + p.getCol() + ") SolidIsLava";
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public GameObject copy(GameWorld newGame) {//para load/save, evito refs compartidas (FGC)
        return new SolidIsLava(newGame, new Position(position.getRow(), position.getCol()));
    }
}
