package tp1.control.commands;

import tp1.exceptions.CommandException;
import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;

public interface Command {

    Command parse(String[] commandWords) throws CommandParseException;

    void execute(GameModel game, GameView view) throws CommandExecuteException;

    String helpText();
}

//public interface Command { Command parse(String[] commandWords); void execute(GameModel game, GameView view); String helpText(); }
