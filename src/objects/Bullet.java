package objects;
import bagel.*;
import bagel.util.Rectangle;
/**
 * Represents a bullet fired by Mario when using a blaster.
 * Bullets move horizontally and can damage Donkey Kong or destroy monkeys.
 * 
 * @author Minh Triet Pham
 */
public class Bullet extends ProjectileEntity{
    /**
     * The image representing the bullet facing left
     */
    private final Image BULLET_IMAGE_LEFT;

    /**
     * The image representing the bullet facing right
     */
    private final Image BULLET_IMAGE_RIGHT;

    /**
     * The current image of the bullet
     */
    private final Image currentImage;

    /**
     * The speed of the bullet
     */
    private static final double BULLET_SPEED = Physics.BULLET_SPEED;

    /**
     * The maximum travel distance of the bullet
     */
    private static final double MAX_TRAVEL_DISTANCE = Physics.BULLET_MAX_TRAVEL_DISTANCE;
    
    /**
     * Constructs a new bullet at Mario's position traveling in the direction Mario is facing.
     *
     * @param startX       The x-coordinate where the bullet starts.
     * @param startY       The y-coordinate where the bullet starts.
     * @param isFacingRight Whether the bullet should travel right (true) or left (false).
     */
    public Bullet(double startX, double startY, boolean isFacingRight) {
        super(startX, startY, isFacingRight);
        this.BULLET_IMAGE_LEFT = new Image("res/bullet_left.png");
        this.BULLET_IMAGE_RIGHT = new Image("res/bullet_right.png");
        // Set initial image based on direction
        this.currentImage = isFacingRight ? BULLET_IMAGE_RIGHT : BULLET_IMAGE_LEFT;
    }
    
    /**
     * Updates the bullet's position and checks if it has traveled its maximum distance.
     *
     * @return {@code true} if the bullet should still be active, {@code false} if it should be removed.
     */
    @Override
    public boolean update() {
        if (!this.isActive()) {
            return false;
        }
        
        // Move bullet in the appropriate direction
        if (this.isFacingRight()) {
            this.setX(getX() + BULLET_SPEED);
        } else {
            this.setX(getX() - BULLET_SPEED);
        }
        this.setDistanceTravelled(getDistanceTravelled() + BULLET_SPEED);
        
        // Check if bullet has reached its maximum travel distance
        if (this.getDistanceTravelled() >= MAX_TRAVEL_DISTANCE) {
            this.deactivate();
            return false;
        }
        
        // Check if bullet is off-screen
        if (getX() < 0 || getX() > bagel.Window.getWidth()) {
            this.deactivate();
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing the bullet's collision area.
     */
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (currentImage.getWidth() / 2),
                getY() - (currentImage.getHeight() / 2),
                currentImage.getWidth(),
                currentImage.getHeight()
        );
    }

    /**
     * Draws the bullet on the screen.
     */
    @Override
    public void draw() {
        if (this.isActive()) {
            currentImage.draw(getX(), getY());
        }
    }
}