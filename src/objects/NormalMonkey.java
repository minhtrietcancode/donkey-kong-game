package objects;
import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents a Normal Monkey enemy in the game.
 * Normal monkeys walk on platforms following predefined paths and are affected by gravity.
 * They can lead to Mario's death if Mario is hit by them without a hammer 
 * 
 * @author Minh Triet Pham
 */
public class NormalMonkey extends Entity{
    /**
     * The image representing the monkey facing left
     */
    private Image MONKEY_LEFT_IMAGE;

    /**
     * The image representing the monkey facing right
     */
    private Image MONKEY_RIGHT_IMAGE;

    /**
     * The current image of the monkey
     */
    private Image currentImage;

    /**
     * The initial velocity of the monkey
     */
    private double velocityY = 0;

    /**
     * Signal of whether the monkey is facing right
     */
    private boolean isFacingRight;

    /**
     * The speed of the monkey
     */
    private final double MOVE_SPEED = 0.5;

    /**
     * The predefined list of route distances of the monkey
     */
    private final int[] routeDistances;

    /**
     * The current route index of the monkey
     */
    private int currentRouteIndex = 0;

    /**
     * The distance moved by the monkey - to check if the monkey has completed 
     * the current route segment or not
     */
    private int distanceMoved = 0;

    /**
     * Signal of whether the monkey is still in the game
     */
    private boolean isAlive = true;

    /**
     * The array of platforms in the game - to check if the monkey is touching a platform
     */
    private Platform[] platforms; 
    
    /**
     * Signal of whether the monkey has changed direction in a single frame
     */
    private boolean hasChangedDirection = false;

    /**
     * Creates a new Normal Monkey with specified starting position, direction, and movement route.
     *
     * @param startX       The initial x-coordinate
     * @param startY       The initial y-coordinate
     * @param facingRight  Whether the monkey initially faces right (true) or left (false)
     * @param routeArray   Array of distances for the monkey's movement pattern
     * @param platforms    Array of platforms in the game
     */
    public NormalMonkey(double startX, double startY, boolean facingRight, int[] routeArray, Platform[] platforms) {
        super(startX, startY);
        this.isFacingRight = facingRight;
        this.routeDistances = routeArray;

        // Load monkey images
        MONKEY_LEFT_IMAGE = new Image("res/normal_monkey_left.png");
        MONKEY_RIGHT_IMAGE = new Image("res/normal_monkey_right.png");
        
        // Set initial image based on direction
        currentImage = isFacingRight ? MONKEY_RIGHT_IMAGE : MONKEY_LEFT_IMAGE;

        // Initialize platforms array
        this.platforms = platforms;
    }

    /**
     *  Update the monkey state each frame
     */ 
    public void update() {
        // If the monkey is not alive, do not update
        if (!isAlive) {
            return;
        }
        
        // Reset direction change flag at the start of each update
        hasChangedDirection = false;

        // Apply gravity to the monkey
        applyGravity();

        // Handle collisions between the monkey and platforms
        handlePlatformCollisions(platforms);

        // Move the monkey along its predefined route
        moveAlongRoute();
        
        // Update the image based on direction
        currentImage = isFacingRight ? MONKEY_RIGHT_IMAGE : MONKEY_LEFT_IMAGE;

        // Draw the monkey on screen
        draw();
    }

    /**
     * Applies gravity to the monkey, increasing its downward velocity.
     */
    private void applyGravity() {
        // Apply gravity to the monkey
        velocityY += Physics.MONKEY_GRAVITY;

        // If the velocity is too high, set it to the terminal velocity
        if (velocityY > Physics.MONKEY_TERMINAL_VELOCITY) {
            velocityY = Physics.MONKEY_TERMINAL_VELOCITY;
        }

        // Update the monkey's position
        this.setY(getY() + velocityY);
    }

    /**
     * Handles collisions between the monkey and platforms.
     * If the monkey lands on a platform, position is adjusted and velocity is reset.
     *
     * @param platforms Array of platforms in the game
     */
    private void handlePlatformCollisions(Platform[] platforms) {
        // Check if the monkey is touching a platform
        for (Platform platform : platforms) {
            if (isTouchingPlatform(platform)) {
                // Position monkey on top of the platform that it is touching
                double newY = platform.getY() - (platform.getHeight() / 2) - (currentImage.getHeight() / 2);
                this.setY(newY);    

                // Stop falling
                velocityY = 0;

                // Break out of the loop once found a platform monkey is touching
                break;
            }
        }
    }

    /**
     * Gets the current platform the monkey is standing on
     * 
     * @return The platform the monkey is on, or null if not on any platform
     */
    private Platform getCurrentPlatform() {
        // Check if the monkey is touching a platform
        for (Platform platform : platforms) {
            if (isTouchingPlatform(platform)) {
                return platform;
            }
        }

        // Return null if the monkey is not touching any platform
        return null;
    }

