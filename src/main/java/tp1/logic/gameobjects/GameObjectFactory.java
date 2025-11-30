package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;

import tp1.logic.Game;
import tp1.logic.Position;

public class GameObjectFactory {

    //lista cositas
    private static final List<GameObject> availableObjects = Arrays.asList(
            new Land(),
            new ExitDoor(),
            new Goomba(),
            new Mario(),
            new Mushroom(),
            new Box()
    );

    public static GameObject parse(String[] words, Game game) throws ObjectParseException, OffBoardException  {
        for (GameObject Objetito : availableObjects) {
            GameObject obj = Objetito.parse(words, game);

            if (obj != null) {
                Position p = obj.getPosition();
                if (!p.isInBounds(Game.DIM_X, Game.DIM_Y)) {//valido pos
                    throw new OffBoardException("Object position is off the board: \"" + String.join(" ", words) + "\"");
                }
                return obj;
            }
        }
        throw new ObjectParseException("Unknown game object: \"" + String.join(" ", words) + "\"");
    }

}
