package pongUi;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferStrategy;

public class GameUI extends Canvas  {
	
	public final static int Running = 1;
	public final static int Stopped_We_have_a_winner = 2;
	public final static int Reset = 0;
	public final static int OFFSET_X = 16;
	public final static int OFFSET_Y = 39;
	
	private int gameState = Reset;
	private Paddle winner = null;

	private static final long serialVersionUID = 1L;

	public final static int WIDTH  = 200; 
	public final static int HEIGHT = 140; 

	private Ball ball;

	private Paddle leftPaddle=null;
	private Paddle rightPaddle=null;

	private MainMenu menu = null; 
	private static WindowUI windowUI = null;
	
	public GameUI(boolean withUI) {

		if(withUI) {
			canvasSetup();
			windowUI = new WindowUI("Machine Learning Pong", this);
		}

		initialise();
		
		this.addKeyListener(new KeyInput(leftPaddle, rightPaddle));
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu);
		this.setFocusable(true);		
	}

	private void initialise() {
		ball = new Ball();

		leftPaddle  = new Paddle(Color.green, true);
		rightPaddle = new Paddle(Color.blue, false);
	}
	
	
	public void reset(double startPointVariationDefault, double startAngleVariationDefault) {
		ball.reset();
		gameState = Reset;
	}
	
	
	public void start() {
		gameState = Running;
		ball.start();
		leftPaddle.reset();
		rightPaddle.reset();
	}
	
	
	public void stop() {
		ball.stop();
	}
	

	private void canvasSetup() {
		// this.setPreferredSize(new Dimension(getw(), geth()));
		// this.setMaximumSize(new Dimension(WIDTH, HEIGHT));

		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
	}



	public void draw() {
		// Initialize drawing tools first before drawing

		BufferStrategy buffer = this.getBufferStrategy(); // extract buffer so we can use them
		// a buffer is basically like a blank canvas we can draw on

		if (buffer == null) { // if it does not exist, we can't draw! So create it please
			this.createBufferStrategy(3); // Creating a Triple Buffer
			/*
			 * triple buffering basically means we have 3 different canvases this is used to
			 * improve performance but the drawbacks are the more buffers, the more memory
			 * needed so if you get like a memory error or something, put 2 instead of 3.
			 * 
			 * BufferStrategy:
			 * https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferStrategy.html
			 */

			return;
		}

		Graphics g = buffer.getDrawGraphics(); // extract drawing tool from the buffers
		/*
		 * Graphics is class used to draw rectangles, ovals and all sorts of shapes and
		 * pictures so it's a tool used to draw on a buffer
		 * 
		 * Graphics: https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
		 */

		// draw background
		drawBackground(g);

		// draw ball
		ball.draw(g);

		// draw paddles (score will be drawn with them)
		leftPaddle.draw(g);
		rightPaddle.draw(g);

		// actually draw
		g.dispose(); // Disposes of this graphics context and releases any system resources that it
						// is using
		buffer.show(); // actually shows us the 3 beautiful rectangles we drew...LOL

	}

	/**
	 * draw the background
	 * 
	 * @param g - tool to draw
	 */
	private void drawBackground(Graphics g) {
		// black background
		g.setColor(Color.black);
		g.fillRect(0, 0, getw(), geth());

		// Dotted line in the middle
		g.setColor(Color.white);
		Graphics2D g2d = (Graphics2D) g; // a more complex Graphics class used to draw Objects (as in give in an Object
											// in parameter and not dimensions or coordinates)
		// How to make a dotted line:
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0);
		g2d.setStroke(dashed);
		g.drawLine(getw() / 2, 0, getw() / 2, geth());
	}

	/**
	 * update settings and move all objects
	 */
	public void update() {
		// update ball (movements)
		Paddle result = ball.update(leftPaddle, rightPaddle);
		if(result != null) { 
			gameState = Stopped_We_have_a_winner;
			winner = result;
			return;
		}

		// update paddles (movements)
		leftPaddle.update(ball);
		rightPaddle.update(ball);
	}

	/**
	 * used to keep the value between the min and max
	 * 
	 * @param value - integer of the value we have
	 * @param min   - minimum integer
	 * @param max   - maximum integer
	 * @return: the value if value is between minimum and max, minimum is returned
	 *          if value is smaller than minimum, maximum is returned if value is
	 *          bigger than maximum
	 */
	public static int ensureRange(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * returns the sign (either 1 or -1) of the input
	 * 
	 * @param d - a double for the input
	 * @return 1 or -1
	 */
	public static int sign(double d) {
		if (d <= 0)
			return -1;

		return 1;
	}
	
	
	public int getGameState() {
		return this.gameState;
	}

	
	public Paddle getLeftPaddle() {
		return leftPaddle;
	}

	
	public Paddle getRightPaddle() {
		return rightPaddle;
	}

	public Paddle getWinner() {
		return winner;
	}

	public int getYPosLeftPlayer() {
		return leftPaddle.getPlayerYPos();
	}

	public int getYPosMax() {
		return geth() - leftPaddle.getPaddleHeight();
	}

	public double getYPosRightPlayer() {
		return rightPaddle.getPlayerYPos();
	}

	public double getXPosBall() {
		return ball.getX();
	}

	public double getXPosMaxBall() {
		return getw() - ball.SIZE;
	}

	public double getYPosBall() {
		return ball.getY();
	}

	public double getYPosMaxBall() {
		return geth() - ball.SIZE;
	}

	public double getXSpeedBall() {
		return ball.getxVel();
	}

	public double getYSpeedBall() {
		return ball.getyVel();
	}

	public void setStartPoint(double d) {
		ball.setStartPoint(d);
	}

	public void setAnglePoint(double startAngle) {
		ball.setStartAngle(startAngle);
	}
	
	public Ball getBall() {
		return ball;
	}

	public void resetScore() {
		leftPaddle.resetScore();
		rightPaddle.resetScore();
	}
	
	
	public static int geth() {
		if(windowUI == null) return HEIGHT; 
		else return windowUI.getHeigth() - GameUI.OFFSET_Y;
	}
	
	
	public static int getw() {
		if(windowUI == null) return WIDTH; 
		else return windowUI.getWidth() - GameUI.OFFSET_X;
	}
	
	
	public static double getScaleFactorX() {
		return (windowUI.getWidth() - GameUI.OFFSET_X) / (1.0*WIDTH);
	}

	
	public static double getScaleFactorY() {
		return (windowUI.getHeigth() - GameUI.OFFSET_Y) / (1.0*HEIGHT);
		
	}
	
	
}
