package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.logic.Action;
import tp1.logic.Game;

public class Mario extends MovingObject {

    public Mario() {//constructor vacio para factoria
        super();
    }
    public Mario(GameWorld game, Position pos) {
        super(game, pos);
    }

    public enum Facing{ LEFT, RIGHT};
    private Facing facing = Facing.RIGHT;
    private boolean big = false;  //si fuera true, ocuparia la tile de arriba
    private boolean stopped = false; //para el icono STOP
    public int saltitosLeft = 0;
    private boolean jumping = false;
    private boolean justDidDown = false; //para que no se me buguee y no se me vaya volando

    private int turnosInmortal=0;

    public void activateStar(int turns){
        turnosInmortal=turns;
    }
    public boolean chetado(){
        return turnosInmortal > 0;
    }

    //basics
    public Position getPosition() {
        return position;
    }
    public Facing getFacing(){
        return facing;
    }
    public void setFacing(Facing f){
        this.facing = f;
    }
    public boolean isBig() {
        return big;
    }
    public void setBig(boolean b) {
        this.big = b;
    }
    public boolean isJumping() { return jumping;} //los dejo por si las moscas
    public void setJumping(boolean j) { this.jumping = j;}

    @Override
    public boolean isFalling() {
        return super.falling;
    }

    @Override
    public void setFalling(boolean f) {
        super.falling = f;
    }

    public GameWorld getGame() { return this.game; }

    public boolean occupies(Position p) { //para ver si ocupa una o dos tiles
        if (p.equals(position)) return true;
        return big && p.equals(position.up());
    }

    private boolean canOccupy(Position p) {//mario ocupa casilla base p
        if (!isInsideBoard(p)) return false;
        if (game.getGameObjectContainer().isSolidAt(p)) return false;

        if (big) {
            Position top = p.up();
            if (!isInsideBoard(top)) return false;
            if (game.getGameObjectContainer().isSolidAt(top)) return false;
        }
        return true;
    }

    private boolean isInsideBoard(Position p) {
        return p.isInBounds(Game.DIM_Y, Game.DIM_X);
    }

    @Override
    public String getIcon() {
        if (stopped) {
            return tp1.view.Messages.MARIO_STOP;
        } else {
            if (facing == Facing.LEFT) {
                return tp1.view.Messages.MARIO_LEFT;
            } else {
                return tp1.view.Messages.MARIO_RIGHT;
            }
        }
    }

    /**
     * Para el movimiento, Mario puede hacer un unico movimiento
     * consume aciiones, mueve a mario, aplica gravedad, gestiona saltitos, detecta muerte por caer resetea flags
     */
    @Override
    public void update() {

        int moves = 0; //cuenta movimientos
        if (stopped) jumping = false;

        while (!game.getActions().isEmpty() && moves < 4) {
            Action act = game.getActions().extract();
            if (act == null) break;

            paso(act);  //se mueve pasito a pasito

            if (act != Action.STOP) {//stop no cuenta
                game.consumeTime(1);
                moves++;
            }
        }
        if (!jumping && saltitosLeft > 0) saltitosLeft = 0;
        //gravedad y salto
        if (jumping && saltitosLeft > 0) {//mientras queden saltitos
            Position up = position.up();

            if (!game.getGameObjectContainer().isSolidAt(up)) {
                position = up;
                saltitosLeft--;
                setFalling(false);
            } else {
                saltitosLeft = 0; //tope si choca
            }
            if (saltitosLeft == 0) {
                jumping = false;
                setFalling(true);
            }
        }
        //gravedad si no esta jumping
        else {
            if (!jumping && !justDidDown) {
                Position below = position.down();

                if (!game.getGameObjectContainer().isSolidAt(below)) {
                    position = below;
                    setFalling(true);
                    if (below.getRow() >= Game.DIM_Y) {
                        game.marioDies();
                    }
                } else {
                    setFalling(false);
                }
            }
        }
        justDidDown = false;
        if (turnosInmortal > 0) {
            turnosInmortal--;
        }
    }

