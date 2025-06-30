package objects;
import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents a barrel in the game, affected by gravity and platform collisions.
 * The barrel can be destroyed, at which point it will no longer be drawn or interact with the environment.
 * 
 * @author Minh Triet Pham
 */
public class Barrel extends StaticOnPlatformEntity{
    /**
     * The image representing the barrel
     */
    private final Image BARREL_IMAGE;

    /**
     * Signal of whether the barrel has been destroyed or not, initialized to be
     * false as the barrel is not destroyed at the start of the game
     */
    private boolean isDestroyed = false;

    /**
     * Constructs a new Barrel at the specified starting position.
     *
     * @param startX The initial x-coordinate of the barrel.
     * @param startY The initial y-coordinate of the barrel.
     */
    public Barrel(double startX, double startY) {
        super(startX, startY);
        this.BARREL_IMAGE = new Image("res/barrel.png"); // Load barrel sprite
    }

    /**
     * Updates the barrel's position, applies gravity, checks for platform collisions,
     * and renders the barrel if it is not destroyed.
     *
     * @param platforms An array of platforms for collision detection.
     */
    @Override
    public void update(Platform[] platforms) {
        if (!isDestroyed) {
            // 1) Apply gravity
            this.setVelocityY(getVelocityY() + Physics.BARREL_GRAVITY);
            if (this.getVelocityY() > Physics.BARREL_TERMINAL_VELOCITY) {
                this.setVelocityY(Physics.BARREL_TERMINAL_VELOCITY);
            }
            this.setY(getY() + getVelocityY());

            // 2) Check for platform collisions
            for (Platform platform : platforms) {
                if (this.getBoundingBox().intersects(platform.getBoundingBox())) {
                    // Position the barrel on top of the platform
                    double newY = platform.getY() - (platform.getHeight() / 2) - (BARREL_IMAGE.getHeight() / 2);
                    this.setY(newY);
                    this.setVelocityY(0); // Stop falling
                    break;
                }
            }

            // 3) Draw the barrel
            draw();
        }
    }

    /**
     * Draws the barrel on the screen if it is not destroyed.
     */
    @Override
    public void draw() {
        if (!isDestroyed) {
            BARREL_IMAGE.draw(getX(), getY());
        }
    }

    /**
     * Creates and returns the barrel's bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing the barrel's bounding box.
     *         If the barrel is destroyed, returns an off-screen bounding box.
     */
    @Override
    public Rectangle getBoundingBox() {
        if (isDestroyed) {
            return new Rectangle(-1000, -1000, 0, 0); // Off-screen if destroyed
        }
        return new Rectangle(
                getX() - (BARREL_IMAGE.getWidth() / 2),
                getY() - (BARREL_IMAGE.getHeight() / 2),
                BARREL_IMAGE.getWidth(),
                BARREL_IMAGE.getHeight()
        );
    }

    /**
     * Marks the barrel as destroyed, preventing it from being drawn or updated.
     */
    public void destroy() {
        isDestroyed = true;
    }

    /**
     * Checks if the barrel has been destroyed.
     *
     * @return {@code true} if the barrel is destroyed, {@code false} otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Retrieves the barrel's image.
     *
     * @return An {@link Image} representing the barrel.
     */
    public Image getBarrelImage() {
        return this.BARREL_IMAGE;
    }
}
