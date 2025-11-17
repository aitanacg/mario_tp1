package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.logic.Action;
import tp1.logic.Game;

public class Mario extends MovingObject {

    //constructor vacio para facotria
    protected Mario() {
        super(null, null);
    }

    public enum Facing{ LEFT, RIGHT};

    private Facing facing = Facing.RIGHT;
    private boolean big = false;  //si fuera true, ocuparia la tile de arriba
    private boolean stopped = false; //para el icono STOP
    public int saltitosLeft = 0;
    private boolean jumping = false;
    private boolean justDidDown = false; //para que no se me buguee y no se me vaya volando


    public Mario(Game game, Position pos) {
        super(game, pos);
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
    public boolean isJumping() { return jumping;}
    public void setJumping(boolean j) { this.jumping = j;}

    @Override
    public boolean isFalling() {
        return super.falling;
    }

    @Override
    public void setFalling(boolean f) {
        super.falling = f;
    }

    public Game getGame() { return this.game; }

    //lo que ocupa
    public boolean occupies(Position p) { //si es big tmb el la de arriba
        if (p.equals(position)) return true;
        return big && p.equals(position.translate(0, -1));
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
     * no aplica ni gravedad, ni interacciones ni valida turnos o resta time (eso en Game)
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
        //mientras queden saltitos
        if (jumping && saltitosLeft > 0) {
            Position up = position.translate(0, -1);

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
                Position below = position.translate(0, +1);

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
            Position below = position.translate(0, +1);
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

            Position next = position.translate(0, +1);
            while (isInsideBoard(next) && !game.getGameObjectContainer().isSolidAt(next)) {
                position = next;
                next = position.translate(0, +1);
            }

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

    //mario ocupa casilla base p
    private boolean canOccupy(Position p) {
        if (!isInsideBoard(p)) return false;
        if (game.getGameObjectContainer().isSolidAt(p)) return false;

        if (big) {
            Position top = p.translate(0, -1);
            if (!isInsideBoard(top)) return false;
            if (game.getGameObjectContainer().isSolidAt(top)) return false;
        }
        return true;
    }

    private boolean isInsideBoard(Position p) {
        return p.getRow() >= 0 && p.getRow() < Game.DIM_Y
                && p.getCol() >= 0 && p.getCol() < Game.DIM_X;
    }

    //INTERACTIONS
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Goomba g) {
        //no fa res, la logica de matar y pisar ta en goomba.recieveInteraction(mario)
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

    //FACTORIA

    public GameObject parse(String[] words, Game game) {
        if (!GameObject.matchesType(words[1], "MARIO", "M"))
            return null;

        Position pos = GameObject.parsePosition(words[0]);
        if (pos == null) return null;

        Mario m = new Mario(game, pos);

        //dir opc
        if (words.length >= 3) {
            String w = words[2].toUpperCase();
            if (w.equals("LEFT") || w.equals("L"))
                m.setFacing(Facing.LEFT);
            else if (w.equals("RIGHT") || w.equals("R"))
                m.setFacing(Facing.RIGHT);
        }

        //tamano opc
        if (words.length >= 4) {
            String w = words[3].toUpperCase();
            if (w.equals("BIG") || w.equals("B"))
                m.setBig(true);
            else if (w.equals("SMALL") || w.equals("S"))
                m.setBig(false);
        }

        return m;
    }


}

