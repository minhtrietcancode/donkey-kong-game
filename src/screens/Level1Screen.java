package screens;
import bagel.*;
import java.util.Properties;
import objects.Mario;
import objects.Barrel;
import objects.Ladder;
import objects.Platform;
import objects.Hammer;
import objects.Donkey;

/**
 * Represents the main gameplay screen where the player controls Mario.
 * This class manages game objects, updates their states, and handles game logic.
 * 
 * @author Minh Triet Pham
 */
public class Level1Screen extends AbstractScreen {
    /**
     * The properties file containing game settings
     */
    private final Properties GAME_PROPS;

    /**
     * The Mario object
     */
    private Mario mario;

    /**
     * The array of barrels in the game
     */
    private Barrel[] barrels;   

    /**
     * The array of ladders in the game
     */
    private Ladder[] ladders;   

    /**
     * The hammer object
     */
    private Hammer hammer;      

    /**
     * The Donkey object
     */
    private Donkey donkey;      

    /**
     * The array of platforms in the game
     */
    private Platform[] platforms; 

    /**
     * The current frame number
     */
    private int currFrame = 0; 

    /**
     * The maximum number of frames before game ends
     */
    private final int MAX_FRAMES; 

    /**
     * The font used for displaying the score
     */
    private final Font STATUS_FONT;

    /**
     * The x coordinate of the score
     */
    private final int SCORE_X;

    /**
     * The y coordinate of the score
     */
    private final int SCORE_Y;

    /**
     * The message displayed for the score
     */
    private static final String SCORE_MESSAGE = "SCORE ";

    /**
     * The message displayed for the time
     */
    private static final String TIME_MESSAGE = "Time Left ";

    /**
     * The score for destroying a barrel with hammer
     */
    private static final int BARREL_SCORE = 100;        

    /**
     * The vertical offset for time display
     */
    private static final int TIME_DISPLAY_DIFF_Y = 30; 
    
    /**
     * The score for jumping over a barrel
     */
    private static final int BARREL_CROSS_SCORE = 30;   

    /**
     * The player's current score
     */
    private int score = 0;      
    
    /**
     * Indicates if the game is over
     */
    private boolean isGameOver = false;                

    /**
     * The message displayed for Donkey Kong's health
     */ 
    private static final String DONKEY_HEALTH_MESSAGE = "DONKEY HEALTH 5";

    /**
     * The x coordinate of the Donkey Kong's health
     */
    private final int DONKEY_HEALTH_X;

    /**
     * The y coordinate of the Donkey Kong's health
     */
    private final int DONKEY_HEALTH_Y;

    /**
     * Returns the player's current score.
     *
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Calculates the remaining time left in seconds.
     *
     * @return The number of seconds remaining before the game ends.
     */
    public int getSecondsLeft() {
        return (MAX_FRAMES - currFrame) / 60;
    }

    /**
     * Constructs the gameplay screen, loading resources and initializing game objects.
     *
     * @param gameProps  Properties file containing game settings.
     */
    public Level1Screen(Properties gameProps) {
        super(gameProps);
        this.GAME_PROPS = gameProps;

        // Load game parameters
        this.MAX_FRAMES = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
        this.STATUS_FONT = new Font(
                gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gamePlay.score.fontSize"))
        );
        this.SCORE_X = Integer.parseInt(gameProps.getProperty("gamePlay.score.x"));
        this.SCORE_Y = Integer.parseInt(gameProps.getProperty("gamePlay.score.y"));

        // Load Donkey health display coordinates from properties file
        String[] healthCoords = gameProps.getProperty("gamePlay.donkeyhealth.coords").split(",");
        this.DONKEY_HEALTH_Y = Integer.parseInt(healthCoords[0]);
        this.DONKEY_HEALTH_X = Integer.parseInt(healthCoords[1]);

        // Initialize game objects
        initializeGameObjects();
    }

    /**
     * Initializes game objects such as Mario, Donkey Kong, barrels, ladders, platforms, and the hammer.
     * Objects are created based on coordinates and counts specified in the properties file.
     */
    private void initializeGameObjects() {
        // Initialize Mario at Level 1 starting position
        String[] marioCoords = GAME_PROPS.getProperty("mario.level1").split(",");
        double marioX = Double.parseDouble(marioCoords[0]);
        double marioY = Double.parseDouble(marioCoords[1]);
        this.mario = new Mario(marioX, marioY);

        // Initialize Donkey Kong at Level 1 position
        String[] donkeyCoords = GAME_PROPS.getProperty("donkey.level1").split(",");
        double donkeyX = Double.parseDouble(donkeyCoords[0]);
        double donkeyY = Double.parseDouble(donkeyCoords[1]);
        this.donkey = new Donkey(donkeyX, donkeyY);

        // Initialize barrels with positions from properties file
        int barrelCount = Integer.parseInt(GAME_PROPS.getProperty("barrel.level1.count"));
        this.barrels = new Barrel[barrelCount];
        for (int i = 1; i <= barrelCount; i++) {
            String barrelData = GAME_PROPS.getProperty("barrel.level1." + i);
            if (barrelData != null) {
                String[] coords = barrelData.split(",");
                double barrelX = Double.parseDouble(coords[0]);
                double barrelY = Double.parseDouble(coords[1]);
                barrels[i-1] = new Barrel(barrelX, barrelY);
            }
        }

        // Initialize ladders with positions from properties file
        int ladderCount = Integer.parseInt(GAME_PROPS.getProperty("ladder.level1.count"));
        this.ladders = new Ladder[ladderCount];
        for (int i = 1; i <= ladderCount; i++) {
            String ladderData = GAME_PROPS.getProperty("ladder.level1." + i);
            if (ladderData != null) {
                String[] coords = ladderData.split(",");
                double ladderX = Double.parseDouble(coords[0]);
                double ladderY = Double.parseDouble(coords[1]);
                ladders[i-1] = new Ladder(ladderX, ladderY);
            }
        }

        // Initialize platforms using semicolon-separated coordinates
        String platformData = GAME_PROPS.getProperty("platforms.level1");
        if (platformData != null && !platformData.isEmpty()) {
            String[] platformEntries = platformData.split(";");
            this.platforms = new Platform[platformEntries.length];
            for (int i = 0; i < platformEntries.length; i++) {
                String[] coords = platformEntries[i].split(",");
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                platforms[i] = new Platform(x, y);
            }
        }

        // Initialize hammer if present in this level
        int hammerCount = Integer.parseInt(GAME_PROPS.getProperty("hammer.level1.count"));
        if (hammerCount > 0) {
            String[] hammerCoords = GAME_PROPS.getProperty("hammer.level1.1").split(",");
            double hammerX = Double.parseDouble(hammerCoords[0]);
            double hammerY = Double.parseDouble(hammerCoords[1]);
            this.hammer = new Hammer(hammerX, hammerY);
        }
    }

