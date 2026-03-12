package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
CLASS: Ship
DESCRIPTION: This class represents the player's ship in Snake Invaders
It extends Polygon for it to be visually drawn, and implements KeyListener to
process keyboard inputs. It features omnidirectional movement of the ship
(press arrow keys), a way to shoot projectiles (press spacebar), an emote animation
(press e), and boundary testing to keep the ship on screen.
AUTHOR: Joe Chen
 */
public class Ship extends Polygon implements KeyListener{

    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private double stepSize = 5.0;

    private int emoteFramesRemaining = 0;

    public boolean shoot = false;

    //Constructor declares the shape of the ship, its starting position offset
    //and its initial rotation (facing up)
    public Ship(Point startingPosition){
        super(new Point[] {
            new Point(30, 15), new Point(0, 30), 
            new Point(10, 15), new Point(0, 0)}, 
            startingPosition, 270.0);
    }

    //This method features the main movement of the ship, and tests the screen
    //boundaries to keep the ship on the screen
    public void move(){
        if(emoteFramesRemaining > 0){
            this.rotate(30);
            emoteFramesRemaining--;
        }

        double oldX = this.position.x;
        double oldY = this.position.y;

        if (up) {
            this.position.x += stepSize * Math.cos(Math.toRadians(rotation)); 
            this.position.y += stepSize * Math.sin(Math.toRadians(rotation));
        }
        if (down) {
            this.position.x -= stepSize * Math.cos(Math.toRadians(rotation)); 
            this.position.y -= stepSize * Math.sin(Math.toRadians(rotation));
        }
        if (left) {
            this.position.x += stepSize * Math.cos(Math.toRadians(rotation - 90));
            this.position.y += stepSize * Math.sin(Math.toRadians(rotation - 90));
        }
        if (right) {
            this.position.x += stepSize * Math.cos(Math.toRadians(rotation + 90));
            this.position.y += stepSize * Math.sin(Math.toRadians(rotation + 90));  
        }

        Point[] currentPoints = this.getPoints();

        for(int i = 0; i < currentPoints.length; i++){
            Point p = currentPoints[i];

            if(p.x < 0 || p.x > 785){
                this.position.x = oldX;
            }
            if(p.y < 0 || p.y > 565){
                this.position.y = oldY;
            }
        }
    }

    //This method recognizes when keys are pressed and executes movement,
    //shooting, and the emote (if it's not already emoting) accordingly
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_UP){
            up = true;
        } else if(keyCode == KeyEvent.VK_DOWN){
            down = true;
        } else if(keyCode == KeyEvent.VK_LEFT){
            left = true;
        } else if(keyCode == KeyEvent.VK_RIGHT){
            right = true;
        } else if(keyCode == KeyEvent.VK_SPACE){
            shoot = true;
        } else if (keyCode == KeyEvent.VK_E){
            if(emoteFramesRemaining == 0){
                emoteFramesRemaining = 12;
            }
        }
    }

    //This method recognizes when keys are released and stops movement 
    //and shooting accordingly
    public void keyReleased(KeyEvent e){
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            up = false;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            down = false; 
        } else if (keyCode == KeyEvent.VK_LEFT) {
            left = false;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            right = false;
        } else if (keyCode == KeyEvent.VK_SPACE){
            shoot = false;
        }
    }

    public void keyTyped(KeyEvent e){}

    //Paints the ship depending on its position on the screen
    public void paint(Graphics brush){
        Point[] currentPoints = this.getPoints();
        int[] xCoords = new int[currentPoints.length];
        int[] yCoords = new int[currentPoints.length];

        for(int i = 0; i < currentPoints.length; i++){
            xCoords[i] = (int) currentPoints[i].x;
            yCoords[i] = (int) currentPoints[i].y;
        }
        
        brush.fillPolygon(xCoords, yCoords, currentPoints.length);
    }
}
