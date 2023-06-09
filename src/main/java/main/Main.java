package main;

import java.util.Scanner;

import pongUi.UI;
import pongnn.AIPlayerRLRandom;
import pongnn.HumanPlayer;
import pongnn.Player;
import pongnn.PlayerAlgo;
import pongnn.PlayerGuess;

public class Main {
	static int numberPointsPerGame = 10;
	

	public static void main(String[] args) {
		System.out.println("pong");
		
		if(args.length == 0) {
			System.out.println("parameter 1 for: AlgoVsAI_trained");
			System.out.println("parameter 2 for: AlgoVsAI_training");
			System.out.println("parameter 3 for: HumanVsAlgo");
			System.out.println("parameter 4 for: HumanVsAI");
			System.out.println("parameter 5 for: HumanVsPlayerGuess");
			System.out.println("parameter 6 for: AIvsAI_trained");
			System.out.println("parameter 7 for: AIvsAI_training");
			
			System.exit(0);
		}
		else {
			if(args[0].contains("1")) {
				Main.mainAlgoVsAI_trained(null);
			} else 
			if(args[0].contains("2")) {
				Main.mainAlgoVsAI_training(null);
			} else 
			if(args[0].contains("3")) {
				Main.mainHumanVsAlgo(null);
			} else 
			if(args[0].contains("4")) {
				Main.mainHumanVsAI(null);
			} else 
			if(args[0].contains("5")) {
				Main.mainHumanVsPlayerGuess(null);
			} else 
			if(args[0].contains("6")) {
				Main.mainAIvsAI_trained(null);
			} else 
			if(args[0].contains("7")) {
				Main.mainAIvsAI_training(null);
			}  
		}

		// Main.mainAlgoVsAI_trained(args);
		// Main.mainAlgoVsAI_training(args); 
		
		// Main.mainHumanVsAlgo(args);
		// Main.mainHumanVsAI(args);
		// Main.mainHumanVsPlayerGuess(args);
		
		
		// Main.mainAIvsAI_trained(args);
		// Main.mainAIvsAI_training(args); 
	}

	
	public static void mainAlgoVsAI_trained(String[] args) {
		Player leftPlayer  = new PlayerAlgo(true);
		Player rightPlayer = new AIPlayerRLRandom(false);
		
		rightPlayer.load("AIPlayerRLRandom.trainedmodel--Right");
		
		
		Pong pong = new Pong();
		
		UI ui = new UI(true);
		
		int maxLoop = 1000000000;
		for(int i=0; i<maxLoop; i++) {
			int counterLeftWin = 0;
			int counterRightWin = 0;
			
			ui.resetScore();

			for(int j=0; counterLeftWin<numberPointsPerGame && counterRightWin<numberPointsPerGame; j++) {
				int result = 0;
				
				
				System.out.println("-----------------------------------------------------------------------");
				result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, true);
				System.out.println("result: " + result + "  (0=left win, 1=left win, 2=right win)");
				
				if(result == 1) counterLeftWin++;
				if(result == 2) counterRightWin++;
			}
			
			System.out.println("counterLeftWin: " + counterLeftWin + "  counterRightWin: " + counterRightWin + "  loop: " + i);
		}
		
