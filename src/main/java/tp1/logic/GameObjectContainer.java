package tp1.logic;

import tp1.logic.gameobjects.*; //land, goomba, exitDoor, mario
import tp1.view.Messages;

public class GameObjectContainer {
	
	private static final int MAX_LANDS = Game.DIM_X * Game.DIM_Y;
	private static final int MAX_GOOMBAS = 64;

	private final Land[] lands = new Land[MAX_LANDS];
	private int nLands = 0;

	private final Goomba[] goombas = new Goomba[MAX_GOOMBAS];
	private int nGoombas = 0;

	private ExitDoor exit = null;
	private Mario mario = null;


	//add sobrecargados
	public boolean isSolidAt(Position p) {
		for (int i = 0; i < nLands; i++) {   //miro si hay un land en p
			if (lands[i].getPosition().equals(p))
				return true;
		}
		return false;
	}

	public boolean hasEnemyAt(Position p) { //miro si hay goomba que me quiera matar (para mario update y dibujar goomba)
		for (int i = 0; i < nGoombas; i++) {
			if (goombas[i].isAlive() && goombas[i].getPosition().equals(p))
				return true;
		}
		return false;
	}

	//Land es solid
	public void add(Land land){
		if (land==null) return;
		Position p = land.getPosition();
		//si ya hay land
		for (int i= 0; i<nLands; i++){
			if(lands[i].getPosition().equals(p))return;
		}
		//hay otra cosa
		if(exit != null && exit.getPosition().equals(p))return;
		if(mario != null && mario.getPosition().equals(p))return;
		for (int i = 0; i<nGoombas; i++){
			if(goombas[i].getPosition().equals(p))return;
		}
		//anado
		if(nLands < lands.length) lands[nLands++] = land;
	}

	//goomba no es solido, pero no puede estar sobre land
	public void add(Goomba g) {
        if (g == null) return;
        Position p = g.getPosition();
        //hay un land
		for (int i = 0; i<nLands; i++){
			if(lands[i].getPosition().equals(p)) return;
		}
		if (nGoombas < goombas.length) goombas[nGoombas++]=g;
    }

	public void removeGoomba(Goomba g) {
		for (int i = 0; i < nGoombas; i++) {
			if (goombas[i] == g) {
				goombas[i] = goombas[nGoombas - 1];//compacto vector
				goombas[nGoombas - 1] = null;
				nGoombas--;
				return;
			}
		}
	}

	public boolean goombaAt(Position p) {
		for (int i = 0; i < nGoombas; i++) {
			if (goombas[i] != null && goombas[i].getPosition().equals(p)) {
				return true;
			}
		}
		return false;
	}


    public void doInteractions() {
        if (mario == null) return;
        Position mp = mario.getPosition();

        for (int i = 0; i < nGoombas; i++) {
            Goomba g = goombas[i];

            if (g != null && g.isAlive()) {
                Position gp = g.getPosition();

                //mario sobre goomba
                Position belowMario = mp.translate(0, +1);
                boolean pisa = mario.isFalling() && gp.equals(mp.translate(0, +1));

                if (pisa) {
                    g.receiveInteraction(mario);  //goomba muere :(
                    mario.saltitosLeft = 0;
                    mario.setFalling(false);
                    return;
                }

                //normal y pos se mata mi amigo
                boolean samePos = gp.equals(mp);
                boolean hitHead = mario.isBig() && gp.equals(mp.translate(0, -1));

                if (samePos || hitHead) {
                    mario.getGame().marioDies();
                    clean();
                    return;
                }
            }
        }

        //exit
        if (exit != null && exit.getPosition().equals(mp)) {
            mario.getGame().setPlayerWon();
        }
    }

	//exit door no es solido, pero no puede estar sobre land
	public void add(ExitDoor d) {
        if (d == null) return;
        Position p = d.getPosition();
        //hay land abajo
		for(int i=0; i<nLands; i++){
			if(lands[i].getPosition().equals(p)) return;
		}
        exit = d; //si ya habia, sustituyo
    }

	public boolean exitDoorAt(Position p) {
		return exit != null && exit.getPosition().equals(p);
	}

	//mario no es solido, pero no puede estar sobre land
	public void add(Mario m) {
        if (m == null) return;
		Position base = m.getPosition();
		//hay land
        for (int i=0; i<nLands; i++){
			if(lands[i].getPosition().equals(base))return;
		}
        mario = m; //si ya habia sustituyo
    }

	//pintado

	//prioridad draw: mario, goombs, door, land, void

	public String stringAt(Position p) {

		if (mario != null) {
			//patas de mario ALWAYS
			if (mario.getPosition().equals(p)) {
				return mario.getIcon();
			}
			
			//cabezoncio solo si es big
			if (mario.isBig() && mario.getPosition().translate(0, -1).equals(p)) {
				return mario.getIcon();
			}
		}
		
		for (int i = 0; i < nGoombas; i++){ //goombas malos
			if(goombas[i].getPosition().equals(p)){
				return goombas[i].getIcon();
			}
		}
		if (exit != null && exit.getPosition().equals(p)){ //puerta
			return exit.getIcon();
		}
		for (int i = 0; i < nLands; i++){ //bloque
			if(lands[i].getPosition().equals(p)){
				return lands[i].getIcon();
			}
		}
		return Messages.EMPTY;

	}

    public void updateAllAutonomous() {
        // Mueve los goombas
        for (int i = 0; i < nGoombas; i++) {
            if (goombas[i] != null) {
                goombas[i].update();
            }
        }
        // 2. Física de Mario (salto + gravedad)
        if (mario != null) {
            Position pos = mario.getPosition();
            Game g = mario.getGame();

            // 2.1. Si está saltando, sube hasta agotar saltitos
            if (mario.isJumping() && mario.saltitosLeft > 0) {
                Position up = pos.translate(0, -1);
                if (!isSolidAt(up)) {
                    mario._setPositionForPhysics(up);
                    mario.saltitosLeft--;
                    mario.setFalling(false);
                } else {
                    mario.saltitosLeft = 0; // tope si choca
                }
                if (mario.saltitosLeft == 0) {
                    mario.setJumping(false); // termina salto
                    mario.setFalling(true);
                }
            }
            // 2.2. Si no salta, aplica gravedad
            else {
                Position below = pos.translate(0, +1);
                if (!isSolidAt(below)) {
                    mario._setPositionForPhysics(below);
                    mario.setFalling(true);
                    // si se sale del tablero, muere
                    if (below.getRow() >= Game.DIM_Y) {
                        g.marioDies();
                    }
                }else {mario.setFalling(false);}
            }
        }

        clean();
        //doInteractions() en Game.updateTurn()
    }

	public void updateAll() {
		/*if (mario != null)
			mario.update(); //primero mario
		for (int i = 0; i < nGoombas; i++) { //luego goombas
			goombas[i].update();
		}
		doInteractionsFrom(mario);
		clean();

		 */
        if (mario != null) mario.update();   // mueve Mario (consume tiempo por movimiento)
        updateAllAutonomous();               // goombas
    }

	public void clear(){  //mato todo y reseteo contadores, para el reset
		for (int i=0; i<nLands; i++) lands[i]=null;
		for (int i=0; i<nGoombas; i++) goombas[i]=null;
		nLands = 0;
    	nGoombas = 0;
    	exit = null;
    	mario = null;
	}

	public void clean() { //elimina goomba muerto
		int j = 0;
		for (int i = 0; i < nGoombas; i++) {
			if (goombas[i].isAlive()) {
				goombas[j++] = goombas[i];
			}
		}
		nGoombas = j;
	}

}
