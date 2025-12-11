package tp1.logic;

import java.util.ArrayList;
import java.util.List;
/**
 * Cola de acciones por turno: max.4
 * mario consume en su update(), el STOP no cuenta
 */
public class ActionList {
    private static final int MAX_ACTIONS = 4;
    private List<Action> actions = new ArrayList<>();

    //anade accion si aun no alcanza al max
    //devuelve true si lo hizo bien

    public boolean add(Action act){//anade una accion, si<MAX, true si todo correcto
        if (act == null) return false;
        if (actions.size() >= MAX_ACTIONS) return false; // ya hay 4

        actions.add(act);
        return true;
    }

    public void add(List<Action> acts) {//anade varias acciones
        if (acts == null) return;
        for (Action a : acts) {
            if (actions.size() < MAX_ACTIONS) {
                actions.add(a);
            } else {
                break; //ignora las que sobran
            }
        }
    }

    public Action extract() {//quita y devuelve la primera accion del list
        if (actions.isEmpty()) return null;
        return actions.remove(0);
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }
    public void clear() { //elimina todas las actions
        actions.clear();
    }
    public int size() {
        return actions.size();
    }

    public List<Action> asList() {
        return new ArrayList<>(actions);
    }//devuelve una copia

    public boolean isValid() {
        return actions.size() <= MAX_ACTIONS;
    }//si cumple el limite, pos si acaso

    @Override
    public String toString() {
        return actions.toString();
    }
}