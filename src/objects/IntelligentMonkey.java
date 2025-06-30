package objects;
import bagel.*;

/**
 * Represents an Intelligent Monkey enemy in the game.
 * Intelligent monkeys walk on platforms following predefined paths.
 * Banana throwing is now handled by the Level2Screen class.
 * 
 * @author Minh Triet Pham
 */
public class IntelligentMonkey extends NormalMonkey {
    /**
     * The image representing the intelligent monkey facing left
     */
    private final Image INTELLIGENT_MONKEY_LEFT_IMAGE;

    /**
     * The image representing the intelligent monkey facing right
     */
    private final Image INTELLIGENT_MONKEY_RIGHT_IMAGE;

    /**
     * Creates a new Intelligent Monkey with specified starting position, direction, and movement route.
     *
     * @param startX       The initial x-coordinate
     * @param startY       The initial y-coordinate
     * @param facingRight  Whether the monkey initially faces right (true) or left (false)
     * @param routeArray   Array of distances for the monkey's movement pattern
     * @param platforms    Array of platforms in the game
     */
    public IntelligentMonkey(double startX, double startY, boolean facingRight, int[] routeArray, Platform[] platforms) {
        super(startX, startY, facingRight, routeArray, platforms);
        
        // Override the normal monkey images with intelligent monkey images
        INTELLIGENT_MONKEY_LEFT_IMAGE = new Image("res/intelli_monkey_left.png");
        INTELLIGENT_MONKEY_RIGHT_IMAGE = new Image("res/intelli_monkey_right.png");
        
        // Replace inherited image variables with intelligent monkey images
        this.setMONKEY_LEFT_IMAGE(INTELLIGENT_MONKEY_LEFT_IMAGE);
        this.setMONKEY_RIGHT_IMAGE(INTELLIGENT_MONKEY_RIGHT_IMAGE);
    }

    /**
     * Updates the intelligent monkey's state for the current frame.
     * Banana shooting is now handled by Level2Screen.
     */
    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
        
        // Call the parent class update method to handle movement and platform collision
        super.update();
    }
}