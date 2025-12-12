package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
/**Clase abstracta para todos los objetos, implementa todo lo de GameItem
 * Es el default de todos los metodos obligatorios
 * Los objetos heredan estos metodos y los pueden implementar
 */
public abstract class GameObject implements GameItem {

    protected Position position;
    protected GameWorld game;
    protected boolean alive = true;

    public GameObject(GameWorld game, Position position) {
        this.game = game;
        this.position = position;
    }

    protected GameObject() {
        this.game = null;
        this.position = new Position(0, 0); //posicion tontita
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
    public void die() {
        alive = false;
    }
    public void revive() {
        alive = true;
    } //quien sabe jsjs

    @Override
    public boolean isSolid() {
        return false; //por defecto
    }
    @Override
    public boolean isInPosition(Position pos) {
        return occupies(pos);
    }
    public boolean occupies(Position p) { return position.equals(p); }

    public abstract void update(); //cada subclase hace el suyo lo suyo
    public abstract String getIcon(); //dibujo

    ////=====================INTERACTIONS==========================================
    @Override
    public abstract boolean interactWith(GameItem item);

    public boolean receiveInteraction(Mario m)   { return false; }
    public boolean receiveInteraction(Goomba g)  { return false; }
    public boolean receiveInteraction(ExitDoor e){ return false; }
    public boolean receiveInteraction(Land l)    { return false; }
    public boolean receiveInteraction(Mushroom m) {
        return false;
    }
    public boolean receiveInteraction(Box b) { return false; }
    public boolean receiveInteraction(StarBox b) { return false; }
    //usamos el double dispatch, para seber quien hace que al chocar dos objetos

    ////=====================FACTORIA==========================================
    public GameObject parse(String[] words, GameWorld game) {
        return null; //por default nada
    }
    //dev true si words[1] coincide con tipo
    protected static boolean matchesType(String word, String full, String shortName) { //para no repetir el G GOOMBAo lo que sea
        String w = word.toUpperCase();
        return w.equals(full) || w.equals(shortName);
    }

    //interpreta mis coords de (mi,au)
    protected static Position parsePosition(String s) {
        try {
            s = s.replace("(", "").replace(")", "");
            String[] p = s.split(",");
            int r = Integer.parseInt(p[0]);
            int c = Integer.parseInt(p[1]);
            return new Position(r, c);
        } catch (Exception e) {
            return null;
        }
    }
    ////=====================COPIA (LOAD)==========================================
    public abstract GameObject copy(GameWorld newGame); //los objetos deben auntar al game actual, no al viejo (para lo de ficheritos, save, load)
}