package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;
import tp1.logic.Action;

public class Goomba extends MovingObject {

    //constructor vacio para factoria
    protected Goomba() {
        super(null, null);
    }

    public Goomba(Game game, Position pos) {
        super(game,pos);
        this.dir = Action.LEFT;
    }

    @Override
    public String getIcon() {
        return tp1.view.Messages.GOOMBA;
    }


    //aplica gravedad, mov horizontal y rebota
    @Override
    public void update() {

        //gravedad
        Position below = position.down();
        if (!game.getGameObjectContainer().isSolidAt(below)) {
            position = below;
            //si cae fuera del tablero muere
            if (position.getRow() >= Game.DIM_Y) die();
            return;
        }

//        //mov horizontal con rebote boing
//        int nextC = position.getCol() + (dirX == 0 ? -1 : dirX); //si no tiene dirX va para la izq por def
//        int r = position.getRow();
//
//        boolean hitsWall = (nextC < 0 || nextC >= Game.DIM_X);
//        boolean solidAhead = !hitsWall && game.getGameObjectContainer().isSolidAt(new Position(r, nextC));
//
//        if (hitsWall || solidAhead) {
//            dirX = (dirX == 0 ? +1 : -dirX);
//            return;
//        }
//
//        position = new Position(r, nextC);
        // ===== MOVIMIENTO HORIZONTAL =====
        Position nextPos = position.next(dir);

        boolean hitsWall = !nextPos.isInBounds(Game.DIM_X, Game.DIM_Y);
        boolean solidAhead = game.getGameObjectContainer().isSolidAt(nextPos);

        // rebote
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

    //DOUBLE DISPATCH

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Mario m) {

        Position mp = m.getPosition();
        Position gp = this.position;

        //boolean pisa = m.isFalling() && mp.equals(gp);
        Position marioAbajo = mp.down();
        boolean pisa = m.isFalling() && marioAbajo.equals(gp);
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

    //FACTORIA

    public GameObject parse(String[] words, Game game) {
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


}