package tp1.logic;
//es la cola de acciones por turno: max 4
//mario las consume en su update, por ej el STOP no consume ni t ni cuenta como mov
import java.util.ArrayList;
import java.util.List;

public class ActionList {
    private static final int MAX_ACTIONS = 4;
    private List<Action> actions = new ArrayList<>();

    //anade accion si aun no alcanza al max
    //devuelve true si lo hizo bien

    public boolean add(Action act){
        if (act == null) return false;
        if (actions.size() >= MAX_ACTIONS) return false; // ya hay 4

        actions.add(act);
        return true;
    }
    //anade lista de acciones
    public void add(List<Action> acts) {
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

    //devuelve una copia
    public List<Action> asList() {
        return new ArrayList<>(actions);
    }

    //si cumple el limite, pos si acaso
    public boolean isValid() {
        return actions.size() <= MAX_ACTIONS;
    }

    @Override
    public String toString() {
        return actions.toString();
    }
}


