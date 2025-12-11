package tp1.logic;
//almacena todos los obj y gestiona interacciones, actualiza turnos y limpia muertos
import tp1.logic.gameobjects.*;
import tp1.view.Messages;
import java.util.ArrayList;
import java.util.List;
/**
 * Almacena todods los objetos, gestiona interacciones, actualiza turnos, limpia muertos
 */
public class GameObjectContainer {

    private final List<GameObject> objects = new ArrayList<>();

    public void add(GameObject obj) {
        if (obj== null) {
            return;
        }
        Position p = obj.getPosition();
        //no en la misma casilla
        for (GameObject o : objects) {
            if (o.getPosition().equals(p)) return;
        }
        objects.add(obj);
    }

    ////==================CONSULTAS DE TABLERO============================
    public boolean isSolidAt(Position p) {
        for (GameObject obj : objects) {
            if (obj.isSolid() && obj.occupies(p)) return true;
        }
        return false;
    }

    public String stringAt(Position p) { //dibuja
        for (GameObject obj : objects) {
            if (obj.occupies(p)) return obj.getIcon();
        }
        return Messages.EMPTY;
    }
    ////==========================CICLO DE JUEGO===============================
    /**
     * Para cada objeto gestiona interacciones con double dispatch, y luego limpia muertos
     */
    public void updateAll() {
        for (GameObject obj : objects) obj.update();
        doInteractions(); //interacciones (b recibe a a en el dd)
        clean(); //recojo cadaveres
    }

    ////==========================INTERACCIONES===============================
    /**
     * Toda la logica de gestion de interacciones
     * Itera todas las parejas, sin duplicados
     * Double dispatch permite a cada objeto interactuar con otros
     * interactWith decide con quien interactua
     * recieveInteracion realiza acciones de respuesta
     * Ex: goomba recibe a mario falling: goomba muere
     * Ex: Mario recibe a goomba y no cae: mario muere
     */
    public void doInteractions() {
        final int n = objects.size(); //primero miro cuantos hay
        for (int i = 0; i < n; i++) {
            GameObject a = objects.get(i);
            if (!a.isAlive()) continue; //si no, me salgo del for

            for (int j = i + 1; j < n; j++) { //empieza con i+1 para no repetir pares
                GameObject b = objects.get(j);
                if (!b.isAlive()) continue;
                //mira si estan el la misma posicion o apilados
                boolean overlap = a.occupies(b.getPosition()) || b.occupies(a.getPosition()) || areStacked(a, b);
                if (overlap) {
                    a.interactWith(b); //DD
                    b.interactWith(a);
                }
            }
        }
        clean(); //elimina muertos
    }

    private boolean areStacked(GameObject a, GameObject b) {
        Position pa = a.getPosition();
        Position pb = b.getPosition();
        return pa.up().equals(pb) || pb.up().equals(pa);
    }

    ////==========================LIMPIEZA Y UTILIDADES===============================
    private void clean() {
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (!objects.get(i).isAlive()) {
                objects.remove(i);
            }
        }
    }

    public void clear() { objects.clear(); }

    public List<GameObject> getObjects() {
        return objects;
    }
}