package objects;
import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents a ladder in the game.
 * The ladder falls under gravity until it lands on a platform.
 * 
 * @author Minh Triet Pham
 */
public class Ladder extends StaticOnPlatformEntity{
    /**
     * The image representing the ladder
     */
    private final Image LADDER_IMAGE;

    /**
     * The width of the ladder
     */
    private static double width;

    /**
     * The height of the ladder
     */
    private static double height;

    /**
     * Constructs a ladder at the specified position.
     *
     * @param startX The initial x-coordinate.
     * @param startY The initial y-coordinate.
     */
    public Ladder(double startX, double startY) {
        super(startX, startY);
        this.LADDER_IMAGE = new Image("res/ladder.png");
        width = LADDER_IMAGE.getWidth();
        height = LADDER_IMAGE.getHeight();
    }

    /**
     * Draws the ladder on the screen.
     */
    @Override
    public void draw() {
        LADDER_IMAGE.draw(getX(), getY());
    }

    /**
     * Updates the ladder's position by applying gravity and checking for platform collisions.
     * If a collision is detected, the ladder stops falling and rests on the platform.
     *
     * @param platforms An array of platforms in the game.
     */
    @Override
    public void update(Platform[] platforms) {
        // 1) Apply gravity
        this.setVelocityY(getVelocityY() + Physics.LADDER_GRAVITY);

        // 2) Limit falling speed to terminal velocity
        if (this.getVelocityY() > Physics.LADDER_TERMINAL_VELOCITY) {
            this.setVelocityY(Physics.LADDER_TERMINAL_VELOCITY);
        }

        // 3) Move the ladder downward
        this.setY(getY() + getVelocityY());

        // 4) Check for collision with platforms
        for (Platform platform : platforms) {
            if (getBoundingBox().intersects(platform.getBoundingBox())) {
                // Position the ladder on top of the platform
                double newY = platform.getY()
                        - (platform.getHeight() / 2)  // Platform top edge
                        - (this.getHeight() / 2);     // Ladder height offset
                this.setY(newY);

                this.setVelocityY(0); // Stop falling
                break; // Stop checking further once the ladder lands
            }
        }

        // 5) Draw the ladder after updating position
        draw();
    }

    /**
     * Returns the bounding box of the ladder for collision detection.
     *
     * @return A {@link Rectangle} representing the ladder's bounding box.
     */
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (LADDER_IMAGE.getWidth() / 2),
                getY() - (LADDER_IMAGE.getHeight() / 2),
                LADDER_IMAGE.getWidth(),
                LADDER_IMAGE.getHeight()
        );
    }

    /**
     * Gets the width of the ladder.
     *
     * @return The width of the ladder.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the ladder.
     *
     * @return The height of the ladder.
     */
    public double getHeight() {
        return height;
    }
}
