package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;

import tp1.logic.Game;

public class GameObjectFactory {

    //lista cositas
    private static final List<GameObject> availableObjects = Arrays.asList(
            new Land(),
            new ExitDoor(),
            new Goomba(),
            new Mario()
            //aqui creo que luego van el setoncios y el box
    );

    public static GameObject parse(String[] words, Game game) {
        for (GameObject proto : availableObjects) {
            GameObject g = proto.parse(words, game);
            if (g != null)
                return g;
        }
        return null;
    }
}
