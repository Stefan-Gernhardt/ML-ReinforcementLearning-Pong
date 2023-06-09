package pongnn;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pongUi.GameUI;
import pongUi.UI;

import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;


public class AIPlayerLearnWithAlgo extends Player {
	private PongNN pongNN = null;
	private boolean trainModePlayMode = true;
	
	public AIPlayerLearnWithAlgo(boolean setSide) {
		super(setSide);
		pongNN = new PongNN();
	}
	
	@Override
	public boolean isHuman() {
		return false;
	}
	

	
	public void setPlayMode() {
		trainModePlayMode = false;
	}
	
	
	public void setTrainMode() {
		trainModePlayMode = true;
	}
	
	
	@Override
	public int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI) {
		if(trainModePlayMode) return calculateMoveAndStoreMoveTrainMode(gameUI, withUI);
		else                  return calculateMoveAndStoreMovePlayMode(gameUI, withUI);
    }

	
	public int calculateMoveAndStoreMovePlayMode(GameUI gameUI, boolean withUI) {
		INDArray input  = playgroundValuesToInputMatrix(gameUI);
        List<INDArray> result = pongNN.getNN().feedForward(input, false);
        INDArray output = result.get(PongNN.COUNT_LAYERS);
        
        
        int move = getWinnerOutputNeuron(output);
        
        if(withUI) {
        	// System.out.println("output vector: " + output.toString());
        	System.out.print("move(c): " + move);
        	System.out.print("  output vector: ");
        	for(int i=0; i<output.size(1); i++) {
        		System.out.print(output.getDouble(0, i) + ", ");
        	}
        	System.out.print("  input vector: ");
        	for(int i=0; i<input.size(1); i++) {
        		System.out.print(input.getDouble(0, i) + ", ");
        	}
        	System.out.println();        	
        }
        
        
        storeDecision(input, output, move);
        
        return move;
    }


	public int calculateMoveAndStoreMoveTrainMode(GameUI gameUI, boolean withUI) {
		INDArray input  = playgroundValuesToInputMatrix(gameUI);
        List<INDArray> result = pongNN.getNN().feedForward(input, false);
        INDArray output = result.get(PongNN.COUNT_LAYERS);
        
        
        int move = 0;
        if(gameUI.getYPosLeftPlayer() < gameUI.getYPosBall()) {
        	move = UI.MOVE_DOWN;
        }

        if(gameUI.getYPosLeftPlayer() > gameUI.getYPosBall()) {
        	move = UI.MOVE_UP;
        }

        if(gameUI.getYPosLeftPlayer() == gameUI.getYPosBall()) {
        	move = UI.MOVE_STAY;
        }

        if(withUI) {
        	// System.out.println("output vector: " + output.toString());
        	System.out.print("move(d): " + move);
        	System.out.print("  output vector: ");
        	DecimalFormat df = new DecimalFormat("#.00"); 
        	for(int i=0; i<output.size(1); i++) {
        		System.out.print(df.format(output.getDouble(0, i)) + ", ");
        	}
        	System.out.print("  input vector: ");
        	for(int i=0; i<input.size(1); i++) {
        		System.out.print(df.format(input.getDouble(0, i)) + ", ");
        	}
        	System.out.println();        	
        }
        
        
        storeDecision(input, output, move);
        
        return move;
	}


	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
		int numberOfDecisions = this.inputList.size();
		// System.out.println("numberOfDecisions: " + numberOfDecisions);
		
        double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];

        
		if(wonThePoint) {
	        for(int i=0; i<numberOfDecisions; i++) {
	        	
	        	INDArray inputINDArray = inputList.get(i);
	        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
	                inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
	        	}
	        	
	            int move = moveList.get(i);

    			outputArrayDouble[i][0] = 0.0;
    			outputArrayDouble[i][1] = 0.0;
    			outputArrayDouble[i][2] = 0.0;
    			
    			outputArrayDouble[i][move] = 1;

	            for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
	            	inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
	            }
	        }
	        
	        pongNN.getNN().fit(Nd4j.create(inputArrayDouble), Nd4j.create(outputArrayDouble));
		}
	}
	

	@Override
	public void resetForPlayOnePoint() {
		inputList  = new ArrayList<INDArray>();
		outputList = new ArrayList<INDArray>();
		moveList   = new ArrayList<Integer>();
	}


	@Override
	public void save() {
		try {
			ModelSerializer.writeModel(this.pongNN.getNN(), "AIPlayerLearnWithAlgo.trainedmodel", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void load(String name) {
		try {
			this.pongNN.setNN(ModelSerializer.restoreMultiLayerNetwork("AIPlayerLearnWithAlgo.trainedmodel"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
