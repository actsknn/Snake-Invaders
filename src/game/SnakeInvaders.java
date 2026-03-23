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
	//static int counter = 0;

	Ship playerShip;

	ArrayList<Projectile> lasers = new ArrayList<>();

	int shootCD = 0;

	// --- Snake / wave state ---
	Snake snake;
	int wave = 1;
	int initialLength = 12;
	double initialSpeed = 3;
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

	//RUBRIC REQUIREMENT: 1 Anonymous Class
        // We are creating an anonymous subclass of KeyAdapter right here on the fly!
        // It has no name, it just exists exactly where it is needed.
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // If the game is over and the player presses 'R', restart the game!
                if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
                    gameOver = false;
                    wave = 1;
                    snake = new Snake(initialLength, initialSpeed);
                    lasers.clear(); 
                    
                    // Reset the ship's position back to the start
                    playerShip.position.x = width / 2 - 30;
                    playerShip.position.y = height - 200;
                }
            }
        });

	snake = new Snake(initialLength, initialSpeed);
  }

	public void paint(Graphics brush) {
    	brush.setColor(Color.black);
    	brush.fillRect(0,0,width,height);

		playerShip.move();

		if(shootCD > 0){
			shootCD--;
		}
		if(playerShip.shoot && shootCD <= 0){
			Point[] shipPoints = playerShip.getPoints();
			Point tip = shipPoints[0];
			double spawnX = tip.x - 7;
            double spawnY = tip.y + 1;
			lasers.add(new Projectile(new Point(spawnX, spawnY), playerShip.rotation));
			shootCD = 25;
		}
		for(Projectile laser : lasers){
			laser.move();
			brush.setColor(Color.green);
			laser.paint(brush);
		}

		lasers.removeIf(laser -> laser.outOfBounds);

		// --- Added Collision Logic ---
        // We only check for collisions if the game isn't over and the snake currently exists
        if (!gameOver && snake != null) {
            
            // We use standard 'for' loops here (instead of for-each) because we are 
            // modifying the lists (removing items) while iterating through them.
            for (int i = 0; i < lasers.size(); i++) {
                Projectile laser = lasers.get(i);
                boolean laserHit = false;

                // Get the list of snake segments
                ArrayList<Snake.Segment> segments = snake.getSegments();
                
                for (int j = 0; j < segments.size(); j++) {
                    Snake.Segment segment = segments.get(j);
                    
                    // Call the collides method we wrote earlier in Polygon.java
                    if (laser.collides(segment)) {
                        segments.remove(j); // Destroy the snake segment
                        laserHit = true;    // Mark the laser for destruction
                        break;              // Stop checking this laser against other segments
                    }
                }

                // If the laser hit something, remove it from the game
                if (laserHit) {
                    lasers.remove(i);
                    i--; // Decrement 'i' so we don't skip the next laser in the list
                }
            }
        }
		
		// --- Snake logic ---
		if (!gameOver) {
			if (waveCooldown > 0) {
				// Between-wave break: count down then spawn the next snake
				waveCooldown--;

				Font waveFont = new Font("DialogInput", Font.BOLD, 30);
				brush.setFont(waveFont);
				FontMetrics waveMetrics = brush.getFontMetrics(waveFont);
				String incomingText = "Wave " + wave + " incoming...";
                int incomingWidth = waveMetrics.stringWidth(incomingText);
				brush.setColor(Color.yellow);
				brush.drawString(incomingText, (width - incomingWidth) / 2, height / 2);

				if (waveCooldown == 0) {
					int newLength = initialLength + (wave - 1) * 2;
					double newSpeed = initialSpeed + (wave - 1) * 0.4;
					snake = new Snake(newLength, newSpeed);
				}
			} else if (snake != null) {
				snake.move();
				brush.setColor(Color.red);
				snake.paint(brush);

				brush.setFont(new Font("DialogInput", Font.BOLD, 20));
                brush.setColor(Color.white);
                brush.drawString("Wave: " + wave, 10, 20);
                brush.drawString("Segments: " + snake.getSegmentCount(), 10, 45);

				if (snake.collidesWithShip(playerShip) || snake.reachedBottom(550)) {
					gameOver = true;
				} else if (snake.isFullyDestroyed()) {
					wave++;
					waveCooldown = WAVE_BREAK_FRAMES;
					snake = null;
				}
			}
		}

		if (gameOver) {
            Font giantFont = new Font("DialogInput", Font.BOLD, 60);
            brush.setFont(giantFont);
            FontMetrics giantMetrics = brush.getFontMetrics(giantFont);
            int gameOverWidth = giantMetrics.stringWidth("GAME OVER");
            
            brush.setColor(Color.red);
            brush.drawString("GAME OVER", (width - gameOverWidth) / 2, height / 2 - 20);
            
            Font smallerFont = new Font("DialogInput", Font.BOLD, 30);
            brush.setFont(smallerFont);
            FontMetrics smallerMetrics = brush.getFontMetrics(smallerFont);
            String waveText = "You reached Wave: " + wave;
            int waveWidth = smallerMetrics.stringWidth(waveText);
            
            brush.setColor(Color.white);
            brush.drawString(waveText, (width - waveWidth) / 2, height / 2 + 30);
		}

		brush.setColor(Color.white);
		playerShip.paint(brush);
  }

	public static void main (String[] args) {
   		SnakeInvaders a = new SnakeInvaders();
		a.repaint();
  }
}