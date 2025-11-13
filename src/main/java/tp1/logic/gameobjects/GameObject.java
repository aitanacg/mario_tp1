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

}