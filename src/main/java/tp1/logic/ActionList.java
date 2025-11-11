package tp1.logic;

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

    //quita y devuelve la primera accion del list
    public Action extract() {
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
        return List.copyOf(actions);
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




   /*
    private int horizontalCount = 0;
    private int verticalCount = 0;
    private boolean hasLeft = false;
    private boolean hasRight = false;
    private boolean hasUp = false;
    private boolean hasDown = false;

    public void add(Action act) { // una
        switch (act) {
            case LEFT:
                if (!hasRight && horizontalCount < 4) {
                    actions.add(act);
                    hasLeft = true;
                    horizontalCount++;
                }
                break;
            case RIGHT:
                if (!hasLeft && horizontalCount < 4) {
                    actions.add(act);
                    hasRight = true;
                    horizontalCount++;
                }
                break;
            case UP:
                if (!hasDown && verticalCount < 4) {
                    actions.add(act);
                    hasUp = true;
                    verticalCount++;
                }
                break;
            case DOWN:
                if (!hasUp && verticalCount < 4) {
                    actions.add(act);
                    hasDown = true;
                    verticalCount++;
                }
                break;
            case STOP:
                if (!hasLeft && !hasRight) {
                    actions.add(act);
                }
                break;
        }
    }

    public void add(List<Action> acts) { //varias
        List<Action> filtered = new ArrayList<>();
        
        for (Action a : acts) {
            switch (a) {
                case LEFT:
                    if (!hasRight && horizontalCount < 4) {
                        filtered.add(a);
                        hasLeft = true;
                        horizontalCount++;
                    }
                    break;
                case RIGHT:
                    if (!hasLeft && horizontalCount < 4) {
                        filtered.add(a);
                        hasRight = true;
                        horizontalCount++;
                    }
                    break;
                case UP:
                    if (!hasDown && verticalCount < 4) {
                        filtered.add(a);
                        hasUp = true;
                        verticalCount++;
                    }
                    break;
                case DOWN:
                    if (!hasUp && verticalCount < 4) {
                        filtered.add(a);
                        hasDown = true;
                        verticalCount++;
                    }
                    break;
                case STOP:
                    if (!hasLeft && !hasRight) {
                        filtered.add(a);
                    }
                    break;
            }
        }

        this.actions.addAll(filtered);
    }

    public Action extract() { //quita y devuelve primera del array
        if (actions.isEmpty()) return null;
        return actions.remove(0);
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public void clear() {
        actions.clear();
        horizontalCount = 0;
        verticalCount = 0;
        hasLeft = false;
        hasRight = false;
        hasUp = false;
        hasDown = false;
    }

    public int size() {
        return actions.size();
    }


    */


