package pongnn;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.api.ndarray.INDArray;

import pongUi.GameUI;
import pongnn.PongNN;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;


public class AIPlayer extends Player {
	private PongNN pongNN = null;

	
	public AIPlayer(boolean setSide) {
		super(setSide);
		pongNN = new PongNN();
	}

	@Override
	public boolean isHuman() {
		return false;
	}
	

	
	public int calculateMoveAndStoreMove(GameUI gameUI, boolean withUI) {
		INDArray input  = playgroundValuesToInputMatrix(gameUI);
        List<INDArray> result = pongNN.getNN().feedForward(input, false);
        INDArray output = result.get(PongNN.COUNT_LAYERS);
        
        
        if(withUI) {
        	System.out.println("output vector: " + output.toString());
        }
        
        int move = getWinnerOutputNeuron(output);
        
        storeDecision(input, output, move);
        
        return move;
	}
	
	
	public void trainAfterPlayingOnePoint1() {
        double[][] inputArrayDouble0  = new double[1][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble0 = new double[1][PongNN.NUMBER_OF_OUTPUT_NEURONS];

        double[][] inputArrayDouble1  = new double[1][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble1 = new double[1][PongNN.NUMBER_OF_OUTPUT_NEURONS];
        
        inputArrayDouble0[0][0] = 0.0; 
        inputArrayDouble0[0][1] = 0.0; 
        inputArrayDouble0[0][2] = 0.0; 
        inputArrayDouble0[0][3] = 0.0; 
        inputArrayDouble0[0][4] = 0.0; 
        inputArrayDouble0[0][5] = 0.0; 
        inputArrayDouble0[0][6] = 0.0; 
        inputArrayDouble0[0][7] = 0.0; 

        outputArrayDouble0[0][0] = 1.0; 
        outputArrayDouble0[0][1] = 0.0; 
        outputArrayDouble0[0][2] = 0.0;

        inputArrayDouble1[0][0] = 1.0; 
        inputArrayDouble1[0][1] = 1.0; 
        inputArrayDouble1[0][2] = 1.0; 
        inputArrayDouble1[0][3] = 1.0; 
        inputArrayDouble1[0][4] = 1.0; 
        inputArrayDouble1[0][5] = 1.0; 
        inputArrayDouble1[0][6] = 1.0; 
        inputArrayDouble1[0][7] = 1.0; 

        outputArrayDouble1[0][0] = 0.0; 
        outputArrayDouble1[0][1] = 0.0; 
        outputArrayDouble1[0][2] = 1.0;

		// pongNN.getNNO().stochasticGradientDescentAdam(inputArrayDouble, outputArrayDouble);

		// INDArray inputZero = convertInputToINDArray(0,0,0,0,0,0,0,0);
		// INDArray inputOne = convertInputToINDArray(1,1,1,1,1,1,1,1);
		// System.out.println(pongNN.getNN().apply(inputZero).toString());

        /*
		for(int i=0; i<10000; i++) {
			pongNN.getNNO().stochasticGradientDescentAdam(inputArrayDouble0, outputArrayDouble0);
			pongNN.getNNO().stochasticGradientDescentAdam(inputArrayDouble1, outputArrayDouble1);
			System.out.println(pongNN.getNN().apply(inputZero).toString());
			System.out.println(pongNN.getNN().apply(inputOne).toString());
		}
		*/
	}
	
	
	public void trainAfterPlayingOnePoint2() {
        double[][] inputArrayDouble  = new double[2][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[2][PongNN.NUMBER_OF_OUTPUT_NEURONS];

        inputArrayDouble[0][0] = 0.0; 
        inputArrayDouble[0][1] = 0.0; 
        inputArrayDouble[0][2] = 0.0; 
        inputArrayDouble[0][3] = 0.0; 
        inputArrayDouble[0][4] = 0.0; 
        inputArrayDouble[0][5] = 0.0; 
        inputArrayDouble[0][6] = 0.0; 
        inputArrayDouble[0][7] = 0.0; 

        outputArrayDouble[0][0] = 1.0; 
        outputArrayDouble[0][1] = 0.0; 
        outputArrayDouble[0][2] = 0.0;

        inputArrayDouble[1][0] = 1.0; 
        inputArrayDouble[1][1] = 1.0; 
        inputArrayDouble[1][2] = 1.0; 
        inputArrayDouble[1][3] = 1.0; 
        inputArrayDouble[1][4] = 1.0; 
        inputArrayDouble[1][5] = 1.0; 
        inputArrayDouble[1][6] = 1.0; 
        inputArrayDouble[1][7] = 1.0; 

        outputArrayDouble[1][0] = 0.0; 
        outputArrayDouble[1][1] = 0.0; 
        outputArrayDouble[1][2] = 1.0;

		// pongNN.getNNO().stochasticGradientDescentAdam(inputArrayDouble, outputArrayDouble);

		INDArray inputZero = convertInputToMatrix(0,0,0,0,0,0,0,0);
		INDArray inputOne = convertInputToMatrix(1,1,1,1,1,1,1,1);

		List<INDArray> result0 = pongNN.getNN().feedForward(inputZero, false);
		INDArray output0 = result0.get(PongNN.COUNT_LAYERS);
		System.out.println(output0.toString());

		for(int i=0; i<10000; i++) {
			List<INDArray> result1 = pongNN.getNN().feedForward(inputZero, false);
			INDArray output1 = result1.get(PongNN.COUNT_LAYERS);
			System.out.println(output1.toString());

			List<INDArray> result2 = pongNN.getNN().feedForward(inputOne, false);
			INDArray output2 = result2.get(PongNN.COUNT_LAYERS);
			System.out.println(output2.toString());
		}
	}
	
	
	public void trainAfterPlayingOnePoint1(boolean wonThePoint) {
		int numberOfDecisions = this.inputList.size();
		// System.out.println("numberOfDecisions: " + numberOfDecisions);
		
        double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];
        
        
        
        for(int i=0; i<numberOfDecisions; i++) {
        	double lambda = (1.0*i)/(1.0*numberOfDecisions);
        	
        	
        	INDArray inputINDArray = inputList.get(i);
        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
                inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
        	}

        	
        	INDArray outputINDArray = outputList.get(i);
            int move = getWinnerOutputNeuron(outputINDArray);
            
            if(wonThePoint) {
            	/*
                outputArrayDouble[i][0] = 0.0; 
                outputArrayDouble[i][1] = 0.0; 
                outputArrayDouble[i][2] = 0.0;

                outputArrayDouble[i][move] = 1.0;
                */

                outputArrayDouble[i][0] = outputINDArray.getDouble(0, 0);
                outputArrayDouble[i][1] = outputINDArray.getDouble(0, 1); 
                outputArrayDouble[i][2] = outputINDArray.getDouble(0, 2);

            	/*
            	outputArrayDouble[i][0] = 0.0 * lambda + (1-lambda) * outputINDArray.get(0, 0);
            	outputArrayDouble[i][1] = 0.0 * lambda + (1-lambda) * outputINDArray.get(0, 1);
            	outputArrayDouble[i][2] = 0.0 * lambda + (1-lambda) * outputINDArray.get(0, 2);

                outputArrayDouble[i][move] = 1.0 * lambda + (1-lambda) * outputINDArray.get(0, move);
                */

            }
            else {
            	double valueOfFalseDecision = outputINDArray.getDouble(0, move); 
            	
            	/*
            	outputArrayDouble[i][0] = outputINDArray.get(0, 0) + (valueOfFalseDecision/2.0);
            	outputArrayDouble[i][1] = outputINDArray.get(0, 1) + (valueOfFalseDecision/2.0);
            	outputArrayDouble[i][2] = outputINDArray.get(0, 2) + (valueOfFalseDecision/2.0);
            	*/

                /*
                outputArrayDouble[i][0] = outputINDArray.get(0, 0);
                outputArrayDouble[i][1] = outputINDArray.get(0, 1); 
                outputArrayDouble[i][2] = outputINDArray.get(0, 2);
                */

            	/*
            	outputArrayDouble[i][0] = 0.5;
            	outputArrayDouble[i][1] = 0.5;
            	outputArrayDouble[i][2] = 0.5;
            	
            	outputArrayDouble[i][move] = 0.0;
            	*/
            	
                
            	outputArrayDouble[i][0] = 0.5 * lambda + (1-lambda) * outputINDArray.getDouble(0, 0);
            	outputArrayDouble[i][1] = 0.5 * lambda + (1-lambda) * outputINDArray.getDouble(0, 1);
            	outputArrayDouble[i][2] = 0.5 * lambda + (1-lambda) * outputINDArray.getDouble(0, 2);
            	
                outputArrayDouble[i][move] = 0.0 * lambda + (1-lambda) * outputINDArray.getDouble(0, move);
                // outputArrayDouble[i][move] = 0.0;

            }
        	
            for(int outputNeuron=0; outputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; outputNeuron++) {
            	inputArrayDouble[i][outputNeuron] = inputINDArray.getDouble(0, outputNeuron); 
            }

        }

        pongNN.getNN().fit(Nd4j.create(inputArrayDouble), Nd4j.create(outputArrayDouble));
	}
	
	
	public void trainAfterPlayingOnePoint2(boolean wonThePoint) {
		if(wonThePoint) return;
		
		int numberOfDecisions = this.inputList.size();
		// System.out.println("numberOfDecisions: " + numberOfDecisions);
		
        double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];
        
        
    	double lambda = 1;
        for(int i=numberOfDecisions-1; i>=0; i--) {
        	lambda = lambda * 0.95; 
        	
        	INDArray inputINDArray = inputList.get(i);
        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
                inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
        	}
        	
        	INDArray outputINDArray = outputList.get(i);
            int move = getWinnerOutputNeuron(outputINDArray);
            
        	outputArrayDouble[i][0] = 0.5 * lambda + (1-lambda) * outputINDArray.getDouble(0, 0);
        	outputArrayDouble[i][1] = 0.5 * lambda + (1-lambda) * outputINDArray.getDouble(0, 1);
        	outputArrayDouble[i][2] = 0.5 * lambda + (1-lambda) * outputINDArray.getDouble(0, 2);
        	
            outputArrayDouble[i][move] = 0.0 * lambda + (1-lambda) * outputINDArray.getDouble(0, move);
            // outputArrayDouble[i][move] = 0.0;

            for(int outputNeuron=0; outputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; outputNeuron++) {
            	inputArrayDouble[i][outputNeuron] = inputINDArray.getDouble(0, outputNeuron); 
            }

        }

