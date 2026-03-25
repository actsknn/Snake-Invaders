package game;

import java.awt.Graphics;
/**
* CLASS: Projectile
* DESCRIPTION: Represents a laser shot(projectile) fired by the player's ship.
* It travels on a straight path at a constant veloccity based on its initial rotation.
* It is marked out of bounds once it leaves the window. 
* @author Joe Chen
*/

public class Projectile extends Polygon {
    
    private double speed = 15.0;
    public boolean outOfBounds = false;

    /**
    * Constructor declares the shape of the projectile, its starting position offset
    * and its initial rotation (facing up).
     
    * @param startingPosition the starting position of the projectile
    * @param initialRotation the initial rotation in degrees that determines
    * the projectile's direction of travel
    */
    public Projectile(Point startingPosition, double initialRotation){
        super(new Point[]
            {new Point(0, 0),new Point(15, 0),  
            new Point(15, 3), new Point(0, 3)},
            startingPosition, initialRotation);
    }

    /**
    * Moves the projectile forward according to its rotation and speed.
    * Also marks the projectile as out of bounds when it leaves the 800x600 screen.
    */
    public void move(){
        this.position.x += speed * Math.cos(Math.toRadians(rotation)); 
        this.position.y += speed * Math.sin(Math.toRadians(rotation));

        if(this.position.x < 0 || this.position.x > 800 || 
            this.position.y < 0 || this.position.y > 600){
                outOfBounds = true;
            }
    }

    /**
    * Draws the projectile on the screen.
    * 
    * @param brush the Graphics object used to draw the projectile
    */
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
