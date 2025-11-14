package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {

    //constructor vacio para factoria
    protected Land() {
        super(null, null);
    }

    public Land(Game game, Position pos) {
        super(game, pos);
    }

    @Override
    public void update() {
        //no fa res
    }

    @Override
    public boolean isSolid() {
        return true; //suelo
    }

    @Override
    public String getIcon() {
        return Messages.LAND;
    }

    //DOUBLE DISPATCH
    @Override
    public boolean interactWith(GameItem other) {
        if (other.isInPosition(position)) {
            return other.receiveInteraction(this);
        }
        return false;
    }

    //INTERACTIONS
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

    //FACTRIA
    public GameObject parse(String[] words, Game game) {
        if (!GameObject.matchesType(words[1], "LAND", "L"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new Land(game, pos);
    }


}
