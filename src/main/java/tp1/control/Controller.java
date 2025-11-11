package tp1.control;

import tp1.logic.Game;
import tp1.logic.Action;
import tp1.view.GameView;

/**
 *  Accepts user input and coordinates the game execution logic
 */
public class Controller {

	private Game game;
	private GameView view;

	public Controller(Game game, GameView view) {
		this.game = game;
		this.view = view;
	}

	
	public void run() {
		view.showWelcome();
		boolean repaint = true;
		boolean running = true;
		String finalMsg = null;

		while (running && !game.isFinished()) {
			if (repaint)
				view.showGame();
			repaint = false;

			String[] words = view.getPrompt();
			String cmd = (words.length == 0) ? "" : words[0].trim().toLowerCase();

			boolean doUpdate = false;
			boolean validCommand = false;

			if ("action".equals(cmd) || "a".equals(cmd)) {
				for (int i = 1; i < words.length; i++) {
					try {
						Action act = Action.parse(words[i]);
						game.addAction(act);
					} catch (IllegalArgumentException e) {
						view.showMessage("Error: Unknown action " + words[i]);
					}
				}
				doUpdate = true;
				repaint = true;
				validCommand = true;
			}

			else if ("exit".equals(cmd) || "e".equals(cmd)) {
				finalMsg = "Player leaves game.";
				running = false;
				validCommand = true;
			}

			else if ("help".equals(cmd) || "h".equals(cmd)) {
				view.showMessage(tp1.view.Messages.HELP);
				validCommand = true;
			}

			else if ("reset".equals(cmd) || "r".equals(cmd)) {
				if (words.length >= 2) {
					try {
						int lvl = Integer.parseInt(words[1]);
						game.reset(lvl);
					} catch (NumberFormatException e) {
						game.reset();
					}
				} else {
					game.reset();
				}
				repaint = true;
				validCommand = true;
			}

			else if ("".equals(cmd) || "update".equals(cmd) || "u".equals(cmd)) {
				doUpdate = true;
				validCommand = true;
			}

			if (doUpdate) {
				game.update();
				repaint = true;
			}

			if (!validCommand) {
				view.showMessage("Error: Unknown command: " + String.join(" ", words));
			}
		}

		if (finalMsg != null)
			view.showMessage(finalMsg);
		else
			view.showEndMessage();
	}

}
