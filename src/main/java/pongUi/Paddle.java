package pongUi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * class for the paddle
 * 
 * @author Zayed
 *
 */
public class Paddle {

	private int x, y; // positions
	private int vel = 0; // speed and direction of paddle
	private int speed = 8; // 10; // speed of the paddle movement
	private int width = 16; // 16; // 22;
	private int height = 30; //5; 
	private int score = 0; // score for the player
	private Color color; // color of the paddle
	private boolean left; // true if it's the left paddle

	public boolean isLeft() {
		return left;
	}

	/**
	 * create initial properties for the paddle
	 * 
	 * @param color - color of the paddle
	 * @param left  - boolean to know if it's the left paddle or not
	 */
	public Paddle(Color c, boolean left) {
		// initial properties
		color = c;
		this.left = left;

		reset();
	}
	

	/**
	 * add a point to the player
	 */
	public void addPoint() {
		score++;
	}

	/**
	 * Draw paddle (a rectangle), Draw score too
	 * 
	 * @param g - Graphics object used to draw everything
	 */
	public void draw(Graphics g) {

		// draw paddle
		g.setColor(color);
		g.fillRect(x, y, getWidthScaled(), getHeightScaled());

		// draw score
		int sx; // x position of the string
		int padding = 25; // space between dotted line and score
		String scoreText = Integer.toString(score);
		Font font = new Font("Roboto", Font.PLAIN, (int)(10*GameUI.getScaleFactorX()));

		if(left) {
			int strWidth = g.getFontMetrics(font).stringWidth(scoreText); // we need the width of the string so we can
																		  // center it properly (for perfectionists)
			sx = GameUI.getw() / 2 - padding - strWidth;
		} else {
			sx = GameUI.getw() / 2 + padding;
		}

		g.setFont(font);
		g.drawString(scoreText, sx, 50);
	}

	/**
	 * update position AND collision tests
	 * 
	 * @param b - the ball
	 */
	public void update(Ball b) {
		// System.out.println("Paddle " + left + " velocicty: " + vel);
		
		// update position
		y = GameUI.ensureRange(y + vel, 0, GameUI.geth() - getHeightScaled());

		// collisions
		int ballX = b.getX();
		int ballY = b.getY();

		if (left) {
			if (ballX <= (getWidthScaled() + x) && (ballY + Ball.SIZE) >= y && ballY <= (y + getHeightScaled())) {
				b.changeXDirToRightSide(vel);
			}
		} else {
			if ((ballX + Ball.SIZE) >= x && (ballY + Ball.SIZE) >= y && ballY <= (y + getHeightScaled()))
				b.changeXDirToLeftSide(vel);
		}

	}


	/**
	 * stop moving the paddle
	 */
	public void stop() {
		vel = 0;
	}
	
	
	public void up() {
		vel = (int)(-speed * GameUI.getScaleFactorY());
	}
	
	public void down() {
		vel = (int)(speed * GameUI.getScaleFactorY());
	}
	
	public void stay() {
		vel = 0;
	}

	public int getPlayerYPos() {
		return y;
	}
	
	public int getPaddleHeight() {
		return getHeightScaled();
	}
	
	public void reset() {
		if(left) // different x values if right or left paddle
			x = 0;
		else
			x = GameUI.getw() - getWidthScaled();

		y = GameUI.geth() / 2 - getHeightScaled() / 2;	
	}

	public void resetScore() {
		score = 0;
	}
	
	private int getHeightScaled() {
		return 	(int)(height * GameUI.getScaleFactorY());
	}
	
	private int getWidthScaled() {
		return 	(int)(width * GameUI.getScaleFactorX());
	}
}
