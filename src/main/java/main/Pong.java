package main;

import pongUi.UI;
import pongUi.WindowUI;
import pongnn.AIPlayer;
import pongnn.Player;

public class Pong {

	public int trainAIReturnToService(UI ui, Player aiLeftPlayerPong, Player aiRightPlayerPong, boolean withUI) {
		return ui.playOnePoint(aiLeftPlayerPong, aiRightPlayerPong, withUI);
	}

}
