package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.logic.Game;
import tp1.logic.GameWorld;
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

    public static GameObject parse(String[] words, GameWorld world) throws ObjectParseException, OffBoardException  { //cambio Game por Gameworld
        for (GameObject Objetito : availableObjects) {
            GameObject obj = Objetito.parse(words, world);

            if (obj != null) {
                Position p = obj.getPosition();
                if (!p.isInBounds(Game.DIM_Y, Game.DIM_X)) {//valido pos PQ ESTABA AL REVES AITANA???
                    throw new OffBoardException("Object position is off the board: \"" + String.join(" ", words) + "\"");
                }
                return obj;
            }
        }
        throw new ObjectParseException("Unknown game object: \"" + String.join(" ", words) + "\"");
    }
}