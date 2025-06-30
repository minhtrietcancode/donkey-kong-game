package objects;
import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents Donkey Kong in the game, affected by gravity and platform collisions.
 * The Donkey object moves downward due to gravity and lands on platforms when applicable.
 * 
 * @author Minh Triet Pham
 */
public class Donkey extends StaticOnPlatformEntity{
    /**
     * The image representing Donkey Kong
     */
    private final Image DONKEY_IMAGE;

    /**
     * The initial health of Donkey Kong
     */
    private static final int INITIAL_HEALTH = Physics.DONKEY_INITIAL_HEALTH;

    /**
     * The current health of Donkey Kong
     */
    private int health;

    /**
     * Constructs a new Donkey at the specified starting position.
     *
     * @param startX The initial x-coordinate of Donkey.
     * @param startY The initial y-coordinate of Donkey.
     */
    public Donkey(double startX, double startY) {
        super(startX, startY);
        this.DONKEY_IMAGE = new Image("res/donkey_kong.png"); // Load Donkey Kong sprite
        this.health = INITIAL_HEALTH;
    }

    /**
     * Decreases Donkey Kong's health by the specified amount.
     * 
     * @param damage Amount of health to decrease
     */
    public void decreaseHealth(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Gets current health of Donkey Kong.
     * 
     * @return Current health value
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Checks if Donkey Kong is still alive.
     * 
     * @return true if health > 0, false otherwise
     */
    public boolean isAlive() {
        return this.health > 0;
    }

    /**
     * Updates Donkey's position by applying gravity and checking for platform collisions.
     * If Donkey lands on a platform, the velocity is reset to zero.
     *
     * @param platforms An array of platforms Donkey can land on.
     */
    @Override
    public void update(Platform[] platforms) {
        // Apply gravity
        this.setVelocityY(getVelocityY() + Physics.DONKEY_GRAVITY);
        this.setY(getY() + getVelocityY());
        if (this.getVelocityY() > Physics.DONKEY_TERMINAL_VELOCITY) {
            this.setVelocityY(Physics.DONKEY_TERMINAL_VELOCITY);
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            if (isTouchingPlatform(platform)) {
                // Position Donkey on top of the platform
                double newY = platform.getY() - (platform.getHeight() / 2) - (DONKEY_IMAGE.getHeight() / 2);
                this.setY(newY);
                this.setVelocityY(0); // Stop downward movement
                break;
            }
        }

        // Draw Donkey
        draw();
    }

    /**
     * Checks if Donkey is colliding with a given platform.
     *
     * @param platform The platform to check for collision.
     * @return {@code true} if Donkey is touching the platform, {@code false} otherwise.
     */
    private boolean isTouchingPlatform(Platform platform) {
        Rectangle donkeyBounds = getBoundingBox();
        return donkeyBounds.intersects(platform.getBoundingBox());
    }

    /**
     * Draws Donkey on the screen.
     */
    @Override
    public void draw() {
        DONKEY_IMAGE.draw(getX(), getY());
    }

    /**
     * Returns Donkey's bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing Donkey's bounding box.
     */
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (DONKEY_IMAGE.getWidth() / 2),
                getY() - (DONKEY_IMAGE.getHeight() / 2),
                DONKEY_IMAGE.getWidth(),
                DONKEY_IMAGE.getHeight()
        );
    }

}
