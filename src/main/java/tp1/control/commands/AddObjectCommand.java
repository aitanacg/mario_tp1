package tp1.control.commands;

import java.util.Arrays;

import tp1.exceptions.CommandParseException;
import tp1.logic.Game;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameObjectFactory;
import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.GameModelException;
import tp1.view.Messages;

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
    public Command parse(String[] words) throws CommandParseException {

        if (!matchCommand(words[0]))//coincide?
            return null;

        if (words.length < 2) { //parametros
            return null;
        }

        String original = String.join(" ",
                Arrays.copyOfRange(words, 1, words.length)); //reconstruyo

        String[] objWords = Arrays.copyOfRange(words, 1, words.length); //desc del obj

        return new AddObjectCommand(original, objWords);
    }


    // EXECUTE
    @Override
    public void execute(GameModel gameModel, GameView view) throws CommandExecuteException {
        Game game = (Game) gameModel;

        try {
            game.addObject(objectWords);
            view.showGame();
        }
        catch (Exception e) {
            throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);
        }


//        Game game = (Game) gameModel;
//
//        GameObject obj;
//        try {
//            obj = GameObjectFactory.parse(objectWords, game);
//        }
//        catch (Exception e) {
//            throw new CommandExecuteException(
//                    Messages.INVALID_GAME_OBJECT.formatted(originalLine), e
//            );
//        }
//
//        if (obj == null) {
//            throw new CommandExecuteException(Messages.INVALID_GAME_OBJECT.formatted(originalLine));            // antes view.showError y return
//        }
//
//        if (!obj.getPosition().isInBounds(Game.DIM_X, Game.DIM_Y)) {
//            throw new CommandExecuteException(Messages.OBJECT_OFF_BOARD.formatted(originalLine));
//        }
//
//        game.getGameObjectContainer().add(obj);//anado al contenedor
//
//        view.showGame();
    }

}