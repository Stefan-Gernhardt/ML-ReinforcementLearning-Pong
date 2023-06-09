package pongnn;

import pongUi.GameUI;
import pongUi.Paddle;
import pongUi.UI;

public class AIPlayerLoose extends Player {

	public AIPlayerLoose(boolean setSide) {
		super(setSide);
	}
	
	@Override
	public boolean isHuman() {
		return false;
	}
	


	
	@Override
	public int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI) {
		double ballYPosRel = (1.0*gameUI.getYPosBall())/(1.0*gameUI.getYPosMax());
		
		
		if(ballYPosRel < 0.40) return UI.MOVE_DOWN;

		if(ballYPosRel > 0.60) return UI.MOVE_UP;
		
		return UI.MOVE_DOWN;
	}

	
	@Override
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
	}


	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void load(String name) {
		// TODO Auto-generated method stub
		
	}

}
