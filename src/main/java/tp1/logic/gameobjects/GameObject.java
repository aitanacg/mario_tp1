package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;

public abstract class GameObject implements GameItem {

    protected Position position;
    protected Game game;
    protected boolean alive = true;

    public GameObject(Game game, Position position) {
        this.game = game;
        this.position = position;
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
    }

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

    @Override
    public abstract boolean interactWith(GameItem item);

    //sobreescritos por clases
    public boolean receiveInteraction(Mario m)   { return false; }
    public boolean receiveInteraction(Goomba g)  { return false; }
    public boolean receiveInteraction(ExitDoor e){ return false; }
    public boolean receiveInteraction(Land l)    { return false; }

    //FACTORIA................. si

    public GameObject parse(String[] words, Game game) {
        return null; //por default nada
    }
    //dev true si words[1] coincide con tipo
    protected static boolean matchesType(String word, String full, String shortName) {
        String w = word.toUpperCase();
        return w.equals(full) || w.equals(shortName);
    }

    //interpreta mis coords de (miau,miau)
    protected static Position parsePosition(String s) {
        try {
            s = s.replace("(", "").replace(")", "");
            String[] p = s.split(",");
            int r = Integer.parseInt(p[0]);
            int c = Integer.parseInt(p[1]);
            return new Position(c, r); // OJO con tu sistema si invertías coordenadas
        } catch (Exception e) {
            return null;
        }
    }

}