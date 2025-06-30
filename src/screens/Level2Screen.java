package screens;
import bagel.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import objects.Mario;
import objects.Barrel;
import objects.Ladder;
import objects.Platform;
import objects.Hammer;
import objects.Donkey;
import objects.Blaster;
import objects.NormalMonkey;
import objects.IntelligentMonkey;
import objects.Banana;
import objects.Physics;

/**
 * Level2Screen handles the game logic for the second level of the Donkey Kong game.
 * This level includes additional enemies (monkeys), bananas, blasters, and different
 * win conditions compared to Level1.
 * 
 * @author Minh Triet Pham 
 */
public class Level2Screen extends AbstractScreen {
    /**
     * The properties for the game
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
     * The array of blasters in the game
     */
    private Blaster[] blasters;

    /**
     * The array of normal monkeys in the game
     */
    private NormalMonkey[] normalMonkeys;

    /**
     * The array of intelligent monkeys in the game
     */
    private IntelligentMonkey[] intelligentMonkeys;

    /**
     * The list of active bananas in the game - to track the bananas that are currently active
     */
    private List<Banana> activeBananas;

    /**
     * The map of intelligent monkeys and their shooting cooldowns
     */
    private Map<IntelligentMonkey, Integer> monkeyShootCooldowns;

    /**
     * The interval between banana shots
     */
    private static final int BANANA_SHOOT_INTERVAL = Physics.BANANA_SHOOT_INTERVAL; // 5 seconds at 60fps

    /**
     * The current frame - starts at 0, this is used to track the time that has passed
     */
    private int currFrame = 0;

    /**
     * The maximum frames - this is the total number of frames that the game will run for
     */
    private final int MAX_FRAMES;

    /**
     * The font for the status text
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
     * The message for the score
     */
    private static final String SCORE_MESSAGE = "SCORE ";

    /**
     * The message for the time
     */
    private static final String TIME_MESSAGE = "Time Left ";

    /**
     * The message for the Donkey's health
     */
    private static final String DONKEY_HEALTH_MESSAGE = "DONKEY HEALTH ";

    /**
     * The score for destroying a barrel
     */
    private static final int BARREL_SCORE = 100;

    /**
     * The score for destroying a monkey 
     */
    private static final int MONKEY_SCORE = 100 ;

    /**
     * The y coordinate difference for the time display
     */
    private static final int TIME_DISPLAY_DIFF_Y = 30;

    /**
     * The score for jumping over a barrel
     */
    private static final int BARREL_CROSS_SCORE = 30;

    /**
     * The y coordinate difference for the bullet display
     */
    private static final int BULLET_DISPLAY_DIFF_Y = 30;

    /**
     * The current score for the game
     */
    private int score = 0;

    /**
     * Whether the game is over
     */
    private boolean isGameOver = false;

    /**
     * The x coordinate of the Donkey's health message
     */
    private final int DONKEY_HEALTH_X;

    /**
     * The y coordinate of the Donkey's health message
     */
    private final int DONKEY_HEALTH_Y;

    /**
     * The message for the bullet count
     */
    private static final String BULLET_MESSAGE = "BULLET ";

    /**
     * The x coordinate of the bullet count
     */
    private final int BULLET_X;

    /**
     * The y coordinate of the bullet count
     */
    private final int BULLET_Y;

    /**
     * The list of monkeys that have already been counted for score
     */
    private final List<NormalMonkey> monkeyDeathCounted = new ArrayList<>();

    /**
     * Constructor for Level2Screen
     * Initializes game parameters, fonts, scoring system, and all game objects
     *
     * @param gameProps Properties containing game configuration data
     */
    public Level2Screen(Properties gameProps) {
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

        // Load Donkey health coordinates
        String[] healthCoords = gameProps.getProperty("gamePlay.donkeyhealth.coords").split(",");
        this.DONKEY_HEALTH_Y = Integer.parseInt(healthCoords[0]);
        this.DONKEY_HEALTH_X = Integer.parseInt(healthCoords[1]);

        // Initialize bullet display coordinates (positioned below Donkey health)
        this.BULLET_X = this.DONKEY_HEALTH_X;
        this.BULLET_Y = this.DONKEY_HEALTH_Y + BULLET_DISPLAY_DIFF_Y;

        // Initialize active bananas list and cooldown map
        this.activeBananas = new ArrayList<>();
        this.monkeyShootCooldowns = new HashMap<>();

        // Initialize game objects
        initializeGameObjects();
    }

