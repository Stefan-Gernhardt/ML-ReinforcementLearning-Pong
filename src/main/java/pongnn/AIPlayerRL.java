package pongnn;

import java.util.ArrayList;
import java.util.List;

import pongUi.GameUI;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class AIPlayerRL extends Player {
	public static final double threshold = 0.6;
	
	private PongNN pongNN = null;
	
	public AIPlayerRL(boolean setSide) {
		super(setSide);
		pongNN = new PongNN();
	}	
	
	@Override
	public boolean isHuman() {
		return false;
	}
	


	@Override
	public int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI) {
		INDArray input  = playgroundValuesToInputMatrix(gameUI);
        List<INDArray> result = pongNN.getNN().feedForward(input, false);
        INDArray output = result.get(PongNN.COUNT_LAYERS);
        
        
        if(withUI) {
        	System.out.println("output vector: " + output.toString());
        }

        
        int move = getWinnerOutputNeuron(output);
        double valueMove = output.getDouble(0, move);

        if(valueMove < threshold) { // pick random move
        	move = (int)(Math.random() * 3);
        }
        
        storeDecision(input, output, move);
        
        return move;
	}
	

	@Override
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
		int numberOfDecisions = this.inputList.size();
		
        double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];

        
		if(wonThePoint)  {
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

	            for(int outputNeuron=0; outputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; outputNeuron++) {
	            	inputArrayDouble[i][outputNeuron] = inputINDArray.getDouble(0, outputNeuron); 
	            }
	        }

	        pongNN.getNN().fit(Nd4j.create(inputArrayDouble), Nd4j.create(outputArrayDouble));
		}
		else {
			// ArrayList<INDArray> inputINDArrayForTraining  = new ArrayList<INDArray>(); 
			// ArrayList<INDArray> outputINDArrayForTraining = new ArrayList<INDArray>(); 
			
			
	        for(int i=0; i<numberOfDecisions; i++) {
	        	
	        	INDArray inputINDArray = inputList.get(i);
	        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
	                inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
	        	}
	        	
	        	INDArray outputINDArray = outputList.get(i);
	            int selectedMove = moveList.get(i);
	            
	            if(outputINDArray.getDouble(0, selectedMove) > threshold) {
	    			outputArrayDouble[i][0] = 0.5 * outputINDArray.getDouble(0, selectedMove) + outputINDArray.getDouble(0, 0);
	    			outputArrayDouble[i][1] = 0.5 * outputINDArray.getDouble(0, selectedMove) + outputINDArray.getDouble(0, 1);
	    			outputArrayDouble[i][2] = 0.5 * outputINDArray.getDouble(0, selectedMove) + outputINDArray.getDouble(0, 2);
	    			
	    			outputArrayDouble[i][selectedMove] = 0.0;
	    			
	    			double sum = outputArrayDouble[i][0] + outputArrayDouble[i][1] + outputArrayDouble[i][2];

	    			outputArrayDouble[i][0] = outputArrayDouble[i][0] / sum; 
	    			outputArrayDouble[i][1] = outputArrayDouble[i][1] / sum;
	    			outputArrayDouble[i][2] = outputArrayDouble[i][2] / sum;
	    			
	            }
	            else {
	    			double sum = outputINDArray.getDouble(0, 0) + outputINDArray.getDouble(0, 1) + outputINDArray.getDouble(0, 2);

	    			outputArrayDouble[i][0] = outputINDArray.getDouble(0, 0) / sum;
	    			outputArrayDouble[i][1] = outputINDArray.getDouble(0, 1) / sum;
	    			outputArrayDouble[i][2] = outputINDArray.getDouble(0, 2) / sum;
	            }

	        }
	        pongNN.getNN().fit(Nd4j.create(inputArrayDouble), Nd4j.create(outputArrayDouble));
		}
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
