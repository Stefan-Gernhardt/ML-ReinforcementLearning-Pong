package pongUi;

import java.util.concurrent.TimeUnit;

import pongnn.AIPlayer;
import pongnn.Player;

public class UI {
	public static final int f = 4;
	public static final int maxNumberGameTicks = (int)((GameUI.WIDTH/ Ball.START_SPEED)*60)/100*500;  
	//! public static final int maxNumberGameTicks = (int)((GameUI.getw()/ Ball.START_SPEED)*60)/100*500;  
	
	public static final int MOVE_UP   = 0;
	public static final int MOVE_DOWN = 1;
	public static final int MOVE_STAY = 2;
	
	private GameUI gameUI = null;
	private boolean withUI = true;
	
	
	public static final double[] startPositions = { 0.0, 1.0 };
	public static final double[] startVels =      { 0.0, 0.0 };

	public UI(boolean withUI) {
		this.withUI = withUI;
		gameUI = new GameUI(withUI);
	}
	
	
	public int playOnePoint(Player leftPlayer, Player rightPlayer, boolean withUI) {
		this.withUI = withUI;
		
		// prepare/start game
		gameUI.start();
		
		// int i = (int)(Math.random() * startVels.length);
		// gameUI.getBall().setY(startPositions[i]);
		// gameUI.getBall().setVelY(startVels[i]);

		// System.out.println("game state: " + gameUI.getGameState());
		
		// game loop for one point
		if(withUI) gameUI.draw();
		leftPlayer.resetForPlayOnePoint();
		rightPlayer.resetForPlayOnePoint();
		
		int gameTicks = 0;
		do {
			// left player
			if(!leftPlayer.isHuman()) {
				int moveLeftPlayer = leftPlayer.calculateMoveAndStoreMove(gameUI, withUI);
				if(moveLeftPlayer == MOVE_UP) {
					gameUI.getLeftPaddle().up();
				}
				else if(moveLeftPlayer == MOVE_DOWN) {
					gameUI.getLeftPaddle().down();
				}
				else {
					gameUI.getLeftPaddle().stay();
				}
			}
			
			// right player
			if(!rightPlayer.isHuman()) {
				int moveRightPlayer = rightPlayer.calculateMoveAndStoreMove(gameUI, withUI);
				// System.out.println("move: " + move);
	
				if(moveRightPlayer == MOVE_UP) {
					gameUI.getRightPaddle().up();
				}
				else if(moveRightPlayer == MOVE_DOWN) {
					gameUI.getRightPaddle().down();
				}
				else {
					gameUI.getRightPaddle().stay();
				}
			}
			
			gameTicks++;
			gameUI.update();
			if(withUI) gameUI.draw();
			
			if(withUI) wait_(100);
		} while((gameUI.getGameState() != GameUI.Stopped_We_have_a_winner) && (gameTicks<maxNumberGameTicks));
		
		if(withUI) {
			System.out.println();
			wait_(1000);
		}

		if(gameTicks >= maxNumberGameTicks) {
			// System.out.println("no winner, because of to many ball exchanges");
			gameUI.getLeftPaddle().addPoint();
			// aiLeftPlayerPong.trainAfterPlayingOnePoint(true, withUI);			
			return 0;
		}
		

		
		// display winner of the last point
		if(gameUI.getWinner().isLeft()) {
			// System.out.println("winner of the last point is left");
			leftPlayer.trainAfterPlayingOnePoint(true, withUI);
			rightPlayer.trainAfterPlayingOnePoint(false, withUI);
			return 1;
		}
		else {
			// System.out.println("winner of the last point is right");
			leftPlayer.trainAfterPlayingOnePoint(false, withUI);
			rightPlayer.trainAfterPlayingOnePoint(true, withUI);
			return 2;
		}
		
	}
	

	public void wait_(long ms) {
		try {
			TimeUnit.MILLISECONDS.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public void setStartPoint(double d) {
		gameUI.setStartPoint(d);
	}


	public void setStartAngle(double startAngle) {
		gameUI.setAnglePoint(startAngle);
	}


	public void resetScore() {
		gameUI.resetScore();
	}
}
