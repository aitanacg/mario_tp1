package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mushroom extends MovingObject {

    //const vacio para factoria
    protected Mushroom() {
        super(null, null);
    }

    //constructor
    public Mushroom(Game game, Position pos) {
        super(game, pos);
        this.dirX = 1; //default derecha
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public String getIcon() {
        return Messages.MUSHROOM;
    }

    @Override
    public void update() {

        //gravedad igual que goomba jsjs
        Position below = position.translate(0, +1);
        if (!game.getGameObjectContainer().isSolidAt(below)) {
            position = below;
            if (position.getRow() >= Game.DIM_Y) {
                die();
            }
            return;
        }

        //mov horizontal con rebote como goomba vamos
        int nextC = position.getCol() + (dirX == 0 ? 1 : dirX);
        int r = position.getRow();

        boolean hitsWall = (nextC < 0 || nextC >= Game.DIM_X);
        boolean solidAhead = !hitsWall &&
                game.getGameObjectContainer().isSolidAt(new Position(r, nextC));

        if (hitsWall || solidAhead) {
            dirX = (dirX == 0 ? -1 : -dirX);
            return;
        }

        position = new Position(r, nextC);
    }

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Mario m) {

        if (!m.isBig()) {
            m.setBig(true);
            game.addPoints(50); //no dice que me de points pero yo quiero puntos asi que eso
        }
        die(); //siempre se muere

        return true;
    }

    //a los otros los ignora
    @Override public boolean receiveInteraction(Goomba g) { return false; }
    @Override public boolean receiveInteraction(Land l) { return false; }
    @Override public boolean receiveInteraction(ExitDoor d) { return false; }

    //FACTORIA
    @Override
    public GameObject parse(String[] words, Game game) {

        if (!GameObject.matchesType(words[1], "MUSHROOM", "MUSH"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        return new Mushroom(game, pos);
    }
}