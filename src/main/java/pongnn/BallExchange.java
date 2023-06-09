package pongnn;

import java.util.ArrayList;

import org.nd4j.linalg.api.ndarray.INDArray;

public class BallExchange {
	private ArrayList<INDArray>  inputList  = null;
	public ArrayList<INDArray> getInputList() {
		return inputList;
	}

	public ArrayList<INDArray> getOutputList() {
		return outputList;
	}

	public ArrayList<Integer> getMoveList() {
		return moveList;
	}

	private ArrayList<INDArray>  outputList = null;
	private ArrayList<Integer> moveList   = null;
	
	BallExchange() {
		inputList  = new ArrayList<INDArray>();
		outputList = new ArrayList<INDArray>();
		moveList   = new ArrayList<Integer>();		
	}

	public BallExchange(ArrayList<INDArray> inputList2, ArrayList<INDArray> outputList2, ArrayList<Integer> moveList2) {
		inputList  = inputList2;
		outputList = outputList2;
		moveList   = moveList2; 		
	}
}
