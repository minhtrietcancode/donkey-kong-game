package objects;
import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents a banana projectile thrown by an intelligent monkey.
 * Bananas move horizontally and can harm Mario on contact.
 * 
 * @author Minh Triet Pham
 */
public class Banana extends ProjectileEntity{
    /**
     * The image representing the banana
     */
    private final Image BANANA_IMAGE;   

    /**
     * The horizontal speed of the banana
     */
    private static final double MOVE_SPEED = Physics.BANANA_MOVE_SPEED;

    /**
     * The maximum travel distance of the banana
     */
    private static final int MAX_DISTANCE = Physics.BANANA_MAX_DISTANCE;
    
    /**
     * Creates a new banana projectile at the specified position.
     *
     * @param startX The initial x-coordinate
     * @param startY The initial y-coordinate
     * @param directionRight Whether the banana moves right (true) or left (false)
     */
    public Banana(double startX, double startY, boolean directionRight) {
        super(startX, startY, directionRight);
        this.BANANA_IMAGE = new Image("res/banana.png");
    }

    /**
     * Updates the banana's position for the current frame.
     * Returns false if the banana has reached its maximum travel distance.
     *
     * @return True if the banana is still active, false if it has reached maximum distance
     */
    @Override
    public boolean update() {
        if (!this.isActive()) {
            return false;
        }

        // Move the banana horizontally
        if (this.isFacingRight()) {
            this.setX(getX() + MOVE_SPEED);
        } else {
            this.setX(getX() - MOVE_SPEED);
        }
        this.setDistanceTravelled(getDistanceTravelled() + MOVE_SPEED);

        // Check if we've reached max distance
        if (getDistanceTravelled() >= MAX_DISTANCE) {
            this.deactivate();
            return false;
        }

        // Draw the banana
        draw();
        return true;
    }

    /**
     * Checks if the banana is colliding with Mario.
     *
     * @param mario The Mario object to check collision with
     * @return True if colliding, false otherwise
     */
    public boolean isCollidingWithMario(Mario mario) {
        if (!this.isActive()) {
            return false;
        }
        return getBoundingBox().intersects(mario.getBoundingBox());
    }

    /**
     * Gets the banana's bounding box for collision detection.
     *
     * @return A Rectangle representing the banana's collision area
     */
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (BANANA_IMAGE.getWidth() / 2),
                getY() - (BANANA_IMAGE.getHeight() / 2),
                BANANA_IMAGE.getWidth(),
                BANANA_IMAGE.getHeight()
        );
    }

    /**
     * Draws the banana on screen.
     */
    @Override
    public void draw() {
        BANANA_IMAGE.draw(getX(), getY());
    }
}