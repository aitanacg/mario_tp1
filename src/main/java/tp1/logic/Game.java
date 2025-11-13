package tp1.logic;

import java.util.ArrayList;
import java.util.List;
import tp1.logic.gameobjects.*;
import tp1.logic.GameModel;

public class Game implements GameModel{

	public static final int DIM_X = 30;
	public static final int DIM_Y = 15;

	private int nLevel;

	//marcador
	private int remainingTime = 100;
	private int points = 0;
	private int numLives = 3;

	//estado final
	private boolean finished = false;
	private boolean playerWon = false;
	private boolean playerLost = false;

	private GameObjectContainer gameObjects = new GameObjectContainer();
	private Mario mario;

	private final ActionList actions = new ActionList();

	public GameObjectContainer getGameObjectContainer() {
		return gameObjects;
	}
	
	public Game(int nLevel) {
		this.nLevel = nLevel;
		resetScoreAndState();
		if (nLevel == 0)
			initLevel0();
		else
			initLevel1();
	}
	
	private void resetScoreAndState() {
    	remainingTime = 100;
    	points = 0;
    	numLives = 3;
    	finished = false;
    	playerWon = false;
    	playerLost = false;
	}
	

	//en este nivel mario empieza pequenito y hay mas de un goomba 
	private void initLevel1() {
		this.nLevel = 1;
		this.remainingTime = 100;
        initCommonTerrain();

		this.mario = new Mario(this, new Position(Game.DIM_Y - 3, 0));  //personajes
		this.mario.setBig(false); //si quisiera empezar big pues le pongo true
		gameObjects.add(this.mario);

		//goombas D:
		gameObjects.add(new Goomba(this, new Position(0, 19)));
		gameObjects.add(new Goomba(this, new Position(5, 6)));
		gameObjects.add(new Goomba(this, new Position(9, 6)));
		gameObjects.add(new Goomba(this, new Position(12, 8)));
		gameObjects.add(new Goomba(this, new Position(12, 11)));
		gameObjects.add(new Goomba(this, new Position(12, 13)));
    
	}
	
	private void initLevel0() {
		this.nLevel = 0;
		this.remainingTime = 100;
        initCommonTerrain();
		// 3. Personajes
		this.mario = new Mario(this, new Position(Game.DIM_Y-3, 0));
		gameObjects.add(this.mario);
		gameObjects.add(new Goomba(this, new Position(0, 19)));
	}

    private void initCommonTerrain() {
        gameObjects = new GameObjectContainer();
        //suelo inicial
        for (int col = 0; col < 15; col++) {
            gameObjects.add(new Land(this, new Position(13, col)));
            gameObjects.add(new Land(this,new Position(14, col)));
        }

        //plataformas comunes
        gameObjects.add(new Land(this,new Position(Game.DIM_Y - 3, 9)));
        gameObjects.add(new Land(this,new Position(Game.DIM_Y - 3, 12)));

        for (int col = 17; col < Game.DIM_X; col++) {
            gameObjects.add(new Land(this,new Position(Game.DIM_Y - 2, col)));
            gameObjects.add(new Land(this,new Position(Game.DIM_Y - 1, col)));
        }

        //plataformas altas
        gameObjects.add(new Land(this,new Position(9, 2)));
        gameObjects.add(new Land(this,new Position(9, 5)));
        gameObjects.add(new Land(this,new Position(9, 6)));
        gameObjects.add(new Land(this,new Position(9, 7)));
        gameObjects.add(new Land(this,new Position(5, 6)));

        //salto final tipo escalera
        int tamX = 8;
        int posIniX = Game.DIM_X - 3 - tamX;
        int posIniY = Game.DIM_Y - 3;
        for (int col = 0; col < tamX; col++) {
            for (int fila = 0; fila < col + 1; fila++) {
                gameObjects.add(new Land(this,new Position(posIniY - fila, posIniX + col)));
            }
        }

        //exit
        gameObjects.add(new ExitDoor(this, new Position(Game.DIM_Y - 3, Game.DIM_X - 1)));
    }

	public String positionToString(int col, int row) {
    	return gameObjects.stringAt(new Position(row, col));
	}


	public boolean isFinished() { return finished; }

	public boolean playerWins() { return playerWon; }

	public boolean playerLoses() { return playerLost; }  

	public int remainingTime() { return remainingTime; }

	public int points() { return points; }

	public int numLives() { return numLives; }

	public void update() { //baja el tiempo uno si no ha acabado
        if (finished) return;

        updateTurn();

        if (!finished && remainingTime > 0) {
            remainingTime--;
            if (remainingTime == 0) {
                finished = true;
                playerLost = true;
            }
        }

        actions.clear(); //a ver si me soluciona el tremendo bug del up :')
	}

	public void reset(Integer mayLevel){
		int target = (mayLevel == null) ? this.nLevel : mayLevel;
    	if (target != 0 && target != 1) target = this.nLevel;

 		int keepPoints = this.points;
    	int keepLives  = this.numLives;

    	if (target == 0) initLevel0();
    	else initLevel1();

    	this.points   = keepPoints;
    	this.numLives = keepLives;
	}

	public void loseLife() { //quita vida y si llega a 0 pierde
		if (finished)
			return;
		if (numLives > 0)
			numLives--;
		if (numLives == 0) {
			finished = true;
			playerLost = true;
		}
	}

	public void marioDies() {
		loseLife();

		if (!finished) {
			if (nLevel == 0)//si queda vidas pos reset
				initLevel0();
			else
				initLevel1();
		}
	}

	@Override
	public String toString() {
		//TODO returns a textual representation of the object
		return "TODO: Hola soy el game";
	}
	
	public void reset() {
		int keepPoints = this.points;
		int keepLives = this.numLives;

		if (nLevel == 0) //recarga level
			initLevel0();
		else
			initLevel1();

		this.points = keepPoints;
		this.numLives = keepLives;
	}

	public void reset(int level) {
		if (level == 0 || level == 1)
			this.nLevel = level;
		reset();
	}
	
	//public void removeGoomba(tp1.logic.gameobjects.Goomba g) {
    	//gameObjects.removeGoomba(g);
    //    if (g != null) g.die();         // marca el Goomba como muerto
    //       // gameObjects.clean(); // lo elimina en la siguiente limpieza

	//}

	public void addAction(Action a) {
        actions.add(a);
	}

	public ActionList getActions() {
		return actions;
	}

	public void setPlayerWon() {
		this.finished = true;
		this.playerWon = true;
	}

	public void addPoints(int pts) {
		this.points += pts;
	}

    public void consumeTime(int t) {
        remainingTime -= t;
        if (remainingTime <= 0) {
            marioDies();
        }
    }

    //un turno hace el update
    public void updateTurn() {
        gameObjects.updateAll();    //goombas
    }

}
