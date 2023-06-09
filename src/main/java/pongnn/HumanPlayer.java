package pongnn;

import pongUi.GameUI;

public class HumanPlayer extends Player {

	public HumanPlayer(boolean setSide) {
		super(setSide);
	}

	@Override
	public int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI) {
		return 0;
	}

	@Override
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
	}

	@Override
	public void save() {
	}

	@Override
	public void load(String name) {
	}

	@Override
	public boolean isHuman() {
		return true;
	}

}
