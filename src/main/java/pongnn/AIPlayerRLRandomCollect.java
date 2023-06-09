package pongnn;

import java.util.ArrayDeque;
import java.util.ArrayList;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class AIPlayerRLRandomCollect extends AIPlayerRLRandom {
	
	
	private ArrayDeque<BallExchange> sBE;
	
	
	public AIPlayerRLRandomCollect(boolean setSide) {
		super(setSide);
	}

	
	@Override
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
		int numberOfDecisions = inputList.size();
		
        double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];

		if(!wonThePoint)  {
			trainWithRecentSuccessfulBallExchanges();
		}
		else {
			addBallExchangeToListOfSuccessfulBallExchanges();
			
			int counter = 0;
	        for(int i=0; i<numberOfDecisions; i++) {
	        	
	            int selectedMove = moveList.get(i);
	            
            	outputArrayDouble[counter][0] = 0.00 + unsharp;
            	outputArrayDouble[counter][1] = 0.00 + unsharp;
            	outputArrayDouble[counter][2] = 0.00 + unsharp;
            	
            	outputArrayDouble[counter][selectedMove] = 1- unsharp;;
            	
            	// validate
    			if(outputArrayDouble[counter][0] < unsharp) outputArrayDouble[counter][0] = unsharp; 
    			if(outputArrayDouble[counter][1] < unsharp) outputArrayDouble[counter][1] = unsharp; 
    			if(outputArrayDouble[counter][2] < unsharp) outputArrayDouble[counter][2] = unsharp; 
    			if(outputArrayDouble[counter][0] > (1-unsharp)) outputArrayDouble[counter][0] = 1-unsharp; 
    			if(outputArrayDouble[counter][1] > (1-unsharp)) outputArrayDouble[counter][1] = 1-unsharp; 
    			if(outputArrayDouble[counter][2] > (1-unsharp)) outputArrayDouble[counter][2] = 1-unsharp; 

    			INDArray inputINDArray = inputList.get(i);
	        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
	                inputArrayDouble[counter][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
	        	}
            	counter++;
	        }
	        
	        double[][] iAD = new double[counter][PongNN.NUMBER_OF_INPUT_NEURONS];
	        double[][] oAD = new double[counter][PongNN.NUMBER_OF_OUTPUT_NEURONS];

	        
	        for(int i=0; i<counter; i++) {
	        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
	        		iAD[i][inputNeuron] = inputArrayDouble[i][inputNeuron]; 
	        	}
	        	
	        	oAD[i][0] = outputArrayDouble[i][0]; 
	        	oAD[i][1] = outputArrayDouble[i][1];
	        	oAD[i][2] = outputArrayDouble[i][2];
	        }	        

	        // System.out.println(oAD.toString());
			pongNN.getNN().fit(Nd4j.create(iAD), Nd4j.create(oAD));
		}
	}

	
	public void trainWithRecentSuccessfulBallExchanges() {
		// System.out.println("trainWithRecentSuccessfulBallExchanges   count: " + ballExchangeDeque.size());
		
		int counter = 0;
		double ae = 0;
		for(BallExchange ballExchange : ballExchangeDeque) {
			ae = ae + trainWithBallExchange(ballExchange);
			counter++;
		}
		
		ae = ae / (1.0 * counter);
    	// System.out.println("loss trainWithRecentSuccessfulBallExchanges: " + ae);
	}


	public double trainWithBallExchange(BallExchange ballExchange) {
		ArrayList<INDArray> inputList = ballExchange.getInputList();
		ArrayList<Integer> moveList = ballExchange.getMoveList();

		
		int numberOfDecisions = inputList.size();
		
        double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];
        
        for(int i=0; i<numberOfDecisions; i++) {
        	
            int selectedMove = moveList.get(i);
            
        	outputArrayDouble[i][0] = 0.00 + unsharp;
        	outputArrayDouble[i][1] = 0.00 + unsharp;
        	outputArrayDouble[i][2] = 0.00 + unsharp;
        	
        	outputArrayDouble[i][selectedMove] = 1- unsharp;;
        	
        	// validate
			if(outputArrayDouble[i][0] < unsharp) outputArrayDouble[i][0] = unsharp; 
			if(outputArrayDouble[i][1] < unsharp) outputArrayDouble[i][1] = unsharp; 
			if(outputArrayDouble[i][2] < unsharp) outputArrayDouble[i][2] = unsharp; 
			if(outputArrayDouble[i][0] > (1-unsharp)) outputArrayDouble[i][0] = 1-unsharp; 
			if(outputArrayDouble[i][1] > (1-unsharp)) outputArrayDouble[i][1] = 1-unsharp; 
			if(outputArrayDouble[i][2] > (1-unsharp)) outputArrayDouble[i][2] = 1-unsharp; 

			INDArray inputINDArray = inputList.get(i);
        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
                inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
        	}
        }
        
    	// double e = 
    	pongNN.getNN().fit(Nd4j.create(inputArrayDouble), Nd4j.create(outputArrayDouble));
    	return pongNN.getNN().score();
	}
	

}


// W = 200; 
// H = 70;
// 