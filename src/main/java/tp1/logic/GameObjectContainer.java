package tp1.logic;
//almacena todos los obj y gestiona interacciones, actualiza turnos y limpia muertos
import tp1.logic.gameobjects.*;
import tp1.view.Messages;
import java.util.ArrayList;
import java.util.List;

public class GameObjectContainer {

    private final List<GameObject> objects = new ArrayList<>();

    public void add(GameObject obj) {
        if (obj == null) return;
        Position p = obj.getPosition();
        //no en la misma casilla
        for (GameObject o : objects) {
            if (o.getPosition().equals(p)) return;
        }
        objects.add(obj);
    }

    //cositas del tablero
    public boolean isSolidAt(Position p) {
        for (GameObject obj : objects) {
            if (obj.isSolid() && obj.occupies(p)) return true;
        }
        return false;
    }

    public String stringAt(Position p) {
        for (GameObject obj : objects) {
            if (obj.occupies(p)) return obj.getIcon();
        }
        return Messages.EMPTY;
    }

    //el ciclo de juego real
    public void updateAll() {
        for (GameObject obj : objects) obj.update();  //cada obj se actualiza, unos hacen cosas y otros no
        //objects =[Mario, Goomba, Mushroom, Land, ExitDoor, Box], bucle for each
        doInteractions(); //interacciones (b recibe a a en el dd)
        //hace la doble interacción si estan dos en la misma casilla, DOUBLE DISPATCH
        clean(); //recojo cadaveres
    }

    public void doInteractions() {
        final int n = objects.size(); //primero miro cuantos hay
        for (int i = 0; i < n; i++) {
            GameObject a = objects.get(i);
            if (!a.isAlive()) continue; //si no, me salgo del for

            for (int j = i + 1; j < n; j++) { //empiexa con i+1 para no repetir pares
                GameObject b = objects.get(j);
                if (!b.isAlive()) continue;
                //mira si estan el la misma posicion o apilados, interactuan mediante double dispatch
                boolean overlap = a.occupies(b.getPosition()) || b.occupies(a.getPosition()) || areStacked(a, b);
                if (overlap) {
                    a.interactWith(b); //precioso double dispatch, asi cada uno decide su reacción
                    b.interactWith(a);
                    //el dd permita a cada obj interactuar con otro sin preguntar su clase con instanceof
                    //el interactWith solo decide con quien interactua, el recieve interacion hace cosas
                    //Ex: goomba recibe a mario falling: goomba muere
                    //Ex: Mario recibe a goomba y no cae: mario muere
                }
            }
        }
        clean(); //elimina muertos
    }

    private boolean areStacked(GameObject a, GameObject b) {
        Position pa = a.getPosition();
        Position pb = b.getPosition();

        //return (pa.getCol() == pb.getCol() &&
        //        (pa.getRow() + 1 == pb.getRow() || pb.getRow() + 1 == pa.getRow()));


        //a justo arriba de b o al reves

        return pa.up().equals(pb) || pb.up().equals(pa);
    }

    private void clean() {
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (!objects.get(i).isAlive()) {
                objects.remove(i);
            }
        }
    }

    public void clear() { objects.clear(); }
}
