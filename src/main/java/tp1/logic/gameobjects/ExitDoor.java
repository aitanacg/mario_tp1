package tp1.logic.gameobjects;

import tp1.logic.Position;

public class ExitDoor {
    private final Position pos;

    public ExitDoor(Position pos) {
        this.pos = pos;
    }

    public Position getPosition() {
        return pos;
    }

    public String getIcon() {
        return tp1.view.Messages.EXIT_DOOR;
    }
}