    /**
     * Moves the monkey according to its predefined route pattern.
     * The monkey moves a certain distance in one direction, then reverses and moves
     * the next distance in the sequence.
     */
    private void moveAlongRoute() {
        // Skip if no route or inactive
        if (routeDistances == null || routeDistances.length == 0) {
            return;
        }
        
        // Get current platform that the monkey is on
        Platform currentPlatform = getCurrentPlatform();
        if (currentPlatform == null) {
            return; // Wait until we land on a platform
        }
        
        // Get current target distance from route
        int targetDistance = routeDistances[currentRouteIndex];
        
        // Calculate boundaries for collision detection
        double monkeyHalfWidth = currentImage.getWidth() / 2;
        double screenWidth = Window.getWidth(); 
        double platformLeftEdge = currentPlatform.getX() - (currentPlatform.getWidth() / 2);
        double platformRightEdge = currentPlatform.getX() + (currentPlatform.getWidth() / 2);
        
        // Calculate next position for the monkey
        double nextX = getX() + (isFacingRight ? MOVE_SPEED : -MOVE_SPEED);
        
        // Check if the monkey would hit the screen boundary
        if (nextX - monkeyHalfWidth < 0 || nextX + monkeyHalfWidth > screenWidth) {
            // Change direction due to screen boundary
            reverseDirection();
            return;
        }
        
        // Check if the monkey would fall off the platform
        if ((isFacingRight && nextX + monkeyHalfWidth > platformRightEdge) ||
            (!isFacingRight && nextX - monkeyHalfWidth < platformLeftEdge)) {
            // Change direction due to platform edge
            reverseDirection();
            return;
        }
        
        // Check if the monkey has completed the current route segment
        if (distanceMoved >= targetDistance) {
            // Change direction due to completing route segment
            reverseDirection();
            return;
        }
        
        // Safe to move - update position and distance counter
        this.setX(nextX);
        distanceMoved++;
    }
    
    /**
     * Helper method to reverse direction and update route tracking
     */
    private void reverseDirection() {
        if (!hasChangedDirection) {
            // Reset the distance moved
            distanceMoved = 0;

            // Update the current route index
            currentRouteIndex = (currentRouteIndex + 1) % routeDistances.length;

            // Update the direction of the monkey
            isFacingRight = !isFacingRight;

            // Set the flag to true to indicate that the direction has changed
            hasChangedDirection = true;
        }
    }

    /**
     * Checks if the monkey is colliding with a platform.
     *
     * @param platform The platform to check
     * @return True if touching, false otherwise
     */
    private boolean isTouchingPlatform(Platform platform) {
        // Get monkey's bounding box
        Rectangle monkeyBox = getBoundingBox();

        // Get platform's bounding box
        Rectangle platformBox = platform.getBoundingBox();

        // First, check if the monkey is directly above the platform
        boolean isAbovePlatform = (monkeyBox.left() < platformBox.right() &&
                monkeyBox.right() > platformBox.left());

        // Check if the bottom of the monkey is touching or slightly above/below the top of platform
        double monkeyBottom = monkeyBox.bottom();
        double platformTop = platformBox.top();

        // Add some tolerance for detection as when the monkey is on the platform,
        // the bottom of the monkey is exactly touching the top of the platform, which 
        // would be detected as not touching
        double tolerance = 5.0;

        // Check if the monkey is on the platform
        boolean isOnPlatform = isAbovePlatform &&
                (Math.abs(monkeyBottom - platformTop) <= tolerance);

        // Return true if the monkey is on the platform, false otherwise
        return isOnPlatform;
    }

    /**
     * Gets the monkey's bounding box for collision detection.
     *
     * @return A Rectangle representing the monkey's collision area
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (currentImage.getWidth() / 2),
                getY() - (currentImage.getHeight() / 2),
                currentImage.getWidth(),
                currentImage.getHeight()
        );
    }

    /**
     * Draws the monkey on screen.
     */
    @Override
    public void draw() {
        currentImage.draw(getX(), getY());
    }

    /**
     * Checks if the monkey is facing right.
     *
     * @return True if facing right, false if facing left
     */
    public boolean isFacingRight() {
        return isFacingRight;
    }

    /**
     * Handles the monkey being hit by a bullet
     */
    public void hit() {
        isAlive = false;
    }
    
    /**
     * Checks if the monkey is still alive
     * 
     * @return True if the monkey is alive, false otherwise
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Setter for MONKEY_LEFT_IMAGE
     * 
     * @param MONKEY_LEFT_IMAGE The image representing the monkey facing left
     */
    public void setMONKEY_LEFT_IMAGE(Image MONKEY_LEFT_IMAGE) {
        this.MONKEY_LEFT_IMAGE = MONKEY_LEFT_IMAGE;
    }

    /**
     * Setter for MONKEY_RIGHT_IMAGE
     * 
     * @param MONKEY_RIGHT_IMAGE The image representing the monkey facing right
     */
    public void setMONKEY_RIGHT_IMAGE(Image MONKEY_RIGHT_IMAGE) {
        this.MONKEY_RIGHT_IMAGE = MONKEY_RIGHT_IMAGE;
    }
}