package tp1.logic;

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

    //el juego ciclo
    public void updateAll() {
        for (GameObject obj : objects) obj.update();  //cada obj se actualiza
        doInteractions(); //interacciones (b recibe a a en el dd)
        clean(); //recojo cadaveres
    }

    public void doInteractions() {
        final int n = objects.size();
        for (int i = 0; i < n; i++) {
            GameObject a = objects.get(i);
            if (!a.isAlive()) continue;

            for (int j = i + 1; j < n; j++) {
                GameObject b = objects.get(j);
                if (!b.isAlive()) continue;

                boolean overlap = a.occupies(b.getPosition()) || b.occupies(a.getPosition()) || areStacked(a, b);
                if (overlap) {
                    a.interactWith(b);
                    b.interactWith(a);
                }
            }
        }
        clean();
    }

    private boolean areStacked(GameObject a, GameObject b) {
        Position pa = a.getPosition();
        Position pb = b.getPosition();

        return (pa.getCol() == pb.getCol() &&
                (pa.getRow() + 1 == pb.getRow() || pb.getRow() + 1 == pa.getRow()));
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
