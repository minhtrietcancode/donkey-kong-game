package objects;
import bagel.Image;
import bagel.util.Rectangle;

/**
 * Represents a Hammer collectible in the game.
 * The hammer can be collected by the player, at which point it disappears from the screen.
 * 
 * @author Minh Triet Pham
 */
public class Hammer extends CollectibleEntity{
    /**
     * The image representing the hammer
     */
    private final Image HAMMER_IMAGE;

    /**
     * The width and height of the hammer
     */
    private final double WIDTH, HEIGHT;

    /**
     * Constructs a Hammer at the specified position.
     *
     * @param startX The initial x-coordinate of the hammer.
     * @param startY The initial y-coordinate of the hammer.
     */
    public Hammer(double startX, double startY) {
        super(startX, startY);
        this.HAMMER_IMAGE = new Image("res/hammer.png");
        this.WIDTH = HAMMER_IMAGE.getWidth();
        this.HEIGHT = HAMMER_IMAGE.getHeight();
    }

    /**
     * Returns the bounding box of the hammer for collision detection.
     * If the hammer has been collected, it returns an off-screen bounding box.
     *
     * @return A {@link Rectangle} representing the hammer's bounding box.
     */
    @Override
    public Rectangle getBoundingBox() {
        if (this.isCollected()) {
            return new Rectangle(-1000, -1000, 0, 0); // Move off-screen if collected
        }
        return new Rectangle(
                getX() - (WIDTH / 2),  // Center-based positioning
                getY() - (HEIGHT / 2),
                WIDTH,
                HEIGHT
        );
    }

    /**
     * Draws the hammer on the screen if it has not been collected.
     */
    @Override
    public void draw() {
        // Just draw if the Hammer has not collected
        if (!this.isCollected()) {
            HAMMER_IMAGE.draw(getX(), getY()); // Bagel centers images automatically
        }
    }
}
