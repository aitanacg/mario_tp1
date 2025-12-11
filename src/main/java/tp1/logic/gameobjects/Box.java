package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;
/**
 * Box
 * Objeto estatico y solido
 * Puede estar empty o full
 * Mario le debe dar desde abajo
 * Box no se mueve ni realiza movimiento automatico
 */

public class Box extends GameObject {

 private boolean isEmpty;

    public Box() { //vacio para factoria
        super();
        this.isEmpty = false;
    }

    public Box(GameWorld game, Position pos, boolean empty) { //constructor default
        super(game, pos);
        this.isEmpty = empty;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void update() {
        //tristemente box no se mueve
    }

    @Override
    public String getIcon() {
        return isEmpty ? Messages.BOX_EMPTY : Messages.BOX_FULL;
    }

    ////===================INTERACTIONS (DOUBLE DISPATCH)====================================
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    //mario le da a la caja
    public boolean receiveInteraction(Mario m) {

        Position mp = m.getPosition();
        Position bp = this.position;

        boolean marioDebajo = mp.equals(bp.down());//solo si es POR ABAJO

        if (!isEmpty && marioDebajo) {

            this.isEmpty = true; //caja vacia
            game.addPoints(50); //me da points yay

            Position arriba = bp.up(); //spawneo un mushroom encima
            if (arriba.isInBounds(Game.DIM_Y, Game.DIM_X)) {
                Mushroom mush = new Mushroom(game, arriba);
                game.getGameObjectContainer().add(mush);
            }
        }
        return true;
    }

    //los otros la ignoran tremendamente
    public boolean receiveInteraction(Goomba g) { return false; }
    public boolean receiveInteraction(Land l) { return false; }
    public boolean receiveInteraction(ExitDoor d) { return false; }
    public boolean receiveInteraction(Box b) { return false; }
    public boolean receiveInteraction(Mushroom m) { return false; }

    ////=====================FACTORIA==========================================
    @Override
    public GameObject parse(String[] words, GameWorld game) {

        if (!GameObject.matchesType(words[1], "BOX", "B"))
            return null;

        Position p = GameObject.parsePosition(words[0]); //en todas, me interpreta el (1,2)
        if (p == null) return null;

        boolean empty = false;

        if (words.length >= 3) { //en caso de box, puede estar o llena o empty
            String w = words[2].toUpperCase();
            if (w.equals("EMPTY") || w.equals("E"))
                empty = true;
            else if (w.equals("FULL") || w.equals("F"))
                empty = false;
        }

        return new Box(game, p, empty);
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //pal save, que no me salga tp1.logic.gameobjects.Goomba@123patatas
        Position p = this.position;
        String state = isEmpty ? "Empty" : "Full";
        return "(" + p.getRow() + "," + p.getCol() + ") Box " + state;
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public GameObject copy(GameWorld newGame) { //para load/save, evito refs compartidas (FGC), genero una copia independiente
        return new Box(newGame,
                new Position(position.getRow(), position.getCol()),
                this.isEmpty);
    }
}