        pongNN.getNN().fit(Nd4j.create(inputArrayDouble), Nd4j.create(outputArrayDouble));
	}
	
	
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
		int numberOfDecisions = this.inputList.size();
		// System.out.println("numberOfDecisions: " + numberOfDecisions);
		
        double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];

        
		if(wonThePoint) {
			/*
	        for(int i=numberOfDecisions-1; i>=0; i--) {
	        	
	        	INDArray inputINDArray = inputList.get(i);
	        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
	                inputArrayDouble[i][inputNeuron] = inputINDArray.get(0, inputNeuron); 
	        	}
	        	
	        	INDArray outputINDArray = outputList.get(i);
	            int move = getWinnerOutputNeuron(outputINDArray);

    			// outputArrayDouble[i][0] = 0.0 * lambda + (1-lambda) * outputINDArray.get(0, 0);
    			// outputArrayDouble[i][1] = 0.0 * lambda + (1-lambda) * outputINDArray.get(0, 1);
    			// outputArrayDouble[i][2] = 0.0 * lambda + (1-lambda) * outputINDArray.get(0, 2);

    			// outputArrayDouble[i][move] = 1.0 * lambda + (1-lambda) * outputINDArray.get(0, move);
    			
    			outputArrayDouble[i][0] = 0.0;
    			outputArrayDouble[i][1] = 0.0;
    			outputArrayDouble[i][2] = 0.0;
    			
    			outputArrayDouble[i][move] = 1.0;

	            for(int outputNeuron=0; outputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; outputNeuron++) {
	            	inputArrayDouble[i][outputNeuron] = inputINDArray.get(0, outputNeuron); 
	            }
	        }
	        

	        double cost = 0;
	        int i = 0;
        	do {
        		cost = pongNN.getNNO().stochasticGradientDescentAdam(inputArrayDouble, outputArrayDouble);
        		System.out.println("i: " + i++ + "   cost: "+ cost);
        		
        	} while ((cost > 0.001) && (i<1000));
        	*/
		}
		else {
	    	double lambda = 1;
	        for(int i=numberOfDecisions-1; i>=0; i--) {
	        	lambda = lambda * 0.95; 
	        	
	        	INDArray inputINDArray = inputList.get(i);
	        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
	                inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
	        	}
	        	
	        	INDArray outputINDArray = outputList.get(i);
	            int move = getWinnerOutputNeuron(outputINDArray);
	            
    			// outputArrayDouble[i][0] = lambda * 0.5 * outputINDArray.get(0, move) + (1-lambda) * outputINDArray.get(0, 0);
    			// outputArrayDouble[i][1] = lambda * 0.5 * outputINDArray.get(0, move) + (1-lambda) * outputINDArray.get(0, 1);
    			// outputArrayDouble[i][2] = lambda * 0.5 * outputINDArray.get(0, move) + (1-lambda) * outputINDArray.get(0, 2);

    			outputArrayDouble[i][0] = 0.5 * outputINDArray.getDouble(0, move) + outputINDArray.getDouble(0, 0);
    			outputArrayDouble[i][1] = 0.5 * outputINDArray.getDouble(0, move) + outputINDArray.getDouble(0, 1);
    			outputArrayDouble[i][2] = 0.5 * outputINDArray.getDouble(0, move) + outputINDArray.getDouble(0, 2);
    			
    			// outputArrayDouble[i][move] = 0.0 * lambda + (1-lambda) * outputINDArray.get(0, move);
    			outputArrayDouble[i][move] = 0.0;

	            for(int outputNeuron=0; outputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; outputNeuron++) {
	            	inputArrayDouble[i][outputNeuron] = inputINDArray.getDouble(0, outputNeuron); 
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
