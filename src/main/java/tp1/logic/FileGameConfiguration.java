package tp1.logic;
//sistema de carga de filess, leo ficheros y reconstruyo mapa, deserializacion
//creo objetos reales a partir del texto, detecto errores de formato en las lineas

import tp1.exceptions.*;
import tp1.logic.gameobjects.*;
import tp1.view.Messages;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileGameConfiguration implements GameConfiguration {

    private int remainingTime;
    private int points;
    private int lives;

    private GameWorld game; //ref al game donde ppongo los obj, cada copy necesita su game

    //guardo estado original de estos, como en el fichero, uso copias
    private Mario mario;
    private List<GameObject> npcObjects = new ArrayList<>(); //ahora seran npcs pq me lio

    public FileGameConfiguration(String fileName, GameWorld game) throws GameLoadException {
        this.game = game;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) { //abro fichero, ya se cierra solo

            String status = br.readLine(); //lee linea de statuss
            if (status == null)
                throw new GameLoadException(Messages.INVALID_FILE_CONFIGURATION.formatted(fileName));

            String[] parts = status.trim().split("\\s+");  //formato: time points lives
            if (parts.length != 3)
                throw new GameLoadException(Messages.INCORRECT_GAME_STATUS.formatted(status));

            try {
                remainingTime = Integer.parseInt(parts[0]);
                points = Integer.parseInt(parts[1]);
                lives = Integer.parseInt(parts[2]);
            }
            catch (NumberFormatException nfe) {
                throw new GameLoadException(Messages.INCORRECT_GAME_STATUS.formatted(status), nfe);
            }

            String line;
            while ((line = br.readLine()) != null) { //leo cada line y creo obj
                if (line.isBlank()) continue;//ignoro
                String[] words = line.trim().split("\\s+"); //trim me quita basura y spliteo los espacios

                Mario esMario = new Mario().parse(words, (Game) game); //intento parsear a mario, covarianzaa, intento de lo del foro, asi no tengo que castear
                if (esMario != null) {
                    mario = esMario;
                    continue; //no sigo con factoria
                }

                GameObject obj; //si no era mario sigo con factoria de los otros obj
                try {
                    obj = GameObjectFactory.parse(words, (Game) game);
                }
                catch (ObjectParseException | OffBoardException e) { //catcheo tos tipos, las guardo en e obv
                    throw new GameLoadException(
                            Messages.INVALID_FILE_CONFIGURATION.formatted(fileName), e
                    );
                }
                npcObjects.add(obj);
            }

            if (mario == null) //valido si mario esta de chill
                throw new GameLoadException(Messages.INVALID_FILE_CONFIGURATION.formatted(fileName));
        }
        catch (FileNotFoundException fileNF) {
            throw new GameLoadException(Messages.FILE_NOT_FOUND.formatted(fileName), fileNF);
        }
        catch (IOException ioe) {
            throw new GameLoadException(Messages.FILE_READING_ERROR.formatted(fileName), ioe);
        }
        catch (Exception e) {
            throw new GameLoadException(Messages.INVALID_FILE_CONFIGURATION.formatted(fileName), e);
        }
    }

    @Override
    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public Mario getMario() {
        return (Mario) mario.copy((Game) game); //con copy devuelvo marios nuevos en cada reset, same con npcs
        //return m
    }

    @Override
    public List<GameObject> getNPCObjects() {
        List<GameObject> copias = new ArrayList<>();
        for (GameObject obj : npcObjects) {
            copias.add(obj.copy((Game) game));
        }
        return copias;
    }
}