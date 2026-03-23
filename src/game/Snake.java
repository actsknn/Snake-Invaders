package game;

import java.awt.Graphics;
import java.util.ArrayList;

/*
CLASS: Snake
DESCRIPTION: The enemy snake that moves in a Space Invaders-like pattern.
             Starts at the top-right, moves left across the screen. When it
             hits the left boundary it drops one row and reverses to the right,
             and vice versa. Individual segments can be destroyed externally
             (remove from getSegments()). The snake keeps moving until it
             either touches the ship or every segment is destroyed, at which
             point a new, slightly longer and faster wave begins.
AUTHOR: Joe Chen
*/
public class Snake extends Polygon {

    public static final int SEGMENT_SIZE = 20;
    private static final int LEFT_BOUNDARY  = 10;
    private static final int RIGHT_BOUNDARY = 790;
    private static final int START_Y = 40;

    private ArrayList<Polygon> segments;
    private double speed;
    private int direction; // -1 = moving left, 1 = moving right

    // Creates a snake with the given number of segments and movement speed.
    // Segments are laid out horizontally from the right side of the screen.
    public Snake(int length, double speed) {
        // Polygon requires a real shape; we use a 1x1 placeholder since
        // all drawing is handled through the internal segments list.
        super(new Point[]{new Point(0,0), new Point(1,0),
                          new Point(1,1), new Point(0,1)},
              new Point(0, 0), 0.0);

        this.speed     = speed;
        this.direction = -1; // start heading left

        segments = new ArrayList<>();

        // Build segments left-to-right (index 0 = leading left edge)
        double startX = RIGHT_BOUNDARY - SEGMENT_SIZE;
        for (int i = 0; i < length; i++) {
            Point[] shape = {
                new Point(0, 0),
                new Point(SEGMENT_SIZE, 0),
                new Point(SEGMENT_SIZE, SEGMENT_SIZE),
                new Point(0, SEGMENT_SIZE)
            };
            segments.add(new Polygon(shape,
                new Point(startX - i * SEGMENT_SIZE, START_Y), 0.0));
        }
    }

    // Moves all segments by speed in the current direction, then checks
    // boundaries and drops + reverses when a wall is reached.
    public void move() {
        if (segments.isEmpty()) return;

        for (Polygon seg : segments) {
            seg.position.x += direction * speed;
        }

        // Find the current left and right extents of the whole snake
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        for (Polygon seg : segments) {
            if (seg.position.x < minX)                    minX = seg.position.x;
            if (seg.position.x + SEGMENT_SIZE > maxX)     maxX = seg.position.x + SEGMENT_SIZE;
        }

        if (direction == -1 && minX <= LEFT_BOUNDARY) {
            // Correct overshoot so the snake stays flush with the boundary
            double overshoot = LEFT_BOUNDARY - minX;
            for (Polygon seg : segments) {
                seg.position.x += overshoot;
                seg.position.y += SEGMENT_SIZE;
            }
            direction = 1;
        } else if (direction == 1 && maxX >= RIGHT_BOUNDARY) {
            double overshoot = maxX - RIGHT_BOUNDARY;
            for (Polygon seg : segments) {
                seg.position.x -= overshoot;
                seg.position.y += SEGMENT_SIZE;
            }
            direction = -1;
        }
    }

    // Draws every remaining segment in whatever color is currently set on the brush.
    public void paint(Graphics brush) {
        for (Polygon seg : segments) {
            Point[] pts = seg.getPoints();
            int[] xCoords = new int[pts.length];
            int[] yCoords = new int[pts.length];
            for (int i = 0; i < pts.length; i++) {
                xCoords[i] = (int) pts[i].x;
                yCoords[i] = (int) pts[i].y;
            }
            brush.fillPolygon(xCoords, yCoords, pts.length);
        }
    }

    // Returns true once every segment has been removed.
    public boolean isFullyDestroyed() {
        return segments.isEmpty();
    }

    // Checks whether any segment overlaps the given ship polygon.
    public boolean collidesWithShip(Ship ship) {
        for (Polygon seg : segments) {
            // Check if any corner of this segment is inside the ship
            Point[] segPts = seg.getPoints();
            for (Point p : segPts) {
                if (ship.contains(p)) return true;
            }
            // Also check if any corner of the ship is inside this segment
            Point[] shipPts = ship.getPoints();
            for (Point p : shipPts) {
                if (seg.contains(p)) return true;
            }
        }
        return false;
    }

    // Exposes the segment list so external code can remove destroyed segments.
    public ArrayList<Polygon> getSegments() {
        return segments;
    }

    public int getSegmentCount() {
        return segments.size();
    }
}
