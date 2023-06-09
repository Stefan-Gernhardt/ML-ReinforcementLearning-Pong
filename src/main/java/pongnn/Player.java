package pongnn;

import java.util.ArrayDeque;
import java.util.ArrayList;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import pongUi.Ball;
import pongUi.GameUI;
import pongUi.UI;


public abstract class Player {
	public final static int DEQUE_SIZE = 5;
	ArrayDeque<BallExchange> ballExchangeDeque = new ArrayDeque<BallExchange>(DEQUE_SIZE);
	
	protected ArrayList<INDArray> inputList  = null;
	protected ArrayList<INDArray> outputList = null;
	protected ArrayList<Integer>  moveList   = null;
	
	protected boolean leftPlayer = true;
	
	public abstract int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI);
	public abstract void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI);
	public abstract void save();
	public abstract void load(String name);
	public abstract boolean isHuman();
	
	String side = "";
	
	
	Player (boolean setSide) {
		leftPlayer = setSide;

		if(setSide) side = "Left";
		else side = "Right";
	}
	
	
	public INDArray convertInputToMatrix(double yPosLeftPlayer, double yPosRightPlayer, double xPosBall, double yPosBall,
			double leftSpeedBall, double rightSpeedBall, double upSpeedBall, double downSpeedBall) {

		INDArray matrix = Nd4j.zeros(1, PongNN.NUMBER_OF_INPUT_NEURONS);
		
		if(yPosLeftPlayer  < 0.0) yPosLeftPlayer = 0.0;
		if(yPosRightPlayer < 0.0) yPosRightPlayer = 0.0;
		if(xPosBall        < 0.0) xPosBall = 0.0;
		if(yPosBall        < 0.0) yPosBall = 0.0;
		if(leftSpeedBall   < 0.0) leftSpeedBall = 0.0;
		if(rightSpeedBall  < 0.0) rightSpeedBall = 0.0;
		if(upSpeedBall     < 0.0) upSpeedBall = 0.0;
		if(downSpeedBall   < 0.0) downSpeedBall = 0.0;

		if(yPosLeftPlayer  > 1.0) yPosLeftPlayer = 1.0;
		if(yPosRightPlayer > 1.0) yPosRightPlayer = 1.0;
		if(xPosBall        > 1.0) xPosBall = 1.0;
		if(yPosBall        > 1.0) yPosBall = 1.0;
		if(leftSpeedBall   > 1.0) leftSpeedBall = 1.0;
		if(rightSpeedBall  > 1.0) rightSpeedBall = 1.0;
		if(upSpeedBall     > 1.0) upSpeedBall = 1.0;
		if(downSpeedBall   > 1.0) downSpeedBall = 1.0;
		
		
		matrix.putScalar(new int[] {0, 0},  yPosLeftPlayer);
		matrix.putScalar(new int[] {0, 1},  yPosRightPlayer);
		matrix.putScalar(new int[] {0, 2},  xPosBall);
		matrix.putScalar(new int[] {0, 3},  yPosBall);
		matrix.putScalar(new int[] {0, 4},  leftSpeedBall);
		matrix.putScalar(new int[] {0, 5},  rightSpeedBall);
		matrix.putScalar(new int[] {0, 6},  upSpeedBall);
		matrix.putScalar(new int[] {0, 7},  downSpeedBall);
		
    	// System.out.println(matrix.toString());
		
		return matrix;
	}
	
	
	public INDArray playgroundValuesToInputMatrix(GameUI gameUI) {
		double yPosLeftPlayer = (1.0 * gameUI.getYPosLeftPlayer()) / (1.0 * gameUI.getYPosMax());
		// System.out.println("y-pos left player: " + yPosLeftPlayer);

		double yPosRightPlayer = (1.0 * gameUI.getYPosRightPlayer()) / (1.0 * gameUI.getYPosMax());
		// System.out.println("y-pos right player: " + yPosRightPlayer);

		double xPosBall = (1.0 * gameUI.getXPosBall()) / (1.0 * gameUI.getXPosMaxBall());
		// System.out.println("x-pos ball: " + xPosBall);

		double yPosBall = (1.0 * gameUI.getYPosBall()) / (1.0 * gameUI.getYPosMaxBall());
		// System.out.println("y-pos ball: " + yPosBall);
		
		double leftSpeedBall = 0;
		double rightSpeedBall = 0;
		double upSpeedBall = 0;
		double downSpeedBall = 0;

		// System.out.println("gameUI.getXSpeedBall(): " + gameUI.getXSpeedBall());  
		
		
		if(gameUI.getXSpeedBall() ==  0) {
			leftSpeedBall  = 0; 
			rightSpeedBall = 0; 
		} else if(gameUI.getXSpeedBall() < 0) {
			leftSpeedBall = -1*gameUI.getXSpeedBall()/Ball.MAX_SPEED; 
			rightSpeedBall = 0; 
		}
		else {
			leftSpeedBall = 0; 
			rightSpeedBall = gameUI.getXSpeedBall()/Ball.MAX_SPEED; 
		}
		
		if(gameUI.getYSpeedBall() ==  0) {
			upSpeedBall = 0; 
			downSpeedBall = 0; 
		}
		else if(gameUI.getYSpeedBall() < 0) {
			upSpeedBall = -1*gameUI.getYSpeedBall()/Ball.MAX_SPEED; 
			downSpeedBall = 0; 
		}
		else {
			upSpeedBall = 0; 
			downSpeedBall = gameUI.getYSpeedBall()/Ball.MAX_SPEED; 
		}

		// System.out.println("leftSpeedBall: " + leftSpeedBall);  
		// System.out.println("rightSpeedBall: " + rightSpeedBall);  
		
		
		// System.out.println("leftSpeedBall: " + leftSpeedBall + "   rightSpeedBall: " + rightSpeedBall + "  upSpeedBall: " + upSpeedBall + "  downSpeedBall: " + downSpeedBall + "  gameUI.getYSpeedBall(): " + gameUI.getYSpeedBall());
		
		
		
		INDArray input = convertInputToMatrix(yPosLeftPlayer, yPosRightPlayer,
				xPosBall, yPosBall,
				leftSpeedBall, rightSpeedBall, upSpeedBall, downSpeedBall);

		return input;
	}
	
	
	public void resetForPlayOnePoint() {
		inputList  = new ArrayList<INDArray>();
		outputList = new ArrayList<INDArray>();
		moveList   = new ArrayList<Integer>();
	}
	
	
	public void storeDecision(INDArray input, INDArray output, int move) {
		inputList.add(input);
		outputList.add(output);
		moveList.add(move);
	}
	
	
	public int getWinnerOutputNeuron(INDArray m) {
    	int neuron = 0;
    	double winnerNeuron = m.getDouble(0, 0);
    	
    	for(int i=0; i<PongNN.NUMBER_OF_OUTPUT_NEURONS; i++) {
    		double d = m.getDouble(0, i);
    		// System.out.print("" + d + " ");
    		
    		if(d >= winnerNeuron) {
    			neuron = i;
    			winnerNeuron = d;
    		}
    	}
    	// System.out.println();
    	
		return neuron;
	}	


	public int getWinnerOutputNeuronViaDiceRoll(INDArray m) {
    	double sum = m.getDouble(0, 0) + m.getDouble(0, 1) + m.getDouble(0, 2);
    	
    	double r = Math.random() * sum;
    	
    	if(r < m.getDouble(0, 0)) return UI.MOVE_UP; 
    	
    	if(r < m.getDouble(0, 0) + m.getDouble(0, 1)) return UI.MOVE_DOWN;
    	
    	return UI.MOVE_STAY;
	}	


	public void addBallExchangeToListOfSuccessfulBallExchanges() {
		
		BallExchange ballExchange = new BallExchange(inputList, outputList, moveList);
		ballExchangeDeque.addFirst(ballExchange);		
		
		// ballExchangeDeque.addLast(ballExchange);		
		// ballExchangeDeque.removeFirst();

		if(ballExchangeDeque.size() >= DEQUE_SIZE) {
			ballExchangeDeque.removeLast();
		}
		
	}
}
