package tp1.logic.gameobjects;

import tp1.logic.Position;
/**Interfaz minima que implementan los objetos del juego
 * lo implementa GameObject y de ahi extienden los Objetitos
 */
public interface GameItem {

    boolean isSolid();
    boolean isAlive();
    boolean isInPosition(Position pos);

    boolean interactWith(GameItem item);

    boolean receiveInteraction(Land obj);
    boolean receiveInteraction(ExitDoor obj);
    boolean receiveInteraction(Mario obj);
    boolean receiveInteraction(Goomba obj);
    boolean receiveInteraction(Mushroom obj);
    boolean receiveInteraction(Box obj);
    boolean receiveInteraction(StarBox obj);
    boolean receiveInteraction(Brick obj);
    boolean receiveInteraction(Bomb obj);
    boolean receiveInteraction(SolidIsLava obj);
}