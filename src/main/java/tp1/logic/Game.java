package tp1.logic;
//gestiona tiempo, vidas, niveles

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

import tp1.exceptions.*;
import tp1.logic.gameobjects.*;
import tp1.logic.GameModel;
import tp1.view.Messages;

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

    private GameConfiguration fileLoader = null;


    public GameObjectContainer getGameObjectContainer() {
		return gameObjects;
	}
	
	public Game(int nLevel) {
		this.nLevel = nLevel;
		resetScoreAndState();
        if (nLevel == -1)
            initLevelMinus1();
        else if (nLevel == 0)
            initLevel0();
        else if (nLevel == 2)
            initLevel2();
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

    //NIVELES__________________________________________________________________________
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

    private void initLevelMinus1() {
        this.nLevel = -1;

        this.remainingTime = 100;
        this.points = 0;
        this.numLives = 3;

        //tablero vacio
        gameObjects = new GameObjectContainer();

        //anado mario chiquito
        this.mario = new Mario(this, new Position(0, 0));
        gameObjects.add(this.mario);
    }

    private void initLevel2() {
        initLevel1();
        gameObjects.add(new Box(this, new Position(9,4), false)); //box (9,4)
        gameObjects.add(new Mushroom(this, new Position(12,8))); //mushroom(12,8)
        gameObjects.add(new Mushroom(this, new Position(2,20))); //mushroom (2,20)
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
    }//FIN NIVELES__________________________________________________________________________

	public String positionToString(int col, int row) {
    	return gameObjects.stringAt(new Position(row, col));
	}

	public boolean isFinished() { return finished; }
	public boolean playerWins() { return playerWon; }
	public boolean playerLoses() { return playerLost; }
	public int remainingTime() { return remainingTime; }
	public int points() { return points; }
	public int numLives() { return numLives; }

	public void update() { //baja el tiempo uno si no ha acabado, y marca el final
        if (finished) return;

        try {
            updateTurn();//objetos y todo lo que importa, llama a updateAll() de GamObjCon, pq es verde esto
        }
        catch (GameModelException e) {
            throw new RuntimeException("Unexpected game model error", e); //error del modelo
        }

        if (!finished && remainingTime > 0) {
            remainingTime--;
            if (remainingTime == 0) {
                finished = true;
                playerLost = true;
            }
        }
        actions.clear();//me soluciona lo de volar
	}

	public void reset(Integer Level){
		int newLevel = (Level == null) ? this.nLevel : Level; //(cond)? true:false, si es null me quedo en el que ya esta
        if (newLevel != -1 && newLevel != 0 && newLevel != 1 && newLevel != 2)
            newLevel = this.nLevel;

 		int keepPoints = this.points;
    	int keepLives  = this.numLives;

        if (newLevel == -1)
            initLevelMinus1();
        else if (newLevel == 0)
            initLevel0();
        else if (newLevel == 1)
            initLevel1();
        else if (newLevel == 2)
            initLevel2();

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
            if (nLevel == -1)
                initLevelMinus1();
            else if (nLevel == 0)
                initLevel0();
            else if (nLevel == 2)
                initLevel2();
            else
                initLevel1();
		}
	}

	@Override
	public String toString() {
		return "TODO: Hola soy el game";
	}
	
	public void reset() {
        if (fileLoader != null) { //si cargo un fichero lo usamosss
            try {
                this.remainingTime = fileLoader.getRemainingTime();  //estado del game
                this.points = fileLoader.getPoints();
                this.numLives = fileLoader.getLives();
                this.finished = false;
                this.playerWon = false;
                this.playerLost = false;

                this.gameObjects.clear(); //quito lo que haya

                this.mario = fileLoader.getMario(); //mario coquette, es un Mario nuevo, no un generico
                this.gameObjects.add(mario);

                for (GameObject obj : fileLoader.getNPCObjects()) { //cada uno de los nps
                    this.gameObjects.add(obj);
                }

            } catch (Exception e) {  //si falla algo me quedo donde antes
                fileLoader = null;
                resetScoreAndState();
                initLevel1();
            }

            return;
        }
        //reset viejo <3
        int keepPoints = this.points;
		int keepLives = this.numLives;

		 //recarga level
        if (nLevel == -1)
            initLevelMinus1();
        else if (nLevel == 0)
            initLevel0();
        else if (nLevel == 2)
            initLevel2();
        else
            initLevel1();

		this.points = keepPoints;
		this.numLives = keepLives;
	}

	public void reset(int level) {
        if (level == -1 || level == 0 || level == 1|| level == 2)
			this.nLevel = level;
		reset();
	}

    @Override
    public void addAction(Action a) throws ActionParseException {
        if (a == null)
            throw new ActionParseException("Null action");
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

    public void updateTurn() throws GameModelException{//un turno hace el update
            gameObjects.updateAll(); //goombas
    }

    public void addObject(String[] objWords) throws OffBoardException, ObjectParseException {

        GameObject obj = GameObjectFactory.parse(objWords, this);

        if (obj == null) {
            throw new ObjectParseException(Messages.UNKNOWN_GAME_OBJECT.formatted(String.join(" ", objWords)));
        }

        Position p = obj.getPosition();
        if (!p.isInBounds(DIM_X, DIM_Y)) {  //valido pos
            throw new OffBoardException(Messages.OBJECT_OFF_BOARD.formatted(String.join(" ", objWords)));
        }

        gameObjects.add(obj);
    }

    public void load(String fileName) throws GameLoadException { //creo un fgc, copio el estado y las copy() y con fileloader hago reset

        FileGameConfiguration fgc = new FileGameConfiguration(fileName, this); //creo config desde fichero
        this.fileLoader = fgc; //guardo ref para reset

        this.remainingTime = fgc.getRemainingTime();  //actualizo game
        this.points = fgc.getPoints();
        this.numLives = fgc.getLives();

        this.gameObjects.clear();  //limpiooo

        this.mario = fgc.getMario();  //viene mi mario amigo
        this.gameObjects.add(mario);
        for (GameObject obj : fgc.getNPCObjects()) {  //npcs
            this.gameObjects.add(obj);
        }

        this.finished = false;
        this.playerWon = false;
        this.playerLost = false;
    }

    public void save(String fileName) throws GameModelException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {

            // 1) Escribir estado del juego
            pw.println(remainingTime + " " + points + " " + numLives);

            // 2) Escribir objetos
            for (GameObject obj : gameObjects.getObjects()) {
                pw.println(obj.toString());
            }

        } catch (IOException e) {
            throw new GameModelException(
                    Messages.ERROR_SAVING.formatted(fileName), e
            );
        }
    }


}
