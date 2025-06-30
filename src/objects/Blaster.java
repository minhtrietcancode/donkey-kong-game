package objects;
import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents a blaster that Mario can pick up and use to shoot bullets.
 * Blasters appear only in level 2 and allow Mario to attack enemies from a distance.
 * 
 * @author Minh Triet Pham
 */
public class Blaster extends CollectibleEntity{ 
    /**
     * The image representing the blaster
     */
    private final Image BLASTER_IMAGE;

    /**
     * The initial number of bullets provided by this blaster
     */
    private static final int INITIAL_BULLETS = Physics.BLASTER_INITIAL_BULLETS;
    
    /**
     * Constructs a new Blaster at the specified position.
     *
     * @param x The x-coordinate of the blaster.
     * @param y The y-coordinate of the blaster.
     */
    public Blaster(double x, double y) {
        super(x, y);
        this.BLASTER_IMAGE = new Image("res/blaster.png");
    }

    /**
     * Gets the width of the blaster image.
     *
     * @return The width of the blaster image.
     */
    public double getWidth() {
        return BLASTER_IMAGE.getWidth();
    }
    
    /**
     * Gets the height of the blaster image.
     *
     * @return The height of the blaster image.
     */
    public double getHeight() {
        return BLASTER_IMAGE.getHeight();
    }

    /**
     * Gets the initial number of bullets provided by this blaster.
     *
     * @return The number of bullets (always 5 for a new blaster).
     */
    public int getInitialBullets() {
        return INITIAL_BULLETS;
    }
    
    /**
     * Gets the bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing the blaster's collision area.
     */
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (BLASTER_IMAGE.getWidth() / 2),
                getY() - (BLASTER_IMAGE.getHeight() / 2),
                BLASTER_IMAGE.getWidth(),
                BLASTER_IMAGE.getHeight()
        );
    }
    
    /**
     * Draws the blaster on the screen if it hasn't been collected.
     */
    @Override
    public void draw() {
        // Just draw if Blaster has not been collected by Mario
        if (!this.isCollected()) {
            BLASTER_IMAGE.draw(getX(), getY());
        }
    }
}