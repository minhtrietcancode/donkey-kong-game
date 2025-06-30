package objects;
/**
 * The Physics class contains constants and methods related to the physics mechanics of the game.
 * It defines values for gravity, terminal velocity, and other movement constraints
 * to ensure realistic and smooth character motion.
 */
public class Physics {

    /**
     * The gravitational acceleration applied to Mario.
     */
    public static final double MARIO_GRAVITY = 0.2;

    /**
     * The gravitational acceleration applied to Donkey Kong.
     */
    public static final double DONKEY_GRAVITY = 0.4;

    /**
     * The gravitational acceleration applied while climbing ladders.
     */
    public static final double LADDER_GRAVITY = 0.25;

    /**
     * The gravitational acceleration applied to barrels.
     */
    public static final double BARREL_GRAVITY = 0.4;

    /**
     * The maximum falling speed (terminal velocity) that Mario can reach.
     */
    public static final double MARIO_TERMINAL_VELOCITY = 10.0;

    /**
     * The maximum falling speed (terminal velocity) for barrels.
     */
    public static final double BARREL_TERMINAL_VELOCITY = 5.0;

    /**
     * The maximum falling speed (terminal velocity) while climbing ladders.
     */
    public static final double LADDER_TERMINAL_VELOCITY = 5.0;

    /**
     * The maximum falling speed (terminal velocity) for Donkey Kong.
     */
    public static final double DONKEY_TERMINAL_VELOCITY = 5.0;

    /**
     * The gravitational acceleration applied to the monkey.
     */
    public static final double MONKEY_GRAVITY = 0.4;

    /**
     * The maximum falling speed (terminal velocity) for the monkey.
     */
    public static final double MONKEY_TERMINAL_VELOCITY = 5.0;
    
    /**
     * The strength of Mario's jump
     */
    public static final double MARIO_JUMP_STRENGTH = -5.0;

    /**
     * The speed of Mario's movement
     */
    public static final double MARIO_MOVE_SPEED = 3.5;

    /**
     * The speed of Mario's climb
     */
    public static final double MARIO_CLIMB_SPEED = 2.0;
    
    /**
     * The speed of the banana
     */
    public static final double BANANA_MOVE_SPEED = 1.8;

    /**
     * The maximum distance the banana can travel
     */
    public static final int BANANA_MAX_DISTANCE = 300;

    /**
     * The interval between banana shots
     */
    public static final int BANANA_SHOOT_INTERVAL = 300;

    /**
     * The speed of the bullet
     */
    public static final double BULLET_SPEED = 3.8;

    /**
     * The maximum distance the bullet can travel
     */
    public static final double BULLET_MAX_TRAVEL_DISTANCE = 300.0;
    
    /**
     * The initial health of Donkey Kong
     */
    public static final int DONKEY_INITIAL_HEALTH = 5;

    /**
     * The initial number of bullets a blaster has
     */
    public static final int BLASTER_INITIAL_BULLETS = 5;
}