		System.out.println("pong ended");	
	}
	
	
	public static void mainHumanVsAlgo(String[] args) {
		Player leftPlayer  = new HumanPlayer(true);
		Player rightPlayer = new PlayerAlgo(false);
		
		Pong pong = new Pong();
		
		UI ui = new UI(true);
		
		int maxLoop = 1000000000;
		for(int i=0; i<maxLoop; i++) {
			int counterLeftWin = 0;
			int counterRightWin = 0;
			
			ui.resetScore();

			for(int j=0; counterLeftWin<numberPointsPerGame && counterRightWin<numberPointsPerGame; j++) {
				int result = 0;
				
				
				// System.out.println("-----------------------------------------------------------------------");
				result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, true);
				// System.out.println("result: " + result + "  (0=left win, 1=left win, 2=right win)");
				
				if(result == 1) counterLeftWin++;
				if(result == 2) counterRightWin++;
			}
			
			// System.out.println("counterLeftWin: " + counterLeftWin + "  counterRightWin: " + counterRightWin + "  loop: " + i);
		}
		
		System.out.println("pong ended");	
	}

	
	
	public static void mainHumanVsAI(String[] args) {
		Player leftPlayer  = new HumanPlayer(true);
		Player rightPlayer = new AIPlayerRLRandom(false);

		rightPlayer.load("AIPlayerRLRandom.trainedmodel--Right");
		
		Pong pong = new Pong();
		
		UI ui = new UI(true);
		
		int maxLoop = 1000000000;
		for(int i=0; i<maxLoop; i++) {
			int counterLeftWin = 0;
			int counterRightWin = 0;
			
			ui.resetScore();

			for(int j=0; counterLeftWin<numberPointsPerGame && counterRightWin<numberPointsPerGame; j++) {
				int result = 0;
				
				
				System.out.println("-----------------------------------------------------------------------");
				result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, true);
				System.out.println("result: " + result + "  (0=left win, 1=left win, 2=right win)");
				
				if(result == 1) counterLeftWin++;
				if(result == 2) counterRightWin++;
			}
			
			System.out.println("counterLeftWin: " + counterLeftWin + "  counterRightWin: " + counterRightWin + "  loop: " + i);
		}
		
		System.out.println("pong ended");	
	}


	public static void mainHumanVsPlayerGuess(String[] args) {
		Player leftPlayer  = new HumanPlayer(true);
		Player rightPlayer = new PlayerGuess(false);

		rightPlayer.load("AIPlayerRLRandom.trainedmodel--Right");
		
		Pong pong = new Pong();
		
		UI ui = new UI(true);
		
		int maxLoop = 1000000000;
		for(int i=0; i<maxLoop; i++) {
			int counterLeftWin = 0;
			int counterRightWin = 0;
			
			ui.resetScore();

			for(int j=0; counterLeftWin<numberPointsPerGame && counterRightWin<numberPointsPerGame; j++) {
				int result = 0;
				
				
				System.out.println("-----------------------------------------------------------------------");
				result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, true);
				System.out.println("result: " + result + "  (0=left win, 1=left win, 2=right win)");
				
				if(result == 1) counterLeftWin++;
				if(result == 2) counterRightWin++;
			}
			
			System.out.println("counterLeftWin: " + counterLeftWin + "  counterRightWin: " + counterRightWin + "  loop: " + i);
		}
		
		System.out.println("pong ended");	
	}


	public static void mainAIvsAI_trained(String[] args) {
		Player leftPlayer  = new AIPlayerRLRandom(true);
		Player rightPlayer = new AIPlayerRLRandom(false);

		leftPlayer.load("AIPlayerRLRandom.trainedmodel--Left");
		rightPlayer.load("AIPlayerRLRandom.trainedmodel--Right");
		
		Pong pong = new Pong();
		UI ui = new UI(true);
		
		double startPoint = 0.5;
		double startAngle = 1.0;
		
		boolean uiMode = false;
		
		int countBallexchanges = 100;
		int countMatches = 100;
		for(int i=0; i<countMatches; i++) {
			int counterLeftWin = 0;
			int counterRightWin = 0;
			double winRate = 0.0;		
			
			ui.setStartPoint(startPoint);
			ui.setStartAngle(startAngle);

			
			// playing
			counterLeftWin = 0;
			counterRightWin = 0;
			for(int j=0; j<countBallexchanges; j++) {
				int result = 0;
				ui.setStartPoint(startPoint);
				ui.setStartAngle(startAngle);
				
				if(j==0) uiMode = true;
				else uiMode = false;
				
				result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, uiMode);
				
				if(result == 0) counterLeftWin++;
				if(result == 1) counterLeftWin++;
				if(result == 2) counterRightWin++;
			}
			
			winRate = (1.0 * counterLeftWin) / (1.0 * countBallexchanges);			
			System.out.println("loop: " + i + "  counterLeftWin: " + counterLeftWin + "  counterRightWin: " + counterRightWin + "  startPoint: " + startPoint + "  startAngle: " + startAngle + "  winRate: " + winRate);
			
		}
		
		System.out.println("pong ended");
	}
	

	
	
	public static void mainAlgoVsAI_training(String[] args) {
		Player leftPlayer  = new PlayerAlgo(true);
		Player rightPlayer = new AIPlayerRLRandom(false);
		
		
		Pong pong = new Pong();
		
		UI ui = new UI(true);
		
		int lotSize = 999;
		int maxLoop = 1000000000;
		for(int i=0; i<maxLoop; i++) {
			// System.out.print("i: " + i%lotSize + ", ");
			
			int counterLeftWin = 0;
			int counterRightWin = 0;
			double winRate = 0.0;		
			
			ui.resetScore();

			for(int j=0; j<lotSize; j++) {
				int result = 0;
				
				
				if(j == lotSize-1) {
					System.out.println("-----------------------------------------------------------------------");
					result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, true);
					System.out.println("result: " + result + "  (0=left win, 1=left win, 2=right win)");
				}
				else {
					result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, false);  
				}
				
				if(result == 0) counterLeftWin++;
				if(result == 1) counterLeftWin++;
				if(result == 2) counterRightWin++;
			}
			
			winRate = (1.0 * counterLeftWin) / (1.0 * lotSize);			
			System.out.println("counterLeftWin: " + counterLeftWin + "  counterRightWin: " + counterRightWin + "  winRate: " + winRate + "  loop: " + i);
			
			
			if(i % 20 == 0) {
				leftPlayer.save();
				rightPlayer.save();
			}
			
		}
		
		System.out.println("pong ended");
	}

	
	
	public static void mainAIvsAI_training(String[] args) {
		Player leftPlayer  = new AIPlayerRLRandom(true);
		Player rightPlayer = new AIPlayerRLRandom(false);
		
		
		Pong pong = new Pong();
		
		UI ui = new UI(true);
		
		int lotSize = 999;
		int maxLoop = 1000000000;
		for(int i=0; i<maxLoop; i++) {
			// System.out.print("i: " + i%lotSize + ", ");
			
			int counterLeftWin = 0;
			int counterRightWin = 0;
			double winRate = 0.0;		
			
			ui.resetScore();

			for(int j=0; j<lotSize; j++) {
				int result = 0;
				
				
				if(j == lotSize-1) {
					System.out.println("-----------------------------------------------------------------------");
					result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, true);
					System.out.println("result: " + result + "  (0=left win, 1=left win, 2=right win)");
				}
				else {
					result = pong.trainAIReturnToService(ui, leftPlayer, rightPlayer, false);  
				}
				
				if(result == 0) counterLeftWin++;
				if(result == 1) counterLeftWin++;
				if(result == 2) counterRightWin++;
			}
			
			winRate = (1.0 * counterLeftWin) / (1.0 * lotSize);			
			System.out.println("counterLeftWin: " + counterLeftWin + "  counterRightWin: " + counterRightWin + "  winRate: " + winRate + "  loop: " + i);
			
			
			if(i % 20 == 0) {
				leftPlayer.save();
				rightPlayer.save();
			}
			
		}
		
		System.out.println("pong ended");
	}

	
	
	public static void promptEnterKey() {
		System.out.println("Press \"ENTER\" to continue...");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

}
