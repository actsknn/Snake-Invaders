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
AUTHOR: Ajay Cunnane, Joe Chen
*/
public class Snake extends Polygon {

    public static final int SEGMENT_SIZE = 40;
    private static final int LEFT_BOUNDARY  = 10;
    private static final int RIGHT_BOUNDARY = 740;
    private static final int START_Y = 40;

    private double speed;

    //RUBRIC REQUIREMENT: Inner Class 1
    // A marker dropped by the head telling trailing segments where to turn
    private class Waypoint {
        double x, y;
        int dirX, dirY;
        
        public Waypoint(double x, double y, int dirX, int dirY) {
            this.x = x; this.y = y;
            this.dirX = dirX; this.dirY = dirY;
        }
    }

    // RUBRIC REQUIREMENT: Inner Class 2
    // Represents a single square of the snake, tracking its own direction
    public class Segment extends Polygon {
        int dirX, dirY;
        int targetWaypointIndex = 0; // Which waypoint is this segment looking for next?

        public Segment(Point[] shape, Point position, double rotation, int dirX, int dirY) {
            super(shape, position, rotation);
            this.dirX = dirX;
            this.dirY = dirY;
        }
    }

    private ArrayList<Segment> segments;
    private ArrayList<Waypoint> waypoints;


    // Creates a snake with the given number of segments and movement speed.
    // Segments are laid out horizontally from the right side of the screen.
    public Snake(int length, double speed) {
        // Polygon requires a real shape; we use a 1x1 placeholder since
        // all drawing is handled through the internal segments list.
        super(new Point[]{new Point(0,0), new Point(1,0), new Point
            (1,1), new Point(0,1)}, new Point(0, 0),
            0.0);

        this.speed = speed;
        segments = new ArrayList<>();
        waypoints = new ArrayList<>();

        double startX = RIGHT_BOUNDARY; 
        for (int i = 0; i < length; i++) {
            Point[] shape = {
                new Point(0, 0),
                new Point(SEGMENT_SIZE, 0),
                new Point(SEGMENT_SIZE, SEGMENT_SIZE),
                new Point(0, SEGMENT_SIZE)
            };
            Segment seg = new Segment(shape, new Point(startX + (i * SEGMENT_SIZE), START_Y), 0.0, -1, 0);
            segments.add(seg);
        }
    }

    // Moves all segments by speed in the current direction, then checks
    // boundaries and drops + reverses when a wall is reached.
    public void move() {
        if (segments.isEmpty()) return;

        // 1. Check if the current HEAD needs to lay down NEW waypoints
        Segment head = segments.get(0);
        
        // ONLY drop new waypoints if this segment has run out of existing ones to follow!
        if (head.targetWaypointIndex == waypoints.size()) {
            if (head.dirX == 1 && head.position.x >= RIGHT_BOUNDARY) {
                waypoints.add(new Waypoint(RIGHT_BOUNDARY, head.position.y, 0, 1));
                waypoints.add(new Waypoint(RIGHT_BOUNDARY, head.position.y + SEGMENT_SIZE, -1, 0));
            } 
            else if (head.dirX == -1 && head.position.x <= LEFT_BOUNDARY) {
                waypoints.add(new Waypoint(LEFT_BOUNDARY, head.position.y, 0, 1));
                waypoints.add(new Waypoint(LEFT_BOUNDARY, head.position.y + SEGMENT_SIZE, 1, 0));
            }
        }

        // 2. Move all segments and check if they step on a waypoint
        for (Segment seg : segments) {
            seg.position.x += seg.dirX * speed;
            seg.position.y += seg.dirY * speed;
            // If this segment has a waypoint it hasn't reached yet
            if (seg.targetWaypointIndex < waypoints.size()) {
                Waypoint wp = waypoints.get(seg.targetWaypointIndex);
                boolean passed = false;
                
                // Did the segment move past the waypoint's coordinates?
                if (seg.dirX == 1 && seg.position.x >= wp.x) passed = true;
                else if (seg.dirX == -1 && seg.position.x <= wp.x) passed = true;
                else if (seg.dirY == 1 && seg.position.y >= wp.y) passed = true;

                if (passed) {
                    // Snap exactly to the corner so the grid alignment stays perfect
                    seg.position.x = wp.x;
                    seg.position.y = wp.y;
                    
                    // Adopt the new direction
                    seg.dirX = wp.dirX;
                    seg.dirY = wp.dirY;
                    
                    // Look for the next waypoint in the list
                    seg.targetWaypointIndex++;
                }
            }    
        }
        
        // If heading right and hits the right wall
        if (head.dirX == 1 && head.position.x >= RIGHT_BOUNDARY) {
            // Drop a waypoint here to turn DOWN
            waypoints.add(new Waypoint(RIGHT_BOUNDARY, head.position.y, 0, 1));
            // Drop a second waypoint right below it to turn LEFT
            waypoints.add(new Waypoint(RIGHT_BOUNDARY, head.position.y + SEGMENT_SIZE, -1, 0));
        } 
        // If heading left and hits the left wall
        else if (head.dirX == -1 && head.position.x <= LEFT_BOUNDARY) {
            waypoints.add(new Waypoint(LEFT_BOUNDARY, head.position.y, 0, 1));
            waypoints.add(new Waypoint(LEFT_BOUNDARY, head.position.y + SEGMENT_SIZE, 1, 0));
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

    // Checks if any part of the snake has touched the bottom boundary
    public boolean reachedBottom(double bottomY) {
        for (Segment seg : segments) {
            // If the segment's Y coordinate is at or past the limit, return true
            if (seg.position.y >= bottomY) {
                return true;
            }
        }
        return false;
    }

    // Exposes the segment list so external code can remove destroyed segments.
    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public int getSegmentCount() {
        return segments.size();
    }
}
