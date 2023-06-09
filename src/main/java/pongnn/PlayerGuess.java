package pongnn;

import pongUi.GameUI;
import pongUi.UI;

public class PlayerGuess extends Player {

	public PlayerGuess(boolean setSide) {
		super(setSide);
	}
	
	@Override
	public boolean isHuman() {
		return false;
	}
	

	@Override
	public int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI) {
    	double r = Math.random();
    	
    	if(r < 0.33) return UI.MOVE_UP; 
    	
    	if(r < 0.66) return UI.MOVE_DOWN;
    	
    	return UI.MOVE_STAY;	
    }

	@Override
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
	}

	@Override
	public void save() {
	}

	@Override
	public void load(String name) {
		// TODO Auto-generated method stub
		
	}

}
