package tp1.control.commands;

import java.util.Arrays;

import tp1.logic.Game;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameObjectFactory;

public class AddObjectCommand extends AbstractCommand {

    private static final String NAME = "addobject";
    private static final String SHORTCUT = "ao";
    private static final String DETAILS = "[a]dd[O]bject <object_description>";
    private static final String HELP = "adds to the board the object given by object_description";

    private String originalLine;
    private String[] objectWords;

    //constructor para el prototype --- CommandGenerator
    public AddObjectCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    //constructor por si pongo parametros
    private AddObjectCommand(String originalLine, String[] objWords) {
        this();
        this.originalLine = originalLine;
        this.objectWords = objWords;
    }

    //PARSE........................
    @Override
    public Command parse(String[] words) {

        if (!matchCommand(words[0]))//coincide?
            return null;

        if (words.length < 2) { //parametros
            return null;
        }

        String original = String.join(" ",
                Arrays.copyOfRange(words, 1, words.length)); //reconstruyo

        String[] objWords = Arrays.copyOfRange(words, 1, words.length);

        return new AddObjectCommand(original, objWords);
    }


    // EXECUTE
    @Override
    public void execute(GameModel gameModel, GameView view) {

        Game game = (Game) gameModel;

        GameObject obj = GameObjectFactory.parse(objectWords, game);//parseo con factoria

        if (obj == null) {
            view.showError("Invalid game object: " + originalLine);
            return;
        }

        int r = obj.getPosition().getRow();  //valido
        int c = obj.getPosition().getCol();

        if (r < 0 || r >= Game.DIM_Y || c < 0 || c >= Game.DIM_X) {
            view.showError("Invalid game object: " + originalLine);
            return;
        }

        game.getGameObjectContainer().add(obj);//anado al contenedor

        view.showGame();
    }
}