package pongnn;

import java.util.function.DoubleUnaryOperator;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

public class PongNN {
	
	// input neurons (all input neurons between 0 and 1.0
	// 0 = y-pos left player
	// 1 = y-pos right player
	
	// 2 = x-pos ball
	// 3 = y-pos ball
	
	// 4 = left speed ball
	// 5 = right speed ball
	// 6 = up-speed ball
	// 7 = down-speed ball
	
	// output neurons
	// 0 = up
	// 1 = down
	// 2 = stay
	
	
	// input layer 8 neurons
	// hidden layer 8 neurons
	// output layer 3 neurons
	
	public final static int NUMBER_OF_INPUT_NEURONS = 8;
	public final static int NUMBER_OF_MIDDLE_NEURONS = 64; 
	public final static int NUMBER_OF_OUTPUT_NEURONS = 3;
	public final static int COUNT_LAYERS = 2;

	public final static int RNG_SEED= 4711;
	public final static double LEARNING_RATE = 0.001; // 0.0015
	
	
	
	private MultiLayerNetwork nn = null;
	public MultiLayerNetwork getNN() {
		return nn;
	}

	public void setNN(MultiLayerNetwork mln) {
		nn = mln;
	}


	public PongNN() {
		createNeuralNetwork();
	}
	
	private void createNeuralNetwork() {
		Activation activation = Activation.SIGMOID;
		// Activation activation = Activation.RELU;
		
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                // .seed(RNG_SEED) //include a random seed for reproducibility
                .seed((int)(Math.random() * 1000)) //include a random seed for reproducibility
                .activation(activation)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nadam())
                .l2(LEARNING_RATE * 0.001) // regularize learning model // * 0.005
                .list()
                .layer(new DenseLayer.Builder() //create the input and hidden layer
                        .nIn(NUMBER_OF_INPUT_NEURONS)
                        .activation(activation)
                        .nOut(NUMBER_OF_MIDDLE_NEURONS)
                        .build())
                /*
                .layer(new DenseLayer.Builder() //create hidden layer 2
                        .nIn(NUMBER_OF_MIDDLE_NEURONS)
                        .activation(activation)
                        .nOut(NUMBER_OF_OUTPUT_NEURONS)
                        .build())
                        */
                .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create output layer
                        .activation(Activation.SOFTMAX)
                        .nOut(NUMBER_OF_OUTPUT_NEURONS)
                        .build())
                .build(); 	
    	
        nn = new MultiLayerNetwork(conf);
        nn.init();
	}
}
