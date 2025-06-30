package objects;
import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents a stationary platform in the game.
 * Platforms provide surfaces for Mario to walk on and interact with.
 * 
 * @author Minh Triet Pham
 */
public class Platform extends Entity{
    /**
     * The image representing the platform
     */ 
    private final Image PLATFORM_IMAGE;

    /**
     * The width and height of the platform
     */
    private final double WIDTH, HEIGHT;

    /**
     * Constructs a platform at the specified position.
     *
     * @param startX The initial x-coordinate of the platform.
     * @param startY The initial y-coordinate of the platform.
     */
    public Platform(double startX, double startY) {
        super(startX, startY);

        // Load platform sprite
        this.PLATFORM_IMAGE = new Image("res/platform.png");

        // Set platform dimensions based on the image size
        this.WIDTH = PLATFORM_IMAGE.getWidth();
        this.HEIGHT = PLATFORM_IMAGE.getHeight();
    }

    /**
     * Draws the platform on the screen.
     */
    @Override
    public void draw() {
        PLATFORM_IMAGE.draw(getX(), getY());
    }

    /**
     * Retrieves the width of the platform.
     *
     * @return The width of the platform.
     */
    public double getWidth() {
        return WIDTH;
    }

    /**
     * Retrieves the height of the platform.
     *
     * @return The height of the platform.
     */
    public double getHeight() {
        return HEIGHT;
    }

    /**
     * Returns a center-based bounding box that aligns with how the platform is drawn.
     * This bounding box is used for collision detection.
     *
     * @return A {@link Rectangle} representing the platform's bounding box.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (WIDTH / 2),
                getY() - (HEIGHT / 2),
                WIDTH,
                HEIGHT
        );
    }
}
