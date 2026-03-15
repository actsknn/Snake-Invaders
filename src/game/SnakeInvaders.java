package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.
AUTHORS: Joe Chen, 
*/
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class SnakeInvaders extends Game {
	static int counter = 0;

	Ship playerShip;

	ArrayList<Projectile> lasers = new ArrayList<>();

	int shootCD = 0;

	// --- Snake / wave state ---
	Snake snake;
	int wave            = 1;
	int initialLength   = 10;
	double initialSpeed = 1.5;
	// Frames to pause between waves (~1.5 s at 100 fps)
	static final int WAVE_BREAK_FRAMES = 150;
	int waveCooldown = 0;
	boolean gameOver = false;

  public SnakeInvaders() {
    super("SnakeInvaders!",800,600);
    this.setFocusable(true);
	this.requestFocus();

	playerShip = new Ship(new Point(width/2 - 30, height - 200));
	this.addKeyListener(playerShip);

	snake = new Snake(initialLength, initialSpeed);
  }
  
	public void paint(Graphics brush) {
    	brush.setColor(Color.black);
    	brush.fillRect(0,0,width,height);
    	
    	// sample code for printing message for debugging
    	// counter is incremented and this message printed
    	// each time the canvas is repainted
    	counter++;
    	brush.setColor(Color.white);
    	brush.drawString("Counter is " + counter,10,10);

		playerShip.move();

		if(shootCD > 0){
			shootCD--;
		}
		if(playerShip.shoot && shootCD <= 0){
			Point[] shipPoints = playerShip.getPoints();
			Point tip = shipPoints[0];
			double spawnX = tip.x - 5;
            double spawnY = tip.y + 1;
			lasers.add(new Projectile(new Point(spawnX, spawnY), playerShip.rotation));
			shootCD = 30;
		}
		for(Projectile laser : lasers){
			laser.move();
			brush.setColor(Color.green);
			laser.paint(brush);
		}

		lasers.removeIf(laser -> laser.outOfBounds);

		// --- Snake logic ---
		if (!gameOver) {
			if (waveCooldown > 0) {
				// Between-wave break: count down then spawn the next snake
				waveCooldown--;
				brush.setColor(Color.yellow);
				brush.drawString("Wave " + wave + " incoming...", width/2 - 60, height/2);
				if (waveCooldown == 0) {
					int newLength = initialLength + (wave - 1) * 2;
					double newSpeed = initialSpeed + (wave - 1) * 0.4;
					snake = new Snake(newLength, newSpeed);
				}
			} else if (snake != null) {
				snake.move();
				brush.setColor(Color.red);
				snake.paint(brush);

				brush.setColor(Color.white);
				brush.drawString("Wave: " + wave, 10, 25);
				brush.drawString("Segments: " + snake.getSegmentCount(), 10, 40);

				if (snake.collidesWithShip(playerShip)) {
					gameOver = true;
				} else if (snake.isFullyDestroyed()) {
					wave++;
					waveCooldown = WAVE_BREAK_FRAMES;
					snake = null;
				}
			}
		}

		if (gameOver) {
			brush.setColor(Color.red);
			brush.drawString("GAME OVER", width/2 - 35, height/2);
		}

		brush.setColor(Color.white);
		playerShip.paint(brush);
  }
  
	public static void main (String[] args) {
   		SnakeInvaders a = new SnakeInvaders();
		a.repaint();
  }
}