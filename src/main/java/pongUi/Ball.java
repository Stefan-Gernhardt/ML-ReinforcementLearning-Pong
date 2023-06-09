package pongUi;

import java.awt.Color;
import java.awt.Graphics;

/**
 * class for the ball in the game
 * 
 * @author Zayed
 *
 */
public class Ball {
	public static final int SIZE = 4; // 16;
	public static final double START_SPEED = 6; // 8; // 16;
	public static final double MAX_SPEED = 16; 

	private double x, y; // position of top left corner of square
	private double xVel, yVel; 
	// private double speed = START_SPEED; // speed of the ball
	public static final double startPointVariationDefault = 0.5; // 0.0 - 1.0 (1.0 means full variation)
	public static final double startAngleVariationDefault = 1.0; // 0.0 - 1.0 (1.0 means full variation)

	private double startPointVariation = startPointVariationDefault;
	private double startAngleVariation = startAngleVariationDefault;
	
	double devAngle = 15.0;
	double maxMinAngle = 60.0;
	double speedIncreaseFactor = 1.20;	
	/**
	 * constructor
	 */
	public Ball() {
		reset();
	}

	public void reset() {
		// initial position
		x = GameUI.getw() / 2 - SIZE / 2;
		y = (int)(Math.random() * GameUI.geth() * startPointVariation + (1-startPointVariation) * (GameUI.geth()/ 2));
		
		// initial velocity
		xVel = GameUI.sign(Math.random() * 2.0 - 1) * Ball.START_SPEED;
		yVel = GameUI.sign(Math.random() * 2.0 - 1) * xVel * Math.random() * startAngleVariation;
	}	
	
	
	public void start() {
		reset();
	}


	public void stop() {
		xVel = 0;
		yVel = 0;
	}
	

	/**
	 * Draw ball (a square)
	 * 
	 * @param g: Graphics object used to draw everything
	 */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(this.getX(), this.getY(), (int)(SIZE*GameUI.getScaleFactorX()), (int)(SIZE*GameUI.getScaleFactorY()));
	}

	/**
	 * update position AND collision tests
	 * 
	 * @param lp: left paddle
	 * @param rp: right paddle
	 */
	public Paddle update(Paddle lp, Paddle rp) {
		Paddle winner = null;

		// update position
		x = x + xVel * GameUI.getScaleFactorX(); // * speed;
		y = y + yVel * GameUI.getScaleFactorY(); // * speed;

		// collisions

		// with ceiling and floor
		if (y + SIZE >= GameUI.geth())
			changeYDirToDown();

		if (y <= 0)
			changeYDirToUp();

		// with walls
		if (x + SIZE > GameUI.getw()) { // right side
			lp.addPoint();
			winner = lp;
			stop();
			// reset();
		}
		
		if (x < 0) { // left side
			rp.addPoint();
			winner = rp;
			stop();
			// reset();
		}
		
		
		return winner;
	}
	

	/**
	 * @return the x
	 */
	public int getX() {
		return (int)x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return (int)y;
	}

	
	public void changeXDirToRightSide(int velOfPaddle) {
		if(xVel < 0) {
			xVel *= -1;
			
			double k = xVel / yVel;
			double alphaDegree =  (Math.atan(k) * 180) / Math.PI;

			if(velOfPaddle <  0) {
				alphaDegree = alphaDegree + devAngle;
				double devK = Math.tan((alphaDegree * Math.PI) / 180);
				if(devK > maxMinAngle) devK = maxMinAngle;
				if(devK < -maxMinAngle) devK = -maxMinAngle;
				
				yVel = xVel / devK; 
			}
			else if (velOfPaddle > 0) {
				alphaDegree = alphaDegree - devAngle;
				double devK = Math.tan((alphaDegree * Math.PI) / 180);
				if(devK > maxMinAngle) devK = maxMinAngle;
				if(devK < -maxMinAngle) devK = -maxMinAngle;
				
				yVel = xVel / devK; 
			}
			else {  // input angle is output angle
				// yVel = yVel; 
			}
			
			// increase speed after every paddle hit
			xVel = xVel * speedIncreaseFactor;
			yVel = yVel * speedIncreaseFactor;
			
			if(xVel > MAX_SPEED)  xVel = MAX_SPEED;
			if(xVel < -MAX_SPEED) xVel = -MAX_SPEED;
			
			if(yVel > MAX_SPEED)  yVel = MAX_SPEED;
			if(yVel < -MAX_SPEED) yVel = -MAX_SPEED;
		}
	}
	

	public void changeXDirToLeftSide(int velOfPaddle) {
		if(xVel > 0) {
			xVel *= -1;
			
			double k = xVel / yVel;
			double alphaDegree =  (Math.atan(k) * 180) / Math.PI;
			
			if(velOfPaddle <  0) {
				alphaDegree = alphaDegree - devAngle;
				double devK = Math.tan((alphaDegree * Math.PI) / 180);
				if(devK > maxMinAngle) devK = maxMinAngle;
				if(devK < -maxMinAngle) devK = -maxMinAngle;
				
				yVel = xVel / devK; 
			}
			else if (velOfPaddle > 0) {
				alphaDegree = alphaDegree + devAngle;
				double devK = Math.tan((alphaDegree * Math.PI) / 180);
				if(devK > maxMinAngle) devK = maxMinAngle;
				if(devK < -maxMinAngle) devK = -maxMinAngle;
				
				yVel = xVel / devK; 
			}
			else {  // input angle is output angle
				// yVel = yVel; 
			}
			
			// increase speed after every paddle hit
			xVel = xVel * speedIncreaseFactor;
			yVel = yVel * speedIncreaseFactor;
			
			if(xVel > MAX_SPEED)  xVel = MAX_SPEED;
			if(xVel < -MAX_SPEED) xVel = -MAX_SPEED;
			
			if(yVel > MAX_SPEED)  yVel = MAX_SPEED;
			if(yVel < -MAX_SPEED) yVel = -MAX_SPEED;
		}
	}

	public void changeYDirToUp() {
		if(yVel <= 0) yVel = -yVel;
	}

	public void changeYDirToDown() {
		if(yVel >= 0) yVel = -yVel;
	}

	public double getxVel() {
		return xVel;
	}

	public double getyVel() {
		return yVel;
	}

	public void setStartPoint(double d) {
		startPointVariation = d;
	}

	public void setStartAngle(double d) {
		startAngleVariation = d;
	}

	
	public void setY(double y) {
		this.y = y;
	}
	
	
	public void setVelY(double yVel) {
		this.yVel = yVel;
	}
}
