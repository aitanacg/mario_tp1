package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;

public class Goomba extends MovingObject {

    //constructor vacio para factoria
    protected Goomba() {
        super(null, null);
    }

    public Goomba(Game game, Position pos) {
        super(game,pos);
    }

    @Override
    public String getIcon() {
        return tp1.view.Messages.GOOMBA;
    }

    @Override
    public void update() {

        //gravedad
        Position below = position.translate(0, +1);
        if (!game.getGameObjectContainer().isSolidAt(below)) {
            position = below;
            //si cae fuera del tablero muere
            if (position.getRow() >= Game.DIM_Y) die();
            return;
        }

        //mov horizontal con rebote boing
        int nextC = position.getCol() + (dirX == 0 ? -1 : dirX); //si no tiene dirX va para la izq por def
        int r = position.getRow();

        boolean hitsWall = (nextC < 0 || nextC >= Game.DIM_X);
        boolean solidAhead = !hitsWall && game.getGameObjectContainer().isSolidAt(new Position(r, nextC));

        if (hitsWall || solidAhead) {
            dirX = (dirX == 0 ? +1 : -dirX);
            return;
        }

        position = new Position(r, nextC);
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

        boolean pisa = m.isFalling() && mp.equals(gp);
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
        if (!GameObject.matchesType(words[1], "GOOMBA", "G"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        Goomba g = new Goomba(game, pos);

        // Dirección opcional
        if (words.length >= 3) {
            String w = words[2].toUpperCase();
            if (w.equals("RIGHT") || w.equals("R"))
                g.setDirX(1);
            else if (w.equals("LEFT") || w.equals("L"))
                g.setDirX(-1);
        }

        return g;
    }


}