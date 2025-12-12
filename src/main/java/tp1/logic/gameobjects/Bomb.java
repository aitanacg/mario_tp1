package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Bomb extends GameObject{
    private int timer = 3;

    protected Bomb() {
        super();
    }//constructor vacio para factoria

    public Bomb(GameWorld game, Position pos) {
        super(game, pos);
        this.timer = 3;
    }

    @Override
    public void update() {
        if (!alive) return;
        timer--;

        if (timer <= 0) {
            explode();
            die();
        }
    }//no fa res

    public void explode() {
        game.BombExploded();
        for(GameObject obj : game.getGameObjectContainer().getObjects()) {
            if (obj == this) continue; // no se autoprocesa
            if (!obj.isAlive()) continue; // si ya está muerto, ignora
            if (obj.isSolid()) continue;
            Position p = obj.getPosition();
            int dist = radio(position, p);
            if (dist <= 6) {
                obj.receiveInteraction(this);
            }
        }
    }
    private int radio(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow())
                + Math.abs(a.getCol() - b.getCol());
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getIcon() {
        return Messages.BOMB;
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

    @Override
    public boolean receiveInteraction(SolidIsLava sl) {
        return false;
    }

    ////=====================FACTORIA==========================================
    public GameObject parse(String[] words, GameWorld game) {
        if (!GameObject.matchesType(words[1], "BOMB", "BO"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new Bomb(game, pos);
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //savee
        Position p = this.position;
        return "(" + p.getRow() + "," + p.getCol() + ") Bomb";
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public GameObject copy(GameWorld newGame) {//para load/save, evito refs compartidas (FGC)
        return new Bomb(newGame, new Position(position.getRow(), position.getCol()));
    }
}
