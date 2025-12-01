package tp1.logic;
//config inicial completa, el game puede cargar cuando hago load o reset, interfaz weno
import java.util.List;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Mario;

public interface GameConfiguration {

    int getRemainingTime();
    int getPoints();
    int getLives();

    Mario getMario();
    List<GameObject> getNPCObjects();  //npcs pov no mario
}