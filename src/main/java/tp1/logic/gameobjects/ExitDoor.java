package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObject {

    //constructor vacio para factoria
    protected ExitDoor() {
        super();
    }

    public ExitDoor(GameWorld game, Position pos) {
        super(game, pos);
    }

    @Override
    public void update() {
        //no se mueve la door
    }

    @Override
    public String getIcon() {
        return Messages.EXIT_DOOR;
    }

    ////===================INTERACTIONS (DOUBLE DISPATCH)====================================
    @Override
    public boolean interactWith(GameItem other) {
        if (other.isInPosition(this.position)) {
            return other.receiveInteraction(this);
        }
        return false;
    }

    @Override
    public boolean receiveInteraction(Mario m) {
        game.setPlayerWon();
        return true;
    }

    @Override
    public boolean receiveInteraction(Goomba g) {
        return false; //le importa bien poco los goombas yk
    }

    @Override
    public boolean receiveInteraction(ExitDoor d) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Land l) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Mushroom m) { return false; }
    @Override public boolean receiveInteraction(SolidIsLava sl) {return false;}

    ////=====================FACTORIA==========================================
    public GameObject parse(String[] words, GameWorld game) {
        if (!GameObject.matchesType(words[1], "EXITDOOR", "ED"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new ExitDoor(game, pos);
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //para el save
        Position p = this.position;
        return "(" + p.getRow() + "," + p.getCol() + ") ExitDoor";
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public GameObject copy(GameWorld newGame) { //para load/save, evito refs compartidas (FGC)
        return new ExitDoor(newGame, new Position(position.getRow(), position.getCol()));
    }
}