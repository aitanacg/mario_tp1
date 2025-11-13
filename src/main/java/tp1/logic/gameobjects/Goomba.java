package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;

public class Goomba extends MovingObject {

    private int dx = -1;

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
        int nextC = position.getCol() + (dirX == 0 ? -1 : dirX); //si dirX=0 pa la izq
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
            System.out.println("Mario pisa a goomba");
            die();
            game.addPoints(100);
            m.setFalling(false);
            return true;
        }

        // Si no viene cayendo muere mario :(
        if (mp.equals(gp)) {
            System.out.println("Mario muere por chocar con goomba");
            game.marioDies();
            return true;
        }
        return false;
    }

    @Override public boolean receiveInteraction(Goomba g)  { return false; }
    @Override public boolean receiveInteraction(ExitDoor d){ return false; }
    @Override public boolean receiveInteraction(Land l)    { return false; }

}