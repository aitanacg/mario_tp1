package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.logic.Action;

public class Goomba extends MovingObject {

    protected Goomba() {//constructor vacio para factoria
     super();
     }

    public Goomba(GameWorld game, Position pos) {
        super(game,pos);
        this.dir = Action.LEFT;
    }

    @Override
    public String getIcon() {
        return tp1.view.Messages.GOOMBA;
    }

    @Override
    public void update() {
        autoMove();//aplica gravedad, mov horizontal y rebota
    }

    ////===================INTERACTIONS (DOUBLE DISPATCH)====================================
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Mario m) {

        if (!this.isAlive()) return false;

        Position mp = m.getPosition();
        Position gp = this.position;

        //Position marioAbajo = mp.down();
        //boolean pisa = m.isFalling() && marioAbajo.equals(gp);
        boolean pisa = m.isFalling() && mp.equals(gp); //es la misma casilla
        if (pisa) {
            die();
            game.addPoints(100);
            m.setFalling(false);
            return true;
        }

        //si no viene cayendo muere mario :(
        if (mp.equals(gp)) {
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

    @Override public boolean receiveInteraction(Goomba g)  { return false; }
    @Override public boolean receiveInteraction(ExitDoor d){ return false; }
    @Override public boolean receiveInteraction(Land l)    { return false; }
    @Override public boolean receiveInteraction(Mushroom m) { return false; }

    ////=====================FACTORIA==========================================
    public GameObject parse(String[] words, GameWorld game) {
        if (!GameObject.matchesType(words[1], "GOOMBA", "G"))  //es goomba??
            return null;

        Position pos = GameObject.parsePosition(words[0]); //parseo la posicion
        if (pos == null) return null;

        Goomba g = new Goomba(game, pos);

        //direccionn opcional
        if (words.length >= 3) {
            String w = words[2].toUpperCase();
            if (w.equals("RIGHT") || w.equals("R"))
                g.setDir(Action.RIGHT);
            else if (w.equals("LEFT") || w.equals("L"))
                g.setDir(Action.LEFT);;
        }

        return g;
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //para savee
        Position p = this.position;
        String dirStr = (dir == Action.LEFT ? "LEFT" : "RIGHT");
        return "(" + p.getRow() + "," + p.getCol() + ") Goomba " + dirStr;
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public GameObject copy(GameWorld newGame) { //para load/save, evito refs compartidas (FGC) ye
        Goomba g = new Goomba(newGame, new Position(position.getRow(), position.getCol()));
        g.setDir(this.dir);
        return g;
    }
}