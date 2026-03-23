package game;

import java.awt.Graphics;
/*
CLASS: Projectile
DESCRIPTION: Represents a laser shot fired by the player's ship.
It travels on a straight path at a constant veloccity based on its initial rotation
AUTHOR: Joe Chen
 */
public class Projectile extends Polygon {
    
    private double speed = 15.0;
    public boolean outOfBounds = false;

    //Constructor declares the shape of the projectile, its starting position offset
    //and its initial rotation (facing up)
    public Projectile(Point startingPosition, double initialRotation){
        super(new Point[]
            {new Point(0, 0),new Point(10, 0),  
            new Point(10, 2), new Point(0, 2)},
            startingPosition, initialRotation);
    }

    //The projectile moves forward based on the direction it was fired at a constant speed
    //It's marked out of bounds when it leaves the 800x600 screen
    public void move(){
        this.position.x += speed * Math.cos(Math.toRadians(rotation)); 
        this.position.y += speed * Math.sin(Math.toRadians(rotation));

        if(this.position.x < 0 || this.position.x > 800 || 
            this.position.y < 0 || this.position.y > 600){
                outOfBounds = true;
            }
    }

    //Paints the projectile depending on its position on the screen
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
