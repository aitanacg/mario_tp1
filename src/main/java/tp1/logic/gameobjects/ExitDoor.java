package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObject {

    public ExitDoor(Game game, Position pos) {
        super(game, pos);
    }

    @Override
    public void update() {
        //no se mueve la door
    }

    @Override
    public String getIcon() {
        return Messages.EXIT_DOOR;
    }

    //DOUBLE DISPATCH

    @Override
    public boolean interactWith(GameItem other) {
        if (other.isInPosition(this.position)) {
            return other.receiveInteraction(this);
        }
        return false;
    }

    //INTERACTIONS

    @Override
    public boolean receiveInteraction(Mario m) {
        game.setPlayerWon();
        return true;
    }

    @Override
    public boolean receiveInteraction(Goomba g) {
        return false; //le importa bien poco los goombas yk
    }

    @Override
    public boolean receiveInteraction(ExitDoor d) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Land l) {
        return false;
    }
}