package tp1.logic.gameobjects;

import tp1.logic.Position;

public class Land {
    private final Position pos;

    public Land(Position pos) {
        this.pos = pos; 
    }

    public Position getPosition(){
        return pos;
    }

    public String getIcon(){
        return tp1.view.Messages.LAND;
    }

}
