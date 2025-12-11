package tp1.logic;
//sistema de carga de filess, leo ficheros y reconstruyo mapa, deserializacion
//creo objetos reales a partir del texto, detecto errores de formato en las lineas

import tp1.exceptions.*;
import tp1.logic.gameobjects.*;
import tp1.view.Messages;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Sistema de load de files: leo fichero, reconstruyo mapa, deserializacion
 * No introduce objetos al game, solo deserializa y guarda los prototipos
 * Game usa copy() para obtener los objetos nuevos en cada reset
 *
 * <remainingTime> <points> <lives>
 * (row,col) Mario <dir> <size>
 * (row,col) Goomba <dir>
 * (row,col) Box <Empty>
 *     ...
 */
public class FileGameConfiguration implements GameConfiguration {

    private int remainingTime;
    private int points;
    private int lives;

    private GameWorld game; //Referenia al GW real, cada objeto necesita uno

    private Mario mario; //prototipo de mario desde fichero
    private List<GameObject> npcObjects = new ArrayList<>(); //lista de npcs

    private final String fileName;
    public String getFileName() { return fileName; }

    /// Leo y parseo el fichero
    public FileGameConfiguration(String fileName, GameWorld game) throws GameLoadException {
        this.game = game;
        this.fileName = fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) { //abro fichero, ya se cierra solo

            ////====STATUS====
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

            ////====OBJETOS====
            String line;
            while ((line = br.readLine()) != null) { //leo cada line y creo obj
                if (line.isBlank()) continue;//ignoro
                String[] words = line.trim().split("\\s+"); //trim me quita basura y spliteo los espacios

                Mario esMario = new Mario().parse(words,game); //intento parsear a mario

                if (esMario != null) {
                    mario = esMario;
                    continue; //no sigo con factoria
                }

                GameObject obj; //si no era mario sigo con factoria de los otros obj
                try {
                    obj = GameObjectFactory.parse(words,game);
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

    ////=====================GETTERS DEV COPIAS========================
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
        return mario.copy(game); //con copy devuelvo marios nuevos en cada reset, same con npcs
    }

    @Override
    public List<GameObject> getNPCObjects() {
        List<GameObject> copias = new ArrayList<>();
        for (GameObject obj : npcObjects) {
            copias.add(obj.copy(game));
        }
        return copias;
    }
}