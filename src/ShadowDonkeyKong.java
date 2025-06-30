import bagel.*;
import java.util.Properties;
import screens.HomeScreen;
import screens.Level1Screen;
import screens.Level2Screen;
import screens.GameEndScreen;

/**
 * The main class for the Shadow Donkey Kong game.
 * This class extends {@code AbstractGame} and is responsible for managing game initialization,
 * updates, rendering, and handling user input.
 *
 * It sets up the game world, initializes characters, platforms, ladders, and other game objects,
 * and runs the game loop to ensure smooth gameplay.
 */
public class ShadowDonkeyKong extends AbstractGame {

    /**
     * The game configuration loaded from external files
     */
    private final Properties GAME_PROPS;

    /**
     * The message properties loaded from external files
     */
    private final Properties MESSAGE_PROPS;

    /**
     * The home screen object
     */
    private HomeScreen homeScreen;

    /**
     * The level 1 screen object
     */
    private Level1Screen level1Screen;

    /**
     * The level 2 screen object
     */
    private Level2Screen level2Screen;

    /**
     * The game end screen object
     */
    private GameEndScreen gameEndScreen;

    /**
     * The current level
     * 0: Home
     * 1: Level 1
     * 2: Level 2
     * 3: Game End
     */
    private int currentLevel = 0;

    /**
     * The constant represent enum for home screen state
     */
    public static final int HOME = 0;

    /**
     * The constant represent enum for level 1 screen state
     */
    public static final int LEVEL_1 = 1;

    /**
     * The constant represent enum for level 2 screen state
     */
    public static final int LEVEL_2 = 2;

    /**
     * The constant represent enum for game end screen state
     */
    public static final int GAME_END = 3;

    /**
     * The constant represent bonus score for each time remaining left in seconds
     */
    public static final int TIME_LEFT_BONUS_SCORE = 3;

    /**
     * The static screen width
     */
    public static double screenWidth;

    /**
     * The static screen height
     */
    public static double screenHeight;

    /**
     * Constructor for the Shadow Donkey Kong game.
     * Initializes the game window, loads properties, and sets up the home screen.
     *
     * @param gameProps Properties containing game configuration settings
     * @param messageProps Properties containing text messages for display
     */
    public ShadowDonkeyKong(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        ShadowDonkeyKong.screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        ShadowDonkeyKong.screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        // Initialize the home screen
        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
    }

    /**
     * Main update method called each frame to process game logic and input.
     * Handles screen transitions and delegates updates to the current active screen.
     *
     * @param input The Input object containing user input information
     */
    @Override
    protected void update(Input input) {
        // Exit game when ESC key is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // Home Screen
        if (currentLevel == HOME) {
            if (input.wasPressed(Keys.ENTER)) {
                // Start Level 1 when ENTER is pressed
                level1Screen = new Level1Screen(GAME_PROPS);
                currentLevel = LEVEL_1;
            } else if (input.wasPressed(Keys.NUM_2)) {
                // Skip to Level 2 when 2 is pressed
                level2Screen = new Level2Screen(GAME_PROPS);
                currentLevel = LEVEL_2;
            } else {
                homeScreen.update(input);
            }
        }
        // Level 1 Gameplay Screen
        else if (currentLevel == LEVEL_1) {
            if (level1Screen.update(input)) {
                // Check if Level 1 is completed (won)
                if (level1Screen.isLevelCompleted()) {
                    // Calculate time bonus: 3 points per second remaining
                    int timeLeft = level1Screen.getSecondsLeft();
                    int timeBonus = timeLeft * TIME_LEFT_BONUS_SCORE;
                    int totalScore = level1Screen.getScore() + timeBonus;

                    // Create Level 2 and pass the updated score from Level 1 (including time bonus)
                    level2Screen = new Level2Screen(GAME_PROPS);
                    level2Screen.setScore(totalScore);
                    currentLevel = LEVEL_2;
                } else if (level1Screen.isTimeOut()) {
                    // Game over due to timeout - keep the current score
                    endLevel(false, level1Screen.getScore(), 0);
                } else {
                    // Game over in Level 1 due to death - set score to 0
                    endLevel(false, 0, 0);
                }
            }
        }
        // Level 2 Gameplay Screen
        else if (currentLevel == LEVEL_2) {
            if (level2Screen.update(input)) {
                // For Level 2, check if the game is over due to death
                if (level2Screen.isGameOver()) {
                    if (level2Screen.isTimeOut()) {
                        // Game over due to timeout - keep the current score
                        endLevel(false, level2Screen.getScore(), 0);
                    } else {
                        // Game over in Level 2 due to death - set score to 0
                        endLevel(false, 0, 0);
                    }
                } else {
                    // Level 2 completed (won)
                    endLevel(true, level2Screen.getScore(), level2Screen.getSecondsLeft());
                }
            }
        }
        // Game Over / Victory Screen
        else {
            if (gameEndScreen.update(input)) {
                // Clean up resources and return to home screen
                level1Screen = null;
                level2Screen = null;
                gameEndScreen = null;
                currentLevel = HOME; // Back to home screen
            }
        }
    }

    /**
     * Transitions the game to the end screen with relevant game statistics.
     * Cleans up level resources and sets up the game end screen.
     *
     * @param isWon Whether the player won the game
     * @param finalScore The final score achieved by the player
     * @param timeRemaining Remaining time when the level was completed
     */
    private void endLevel(boolean isWon, int finalScore, int timeRemaining) {
        gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);
        gameEndScreen.setIsWon(isWon);
        gameEndScreen.setFinalScore(timeRemaining, finalScore);
        level1Screen = null;
        level2Screen = null;
        currentLevel = GAME_END; // Game end screen
    }

    /**
     * Getter for the static screen width.
     *
     * @return The width of the game window
     */
    public static double getScreenWidth() {
        return screenWidth;
    }

    /**
     * Getter for the static screen height.
     *
     * @return The height of the game window
     */
    public static double getScreenHeight() {
        return screenHeight;
    }

    /**
     * Entry point for the game application.
     * Loads property files and starts the game.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDonkeyKong game = new ShadowDonkeyKong(gameProps, messageProps);
        game.run();
    }
}