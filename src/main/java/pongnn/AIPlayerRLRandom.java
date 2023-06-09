package pongnn;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import pongUi.GameUI;
import pongUi.Paddle;
import pongUi.UI;


public class AIPlayerRLRandom extends Player {
	
	
	public final static double unsharp = 0.1;
	
	protected PongNN pongNN = null;
	
	
	public AIPlayerRLRandom(boolean setSide) {
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

        int move = getWinnerOutputNeuronViaDiceRoll(output);

		
        if(withUI) {
        	System.out.println("output vector: " + output.toString() + "   input vector: " + input.toString());
        	
        	// System.out.print("move(a): " + move);
        	// System.out.print("  output vector: ");
        	
        	/*
        	for(int i=0; i<output.size(1); i++) {
        		System.out.print(String.format("%.2f", output.getDouble(0, i)) + " ");
        	}
        	System.out.print("  input vector: ");
        	for(int i=0; i<input.size(1); i++) {
        		System.out.print(String.format("%.2f", input.getDouble(0, i)) + " ");
        	}
        	System.out.println();        	
        	*/
        	
       }
        
        storeDecision(input, output, move);
        
        return move;
	}
	

	@Override
	public void trainAfterPlayingOnePoint(boolean wonThePoint, boolean withUI) {
		// pseudo code (python)
		// 
		// y = np.zeros(number_of_actions)
		// y[selectedMove] = 1
		// action_prob_grads.append(y - action_prob)
	    // Y = action_probs + lambda * learning_rate * rewards * action_prob_grads // lambda = far decisions not important and recent decisions important
        // model.train_on_batch(X, Y)
		
		int numberOfDecisions = this.inputList.size();
        
		double[][] inputArrayDouble  = new double[numberOfDecisions][PongNN.NUMBER_OF_INPUT_NEURONS];
        double[][] outputArrayDouble = new double[numberOfDecisions][PongNN.NUMBER_OF_OUTPUT_NEURONS];
		
        for(int i=0; i<numberOfDecisions; i++) {
        	double lambda = (1.0*(i+1))/(1.0*numberOfDecisions);
            int selectedMove = moveList.get(i);

            INDArray outputINDArray = outputList.get(i);
            outputArrayDouble[i][0] = -outputINDArray.getDouble(0, 0);
			outputArrayDouble[i][1] = -outputINDArray.getDouble(0, 1);
			outputArrayDouble[i][2] = -outputINDArray.getDouble(0, 2);
        	
        	outputArrayDouble[i][selectedMove] = outputArrayDouble[i][selectedMove] + 1;
        	
        	double reward = 1;
        	if(wonThePoint) reward = 1;
        	else reward = -1;

        	double learningRate = 0.5;
        	
            outputArrayDouble[i][0] = reward * lambda * learningRate * outputArrayDouble[i][0];
			outputArrayDouble[i][1] = reward * lambda * learningRate * outputArrayDouble[i][1];
			outputArrayDouble[i][2] = reward * lambda * learningRate * outputArrayDouble[i][2];
        	
            outputArrayDouble[i][0] = outputINDArray.getDouble(0, 0) + outputArrayDouble[i][0];
			outputArrayDouble[i][1] = outputINDArray.getDouble(0, 1) + outputArrayDouble[i][1];
			outputArrayDouble[i][2] = outputINDArray.getDouble(0, 2) + outputArrayDouble[i][2];
        	
			INDArray inputINDArray = inputList.get(i);
        	for(int inputNeuron=0; inputNeuron<PongNN.NUMBER_OF_INPUT_NEURONS; inputNeuron++) {
                if(inputINDArray.getDouble(0, inputNeuron) < 0) inputArrayDouble[i][inputNeuron] = 0.0; 
                else if(inputINDArray.getDouble(0, inputNeuron) > 1) inputArrayDouble[i][inputNeuron] = 1.0; 
                else inputArrayDouble[i][inputNeuron] = inputINDArray.getDouble(0, inputNeuron); 
        	}
        }
        if(withUI) { 
        	// System.out.println(Nd4j.create(inputArrayDouble).toString());
        	// System.out.println();
        	System.out.println(Nd4j.create(outputArrayDouble).toString());
        }
        
        pongNN.getNN().fit(Nd4j.create(inputArrayDouble), Nd4j.create(outputArrayDouble));		
	}

	
	@Override
	public void save() {
		try {
		    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		    //get current date time with Date()
		    Date date = new Date();
		    // System.out.println(dateFormat.format(date));			
			
			ModelSerializer.writeModel(this.pongNN.getNN(), "AIPlayerRLRandom.trainedmodel" + "--" + side + "--" + dateFormat.format(date), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void load(String name) {
		try {
			this.pongNN.setNN(ModelSerializer.restoreMultiLayerNetwork(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