    /**
     * Updates game state each frame, handling object interactions, collision detection,
     * scoring, and win/lose conditions.
     *
     * @param input The current player input.
     * @return {@code true} if the game ends, {@code false} otherwise.
     */
    @Override
    public boolean update(Input input) {
        currFrame++;

        // Draw background
        drawBackground();

        // Draw all platforms
        for (Platform platform : platforms) {
            if (platform != null) {
                platform.draw();
            }
        }

        // Update ladders with platform collision detection
        for (Ladder ladder : ladders) {
            if (ladder != null) {
                ladder.update(platforms);
            }
        }

        // Update barrels and check for collision with Mario
        for (Barrel barrel : barrels) {
            if (barrel == null) continue;

            // Award points if Mario jumps over a barrel
            if (mario.jumpOver(barrel)) {
                score += BARREL_CROSS_SCORE;
            }

            // Handle barrel collision with Mario
            if (!barrel.isDestroyed() && mario.isTouchingBarrel(barrel)) {
                if (!mario.holdHammer()) {
                    // Mario dies if touching barrel without hammer
                    isGameOver = true;
                } else {
                    // Mario destroys barrel with hammer and gets points
                    barrel.destroy();
                    score += BARREL_SCORE;
                }
            }
            barrel.update(platforms);
        }

        // Check for game timeout condition
        if (checkingGameTime()) {
            isGameOver = true;
        }

        // Update Donkey Kong position and state
        donkey.update(platforms);

        // Draw hammer and Donkey Kong
        hammer.draw();
        donkey.draw();

        // Update Mario with all game object interactions
        mario.update(input, ladders, platforms, hammer, null, donkey, null); // Level 1 has no blasters or monkeys

        // Check win/lose condition: Mario reaches Donkey without hammer = game over
        if (mario.hasReached(donkey) && !mario.holdHammer()) {
            isGameOver = true;
        }

        // Display score, time and other game information
        displayInfo();

        // Return true if game is over (either win or lose condition met)
        return isGameOver || isLevelCompleted();
    }

    /**
     * Displays the player's score, time remaining, and Donkey's health on the screen.
     * Updates every frame to show current game status.
     */
    public void displayInfo() {
        // Display current score
        STATUS_FONT.drawString(SCORE_MESSAGE + score, SCORE_X, SCORE_Y);

        // Display time remaining in seconds
        int secondsLeft = (MAX_FRAMES - currFrame) / 60;
        int TIME_X = SCORE_X;
        int TIME_Y = SCORE_Y + TIME_DISPLAY_DIFF_Y;
        STATUS_FONT.drawString(TIME_MESSAGE + secondsLeft, TIME_X, TIME_Y);

        // Display Donkey Kong's health
        STATUS_FONT.drawString(DONKEY_HEALTH_MESSAGE, DONKEY_HEALTH_X, DONKEY_HEALTH_Y);
    }

    /**
     * Checks whether the level is completed by determining if Mario has reached Donkey Kong
     * while holding a hammer. This serves as the game's winning condition.
     *
     * @return {@code true} if Mario reaches Donkey Kong while holding a hammer,
     *         indicating the level is completed; {@code false} otherwise.
     */
    public boolean isLevelCompleted() {
        return mario.hasReached(donkey) && mario.holdHammer();
    }

    /**
     * Checks if the game has reached its time limit by comparing the current frame count
     * against the maximum allowed frames. If the limit is reached, the game may trigger
     * a timeout condition.
     *
     * @return {@code true} if the current frame count has reached or exceeded
     *         the maximum allowed frames, indicating the time limit has been reached;
     *         {@code false} otherwise.
     */
    public boolean checkingGameTime() {
        return currFrame >= MAX_FRAMES;
    }

    /**
     * Checks if the game over is due to timeout rather than player death.
     * This allows for displaying different game over messages.
     *
     * @return {@code true} if game over is caused by timeout, {@code false} otherwise.
     */
    public boolean isTimeOut() {
        return checkingGameTime();
    }
}