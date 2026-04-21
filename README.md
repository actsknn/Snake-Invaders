Here is the clean, standard version ready to be copied and pasted right into your repository.

***

# Snake Invaders

**Snake Invaders** is a Java-based arcade mashup that combines the defensive shooting mechanics of *Space Invaders* with the segmented enemy dynamics of *Snake*. Originally developed as a programming project for CMSC132, this game features a custom 2D rendering engine, dynamic wave progression, and advanced object pathing.

## Features
* **Hybrid Gameplay:** Pilot a ship at the bottom of the screen and fend off a descending, slithering snake by destroying its segments one by one.
* **Advanced Movement Logic:** The enemy snake utilizes a "virtual head" and path-history tracking to create a smooth, follow-the-leader slithering effect for all of its segments.
* **Custom Collision Detection:** Built from scratch without external physics libraries, using custom polygon and point intersection math to register laser hits on individual, moving snake segments.
* **Dynamic Wave System:** The difficulty scales infinitely. Clearing a snake triggers a brief intermission before the next wave spawns a longer, faster snake. 

## Controls
* **Movement:** Arrow Keys (Up, Down, Left, Right)
* **Shoot:** Spacebar (Includes a firing cooldown to prevent laser-spam)
* **Restart:** `R` (Available on the Game Over screen)

## Project Architecture
The game is built entirely in Java using AWT/Graphics for rendering. 

* **`SnakeInvaders.java`**: The main control center and game loop. Handles frame updates, rendering, wave management, and garbage collection for out-of-bound projectiles.
* **`Snake.java`**: Manages the collection of snake segments, their speed, and the path-history queue that dictates how the tail follows the head.
* **`Ship.java`**: Controls player state, boundary restrictions, and rotational calculations.
* **`Projectile.java`**: Manages laser trajectory and handles the `collides()` logic against enemy segments.
* **`Polygon.java` & `Point.java`**: The foundational math classes responsible for shape rendering and vertex-based collision detection.

## How to Run
1. Clone the repository to your local machine.
2. Navigate to the source folder and compile the Java files:
   ```bash
   javac game/*.java
   ```
3. Run the main application:
   ```bash
   java game.SnakeInvaders
   ```
