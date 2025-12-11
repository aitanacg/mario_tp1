package tp1.logic;

import java.util.List;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Mario;
/**
 * Config inicial completa, cuando hago load o reset
 */
public interface GameConfiguration {

    int getRemainingTime();
    int getPoints();
    int getLives();

    Mario getMario();
    List<GameObject> getNPCObjects();  //npcs pov no mario
    String getFileName();
}