package tp1.control.commands;

import tp1.logic.Game;
import tp1.logic.GameModel;
import tp1.view.GameView;

public interface Command {
    Command parse(String[] commandWords);
    void execute(GameModel game, GameView view);
    String helpText();
}
