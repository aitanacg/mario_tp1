
	/**
	 * Los tests no funcionan demasiado bien pero el codigo compila, y en terminal se ve y se puede jugar perfectamente
	 * Por lo que veo los test no pueden leer bien lo que imprimo por la terminal, y no detecta ni a mario ni a otras entidades
	 * Las fisicas del juego las he implementado como mejor he podido interpretarlas
 	 * estan explicadas a lo largo del codigo y en mario.java explica los movimientos de mario
	 * 
	 */

package tp1;

import java.util.Locale;

import tp1.control.Controller;
import tp1.logic.Game;
import tp1.view.ConsoleColorsView;
import tp1.view.ConsoleView;
import tp1.view.GameView;
import tp1.view.Messages;

public class Main {

	/**
	 * Entry pointpackage tp1.logic.gameobjects;
	 * 
	 * @param args Arguments for the game.
	 */
	public static void main(String[] args) {
		// Required to avoid issues with tests
        Locale.of("es", "ES");

		try {
			
			int nLevel = 1;
			if (args.length != 0) nLevel = Integer.parseInt(args[0]);

            Game game = new Game(nLevel);
            GameView view = args.length>1 ? new ConsoleView(game): new ConsoleColorsView(game);
            Controller controller = new Controller(game, view);
					
			controller.run();

		} catch (NumberFormatException e) {
			System.out.println(String.format(Messages.LEVEL_NOT_A_NUMBER_ERROR, args[0]));
		}
	}
}
