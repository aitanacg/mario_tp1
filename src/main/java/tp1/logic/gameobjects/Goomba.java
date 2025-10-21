package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;


public class Goomba {
    private final Game game;
    private Position pos;
    private int dx = -1;

    private boolean alive = true;

    public boolean isAlive() {
        return alive;
    }

   
    public Goomba(Game game, Position pos) {
        this.game = game;
        this.pos = pos;  
    }

    public Position getPosition() {
        return pos;
    }

    public void setPosition(Position p) {
        this.pos = p;
    } //movimiento mas adelante

    public String getIcon() {
        return tp1.view.Messages.GOOMBA;
    }
    
    public void update() {
         
        int r = pos.getRow();
        int c = pos.getCol();

        // 1) Gravedad: mirar la celda de debajo
        String below = game.positionToString(c, r + 1);
        boolean hasFloor = tp1.view.Messages.LAND.equals(below);
        if (!hasFloor) {
            // cae una casilla
            pos = new Position(r + 1, c);
            // si se sale por abajo, muere
            if (pos.getRow() >= Game.DIM_Y)
                game.removeGoomba(this);
            return;
        }

        // 2) En suelo: intentar avanzar 1 en la dirección dx
        int nextC = c + dx;
        boolean hitsWall = (nextC < 0 || nextC >= Game.DIM_X);
        boolean landAhead = !hitsWall && tp1.view.Messages.LAND.equals(
                game.positionToString(nextC, r));

        if (hitsWall || landAhead) {
            dx = -dx; //giroo
            return;
        }

        pos = new Position(r, nextC);//delante
        
    }
 
    public boolean receiveInteraction(Mario other) {
        this.alive = false;
        game.addPoints(100);
        return true;
        
    }


}