    //aplica una accion
    public boolean paso(Action a) {
        if (a == null) return false;

        if (a == Action.STOP) {
            stopped = true;
            jumping = false;
            setFalling(false);
            saltitosLeft = 0;
            return true;
        }

        stopped = false;
        //direccion que queremos
        if (a == Action.LEFT)  facing = Facing.LEFT;
        if (a == Action.RIGHT) facing = Facing.RIGHT;
        if (a == Action.LEFT || a == Action.RIGHT) {
            jumping = false;
            saltitosLeft = 0;
        }

        //salto
        if (a == Action.UP) {
            if (jumping || saltitosLeft > 0) return true;
            Position below = position.down();
            //solo inicia salto si está tocando el suelo
            if (game.getGameObjectContainer().isSolidAt(below)) {
                jumping = true;
                saltitosLeft = 4;
                setFalling(false);
            }
            return true;
        }
        //bajar
        if (a == Action.DOWN) { //cae hasta suelo :)
            // cancela el salto para que no se me vaya flotando
            jumping = false;
            saltitosLeft = 0;
            setFalling(true);
            justDidDown = true;

            Position next = position.down();
            while (isInsideBoard(next) && !game.getGameObjectContainer().isSolidAt(next)) {
                position = next;
                next = position.down();
            }
            return true;
        }

        if (a == Action.RIGHTUP || a == Action.LEFTUP) {
            // solo permitimos subir si está ONGROUND
            Position below = position.down();
            boolean onGround = game.getGameObjectContainer().isSolidAt(below);
            if (!onGround) return true; // no está en el suelo → no sube
            // se mueve diagonal arriba
            Position next = position.next(a);
            if (canOccupy(next)) {
                position = next;
                justDidDown = true;
                setFalling(false);
            }
            return true;
        }

        if (a == Action.RIGHTDOWN || a == Action.LEFTDOWN) {
            // movimiento diagonales abajo → dejar gravedad normal
            Position next = position.next(a);
            if (canOccupy(next)) position = next;
            justDidDown = true; //evita doble gravedad
            return true;
        }

        //mover si es ocupable
        Position next = position.next(a);
        if (canOccupy(next)) {
            position = next;
        }
        //accion procesada
        return true;
    }

    ////===================INTERACTIONS (DOUBLE DISPATCH)====================================
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }
    //llama al recieveInteraction del otro objeto y ese objeto decide que pasa
    // por ej el goomba decide que le pasa a mario, si muere o no

    @Override
    public boolean receiveInteraction(Goomba g) {
        //no fa res, la logica de matar y pisar ta en goomba.recieveInteraction(mario)
        if (turnosInmortal > 0) {
            // si tengo estrella, el goomba muere automáticamente
            g.die();
            game.addPoints(100);
            return true;
        }

        return false;
    }

    @Override
    public boolean receiveInteraction(ExitDoor d) {
        game.setPlayerWon();
        return true;
    }

    @Override
    public boolean receiveInteraction(Land l) {
        //land y mario no se quieren
        return false;
    }

    @Override
    public boolean receiveInteraction(Mushroom m) {
        if (!this.isBig()) {  //mario toca seta
            this.setBig(true);
            game.addPoints(50);
        }
        m.die();
        return true;
    }

    ////=====================FACTORIA==========================================
    @Override
    public Mario parse(String[] words, GameWorld game){  //puedo devolver mario aunque el papa devuelva GameObject para el fgc
        if (!GameObject.matchesType(words[1], "MARIO", "M"))  //es mario?
            return null;

        Position pos = GameObject.parsePosition(words[0]); //identifico la posicion
        if (pos == null) return null;

        Mario m = new Mario(game, pos);

        //dir opcional
        if (words.length >= 3) {
            String w = words[2].toUpperCase();
            if (w.equals("LEFT") || w.equals("L"))
                m.setFacing(Facing.LEFT);
            else if (w.equals("RIGHT") || w.equals("R"))
                m.setFacing(Facing.RIGHT);
        }

        //tamano opcional
        if (words.length >= 4) {
            String w = words[3].toUpperCase();
            if (w.equals("BIG") || w.equals("B"))
                m.setBig(true);
            else if (w.equals("SMALL") || w.equals("S"))
                m.setBig(false);
        }

        return m;
    }

    ////=====================SERIALIZACION (SAVE)==========================================
    @Override
    public String toString() { //para el save, el mas tocho
        Position p = this.position;

        String dirStr = (facing == Facing.LEFT ? "LEFT" : "RIGHT");
        String sizeStr = big ? "BIG" : "SMALL";

        return "(" + p.getRow() + "," + p.getCol() + ") Mario " + dirStr + " " + sizeStr;
    }

    ////=====================COPIA (LOAD)==========================================
    @Override
    public Mario copy(GameWorld newGame) {//para load/save, evito refs compartidas (FGC), importante el mario
        Mario m = new Mario(newGame, new Position(position.getRow(), position.getCol()));

        m.setBig(this.isBig());
        m.setFacing(this.getFacing());
        m.setFalling(this.isFalling());
        m.setDir(this.getDir());

        return m;
    }
}