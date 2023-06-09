package pongnn;

import pongUi.GameUI;
import pongUi.UI;

public class PlayerAlgo extends Player {
	
	int border = 12;
	int counter1 = 0;
	int counter2 = border;
	int counter3 = border;

	public PlayerAlgo(boolean setSide) {
		super(setSide);
	}

	@Override
	public boolean isHuman() {
		return false;
	}
	
	
	@Override
	public int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI) {
		
		int heightPaddle = gameUI.getLeftPaddle().getPaddleHeight();
		double yBall = gameUI.getYPosBall();
		double yPosPaddle = 0.0;
		if(leftPlayer) yPosPaddle = gameUI.getYPosLeftPlayer();
		else yPosPaddle = gameUI.getYPosRightPlayer();
		
        int move = UI.MOVE_STAY;
        if(yPosPaddle + (heightPaddle/2) <= yBall) {
        	move = UI.MOVE_DOWN;
        }

        if(yPosPaddle + (heightPaddle/2) >= yBall) {
        	move = UI.MOVE_UP;
        }

        if(yPosPaddle == yBall) {
        	move = UI.MOVE_STAY;
        }


        /*
        if(counter1 <= border) {
        	counter1++;
        	move = UI.MOVE_STAY;

        	if(counter1 == border) {
        		counter2 = 0;
        	}
        } else
        if(counter2 <= border) {
            counter2++;
            move = UI.MOVE_UP;
            
            
        	if(counter2 == border) {
        		counter3 = 0;
        	}
        } else
        if(counter3 <= border) {
            counter3++;
            move = UI.MOVE_DOWN;
        	
        	if(counter3 == border) {
        		counter1 = 0;
        	}
        }
        */
        
        // System.out.println("move " + this.leftPlayer + " : " + move);
        
        return move;	
    }
	

	@Override
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
		// do nothing
	}

	@Override
	public void save() {
		// do nothing
	}

	@Override
	public void load(String name) {
		// do nothing
	}

}