    /**
     * Initializes all game objects for Level 2 based on configuration in properties file
     * This includes creating Mario, Donkey Kong, platforms, ladders, barrels, hammer,
     * blasters, normal monkeys, and intelligent monkeys
     */
    private void initializeGameObjects() {
        // 1) Create Mario
        String[] marioCoords = GAME_PROPS.getProperty("mario.level2").split(",");
        double marioX = Double.parseDouble(marioCoords[0]);
        double marioY = Double.parseDouble(marioCoords[1]);
        this.mario = new Mario(marioX, marioY);

        // 2) Create Donkey Kong
        String[] donkeyCoords = GAME_PROPS.getProperty("donkey.level2").split(",");
        double donkeyX = Double.parseDouble(donkeyCoords[0]);
        double donkeyY = Double.parseDouble(donkeyCoords[1]);
        this.donkey = new Donkey(donkeyX, donkeyY);

        // 3) Create Platforms
        String platformData = GAME_PROPS.getProperty("platforms.level2");
        String[] platformEntries = platformData.split(";");
        this.platforms = new Platform[platformEntries.length];
        for (int i = 0; i < platformEntries.length; i++) {
            String[] coords = platformEntries[i].split(",");
            platforms[i] = new Platform(Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]));
        }

        // 4) Create Ladders
        int ladderCount = Integer.parseInt(GAME_PROPS.getProperty("ladder.level2.count"));
        this.ladders = new Ladder[ladderCount];
        for (int i = 1; i <= ladderCount; i++) {
            String[] coords = GAME_PROPS.getProperty("ladder.level2." + i).split(",");
            ladders[i-1] = new Ladder(Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]));
        }

        // 5) Create Barrels
        int barrelCount = Integer.parseInt(GAME_PROPS.getProperty("barrel.level2.count"));
        this.barrels = new Barrel[barrelCount];
        for (int i = 1; i <= barrelCount; i++) {
            String[] coords = GAME_PROPS.getProperty("barrel.level2." + i).split(",");
            barrels[i-1] = new Barrel(Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]));
        }

        // 6) Create Hammer
        int hammerCount = Integer.parseInt(GAME_PROPS.getProperty("hammer.level2.count"));
        if (hammerCount > 0) {
            String[] hammerCoords = GAME_PROPS.getProperty("hammer.level2.1").split(",");
            this.hammer = new Hammer(Double.parseDouble(hammerCoords[0]),
                    Double.parseDouble(hammerCoords[1]));
        }

        // 7) Create Blasters
        int blasterCount = Integer.parseInt(GAME_PROPS.getProperty("blaster.level2.count"));
        this.blasters = new Blaster[blasterCount];
        for (int i = 1; i <= blasterCount; i++) {
            String[] coords = GAME_PROPS.getProperty("blaster.level2." + i).split(",");
            blasters[i-1] = new Blaster(Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]));
        }

        // 8) Create Normal Monkeys
        int normalMonkeyCount = Integer.parseInt(GAME_PROPS.getProperty("normalMonkey.level2.count"));
        this.normalMonkeys = new NormalMonkey[normalMonkeyCount];
        for (int i = 1; i <= normalMonkeyCount; i++) {
            String[] data = GAME_PROPS.getProperty("normalMonkey.level2." + i).split(";");
            String[] coords = data[0].split(",");
            boolean facingRight = data[1].equals("right");
            String[] routeData = data[2].split(",");
            int[] route = new int[routeData.length];
            for (int j = 0; j < routeData.length; j++) {
                route[j] = Integer.parseInt(routeData[j]);
            }
            normalMonkeys[i-1] = new NormalMonkey(Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]),
                    facingRight, route, platforms);
        }

        // 9) Create Intelligent Monkeys
        int intelliMonkeyCount = Integer.parseInt(GAME_PROPS.getProperty("intelligentMonkey.level2.count"));
        this.intelligentMonkeys = new IntelligentMonkey[intelliMonkeyCount];
        for (int i = 1; i <= intelliMonkeyCount; i++) {
            String[] data = GAME_PROPS.getProperty("intelligentMonkey.level2." + i).split(";");
            String[] coords = data[0].split(",");
            boolean facingRight = data[1].equals("right");
            String[] routeData = data[2].split(",");
            int[] route = new int[routeData.length];
            for (int j = 0; j < routeData.length; j++) {
                route[j] = Integer.parseInt(routeData[j]);
            }
            intelligentMonkeys[i-1] = new IntelligentMonkey(Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]),
                    facingRight, route, platforms);
        }
    }

    /**
     * Displays game information on the screen including score, time left,
     * Donkey's health, and bullet count
     */
    public void displayInfo() {
        STATUS_FONT.drawString(SCORE_MESSAGE + score, SCORE_X, SCORE_Y);

        // Calculate the time left
        int secondsLeft = (MAX_FRAMES - currFrame) / 60;

        // Display the time left
        int TIME_X = SCORE_X;
        int TIME_Y = SCORE_Y + TIME_DISPLAY_DIFF_Y;
        STATUS_FONT.drawString(TIME_MESSAGE + secondsLeft, TIME_X, TIME_Y);

        // Display Donkey's current health
        STATUS_FONT.drawString(DONKEY_HEALTH_MESSAGE + donkey.getHealth(),
                DONKEY_HEALTH_X, DONKEY_HEALTH_Y);

        // Display Mario's bullet count
        int bulletCount = mario.getBulletCount();
        STATUS_FONT.drawString(BULLET_MESSAGE + bulletCount, BULLET_X, BULLET_Y);
    }

    /**
     * Main update method that runs every frame
     * Updates all game objects, handles collisions, and checks win/lose conditions
     *
     * @param input User input from keyboard
     * @return true if level is completed or game is over, false if level continues
     */
    @Override
    public boolean update(Input input) {
        currFrame++;

        // Draw background
        drawBackground();

        // Draw platforms
        for (Platform platform : platforms) {
            platform.draw();
        }

        // Update ladders
        for (Ladder ladder : ladders) {
            ladder.update(platforms);
        }

        // Update barrels and check collisions
        for (Barrel barrel : barrels) {
            if (barrel == null) continue;

            if (mario.jumpOver(barrel)) {
                score += BARREL_CROSS_SCORE;
            }

            if (!barrel.isDestroyed() && mario.isTouchingBarrel(barrel)) {
                if (!mario.holdHammer()) {
                    isGameOver = true;
                } else {
                    barrel.destroy();
                    score += BARREL_SCORE;
                }
            }
            barrel.update(platforms);
        }

        // Update normal monkeys
        for (NormalMonkey monkey : normalMonkeys) {
            if (monkey != null && monkey.isAlive()) {
                monkey.update();
                // Check collision with Mario
                if (mario.getBoundingBox().intersects(monkey.getBoundingBox())) {
                    if (!mario.holdHammer()) {
                        isGameOver = true;
                    } else {
                        monkey.hit();
                        score += MONKEY_SCORE;
                        // Add to death counted list to prevent duplicate score
                        monkeyDeathCounted.add(monkey);
                    }
                }
            }
        }

        // Update intelligent monkeys and handle banana shooting
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            if (monkey != null && monkey.isAlive()) {
                // Update the monkey movement
                monkey.update();

                // Check if we need to initialize cooldown for this monkey
                if (!monkeyShootCooldowns.containsKey(monkey)) {
                    // Initialize with a random offset between 0-60 frames to stagger shots
                    monkeyShootCooldowns.put(monkey, (int)(Math.random() * 60));
                }

                // Update cooldown and shoot banana if needed
                int cooldown = monkeyShootCooldowns.get(monkey);
                cooldown++;
                if (cooldown >= BANANA_SHOOT_INTERVAL) {
                    // Create and add a new banana
                    Banana newBanana = new Banana(monkey.getX(), monkey.getY(), monkey.isFacingRight());
                    activeBananas.add(newBanana);
                    cooldown = 0;
                }
                monkeyShootCooldowns.put(monkey, cooldown);

                // Check collision with Mario
                if (mario.getBoundingBox().intersects(monkey.getBoundingBox())) {
                    if (!mario.holdHammer()) {
                        isGameOver = true;
                    } else {
                        monkey.hit();
                        score += MONKEY_SCORE;
                        // Add to death counted list to prevent duplicate score
                        monkeyDeathCounted.add(monkey);
                    }
                }
            }
        }

        // Update all active bananas
        List<Banana> bananasToRemove = new ArrayList<>();
        for (Banana banana : activeBananas) {
            // Update banana position
            boolean isActive = banana.update();
            if (!isActive) {
                bananasToRemove.add(banana);
                continue;
            }

            // Check collision with Mario
            if (mario.getBoundingBox().intersects(banana.getBoundingBox())) {
                // Regardless of hammer status, Mario dies when touching a banana
                isGameOver = true;
                break;
            }
        }

        // Remove bananas that are no longer active
        activeBananas.removeAll(bananasToRemove);

        // Draw blasters
        for (Blaster blaster : blasters) {
            if (blaster != null && !blaster.isCollected()) {
                blaster.draw();
            }
        }

        // Check game conditions
        if (checkingGameTime()) {
            isGameOver = true;
        }

        // Update remaining objects
        donkey.update(platforms);
        hammer.draw();

        // Update Mario with all Level 2 objects
        mario.update(input, ladders, platforms, hammer, blasters, donkey,
                combineMonkeys(normalMonkeys, intelligentMonkeys));

        // Check for monkeys killed by bullets (not by hammer)
        for (NormalMonkey monkey : normalMonkeys) {
            if (monkey != null && !monkey.isAlive() && !monkeyDeathCounted.contains(monkey)) {
                // This monkey was killed by a bullet, not by hammer collision
                score += MONKEY_SCORE;
                monkeyDeathCounted.add(monkey);
            }
        }

        // Check intelligent monkeys killed by bullets (not by hammer)
        for (IntelligentMonkey monkey : intelligentMonkeys) {
            if (monkey != null && !monkey.isAlive() && !monkeyDeathCounted.contains(monkey)) {
                // This monkey was killed by a bullet, not by hammer collision
                score += MONKEY_SCORE;
                monkeyDeathCounted.add(monkey);
            }
        }

        // Check win/lose conditions
        if (mario.hasReached(donkey)) {
            if (mario.holdHammer()) {
                return true; // Win condition 1: Reaching Donkey with hammer
            } else {
                isGameOver = true;
            }
        }

        // Win condition 2: Killing Donkey with bullets
        if (!donkey.isAlive()) {
            return true; // Win if Donkey is dead
        }

        displayInfo();
        return isGameOver;
    }

    /**
     * Checks if the game time has run out
     *
     * @return true if maximum frames have been exceeded, false otherwise
     */
    private boolean checkingGameTime() {
        return currFrame >= MAX_FRAMES;
    }

    /**
     * Combines normal and intelligent monkeys into a single array for Mario's update method
     * This allows Mario to handle all monkey types consistently
     *
     * @param normal Array of normal monkeys
     * @param intelligent Array of intelligent monkeys
     * @return Combined array containing all monkey types
     */
    private NormalMonkey[] combineMonkeys(NormalMonkey[] normal, IntelligentMonkey[] intelligent) {
        NormalMonkey[] combined = new NormalMonkey[normal.length + intelligent.length];
        System.arraycopy(normal, 0, combined, 0, normal.length);
        System.arraycopy(intelligent, 0, combined, normal.length, intelligent.length);
        return combined;
    }

    /**
     * Gets the current score
     *
     * @return Current score value
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the initial score for Level 2, typically carrying over from Level 1.
     *
     * @param initialScore The score carried over from Level 1.
     */
    public void setScore(int initialScore) {
        this.score = initialScore;
    }

    /**
     * Checks if the game is over due to player death or time limit
     *
     * @return true if game is over, false if still playing
     */
    public boolean isGameOver() {
        return isGameOver;
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
     * Checks if the game over is due to timeout rather than player death.
     *
     * @return {@code true} if game over is caused by timeout, {@code false} otherwise.
     */
    public boolean isTimeOut() {
        return checkingGameTime();
    }
}