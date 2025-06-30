package objects;
import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player-controlled character, Mario.
 * Mario can move, jump, climb ladders, pick up a hammer or blaster, and interact with platforms.
 * 
 * @author Minh Triet Pham
 */
public class Mario extends Entity{
    /**
     * The vertical velocity of Mario
     */
    private double velocityY = 0;

    /**
     * Whether Mario is currently jumping
     */
    private boolean isJumping = false;
    
    /**
     * Whether Mario has collected a hammer
     */
    private boolean hasHammer = false; 

    /**
     * Whether Mario has collected a blaster
     */
    private boolean hasBlaster = false; 

    /**
     * The number of bullets Mario has
     */
    private int bulletCount = 0; 

    /**
     * The current image of Mario based on different states
     */ 
    private Image marioImage;

    /**
     * The image representing Mario facing right
     */
    private final Image MARIO_RIGHT_IMAGE;

    /**
     * The image representing Mario facing left
     */
    private final Image MARIO_LEFT_IMAGE;

    /**
     * The image representing Mario with a hammer facing left
     */
    private final Image MARIO_HAMMER_LEFT_IMAGE;

    /**
     * The image representing Mario with a hammer facing right
     */
    private final Image MARIO_HAMMER_RIGHT_IMAGE;

    /**
     * The image representing Mario with a blaster facing left
     */
    private final Image MARIO_BLASTER_LEFT_IMAGE;

    /**
     * The image representing Mario with a blaster facing right
     */
    private final Image MARIO_BLASTER_RIGHT_IMAGE;

    /**
     * List to manage active bullets
     */
    private List<Bullet> bullets = new ArrayList<>();

    /**
     * The strength of Mario's jump
     */ 
    private static final double JUMP_STRENGTH = Physics.MARIO_JUMP_STRENGTH;

    /**
     * The speed of Mario's movement
     */
    private static final double MOVE_SPEED = Physics.MARIO_MOVE_SPEED;

    /**
     * The speed of Mario's climb
     */
    private static final double CLIMB_SPEED = Physics.MARIO_CLIMB_SPEED;

    /**
     * The height of Mario
     */
    private static double height;

    /**
     * The width of Mario
     */
    private static double width;

    /**
     * Whether Mario is facing right
     */ 
    private boolean isFacingRight = true;

    /**
     * Constructs a Mario character at the specified starting position.
     *
     * @param startX Initial x-coordinate.
     * @param startY Initial y-coordinate.
     */
    public Mario(double startX, double startY) {
        super(startX, startY);

        // Load images for different Mario states
        this.MARIO_RIGHT_IMAGE = new Image("res/mario_right.png");
        this.MARIO_LEFT_IMAGE = new Image("res/mario_left.png");
        this.MARIO_HAMMER_RIGHT_IMAGE = new Image("res/mario_hammer_right.png");
        this.MARIO_HAMMER_LEFT_IMAGE = new Image("res/mario_hammer_left.png");
        this.MARIO_BLASTER_RIGHT_IMAGE = new Image("res/mario_blaster_right.png");
        this.MARIO_BLASTER_LEFT_IMAGE = new Image("res/mario_blaster_left.png");

        // Default Mario starts facing right
        this.marioImage = MARIO_RIGHT_IMAGE;

        width = marioImage.getWidth();
        height = marioImage.getHeight();
    }

    /**
     * Sets whether Mario has picked up the hammer.
     * If Mario has a blaster, switching to a hammer will reset bullets to 0.
     *
     * @param status {@code true} if Mario has the hammer, {@code false} otherwise.
     */
    public void setHasHammer(boolean status) {
        this.hasHammer = status;
        if (status) {
            // If Mario gets a hammer, he loses the blaster and all bullets
            this.hasBlaster = false;
            this.bulletCount = 0;
        }
    }

    /**
     * Checks if Mario has the hammer.
     *
     * @return {@code true} if Mario has the hammer, {@code false} otherwise.
     */
    public boolean holdHammer() {
        return this.hasHammer;
    }

    /**
     * Sets whether Mario has picked up the blaster.
     * If Mario had a hammer, switching to a blaster will remove the hammer.
     *
     * @param status {@code true} if Mario has the blaster, {@code false} otherwise.
     * @param bullets The number of bullets to add to Mario's inventory.
     */
    public void setHasBlaster(boolean status, int bullets) {
        if (status) {
            // If Mario already has a blaster, just add more bullets
            if (this.hasBlaster) {
                this.bulletCount += bullets;
            } else {
                // If Mario is getting a new blaster
                this.hasBlaster = true;
                this.bulletCount += bullets;
                this.hasHammer = false; // Mario loses the hammer when getting a blaster
            }
        } else {
            this.hasBlaster = false;
        }
    }

    /**
     * Checks if Mario has the blaster.
     *
     * @return {@code true} if Mario has the blaster, {@code false} otherwise.
     */
    public boolean holdBlaster() {
        return this.hasBlaster && bulletCount > 0;
    }

    /**
     * Gets the current number of bullets Mario has.
     *
     * @return The number of bullets.
     */
    public int getBulletCount() {
        return this.bulletCount;
    }

    /**
     * Gets Mario's bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing Mario's collision area.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(
                getX() - (width / 2),
                getY() - (height / 2),
                width,
                height
        );
    }

    /**
     * Updates Mario's movement, jumping, ladder climbing, weapon collection, and interactions.
     * This method is called every frame to process player input and update Mario's state.
     *
     * @param input     The player's input (keyboard/mouse).
     * @param ladders   The array of ladders in the game that Mario can climb.
     * @param platforms The array of platforms in the game that Mario can walk on.
     * @param hammer    The hammer object that Mario can collect and use.
     * @param blasters  Array of blasters Mario can collect (can be null in level 1).
     * @param donkey    Donkey Kong object (needed for bullet collision checks).
     * @param monkeys   Array of monkeys (needed for bullet collision checks, can be null in level 1).
     */
    public void update(Input input, Ladder[] ladders, Platform[] platforms, Hammer hammer, 
                    Blaster[] blasters, Donkey donkey, NormalMonkey[] monkeys) {
        // 1) Horizontal movement
        handleHorizontalMovement(input);

        // 2) Update Mario's current sprite
        updateSprite();
        
        // 3) Handle weapon collection
        handleHammerCollection(hammer);
        if (blasters != null) {
            handleBlasterCollection(blasters);
        }
        
        // 4) Handle shooting if Mario has a blaster
        if (hasBlaster && bulletCount > 0 && input.wasPressed(Keys.S)) {
            shootBullet();
        }
        
        // 5) Update and check all active bullets
        updateBullets(donkey, monkeys, platforms);
        
        // 6) Ladder logic – check if on a ladder
        boolean isOnLadder;
        isOnLadder = handleLadders(input, ladders);

        // 7) Jump logic: if on platform but let's queue jump if needed
        boolean wantsToJump = input.wasPressed(Keys.SPACE);

        // 8) If not on ladder, apply gravity, move Mario
        if (!isOnLadder) {
            velocityY += Physics.MARIO_GRAVITY;
            velocityY = Math.min(Physics.MARIO_TERMINAL_VELOCITY, velocityY);
        }

        // 9) Actually move Mario vertically after gravity
        this.setY(getY() + velocityY);

        // 10) Check for platform collision AFTER Mario moves
        boolean onPlatform;
        onPlatform = handlePlatforms(platforms, hammer);

        // 11) If we are on the platform, allow jumping
        handleJumping(onPlatform, wantsToJump);

        // 12) Enforce horizontal screen bounds
        enforceBoundaries();

        // 13) Draw Mario and active bullets
        draw();
    }

    /**
     * Creates a new bullet and adds it to the active bullets list.
     * Decreases the bullet count.
     */
    private void shootBullet() {
        if (bulletCount > 0) {
            // Creates a new bullet and adds it to the active bullets list for tracking
            bullets.add(new Bullet(getX(), getY(), isFacingRight));

            // Decreases the bullet count
            bulletCount--;

            // If no bullets left, Mario goes back to normal state
            if (bulletCount == 0) {
                hasBlaster = false;
            }
        }
    }

    /**
     * Updates all active bullets and checks for collisions.
     * 
     * @param donkey    The Donkey Kong object to check for bullet hits.
     * @param monkeys   Array of NormalMonkey/IntelligentMonkey objects (can be null in level 1).
     * @param platforms Array of platforms to check for bullet collisions.
     */
    private void updateBullets(Donkey donkey, NormalMonkey[] monkeys, Platform[] platforms) {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        
        for (Bullet bullet : bullets) {
            // Update bullet position and check if it's still active
            boolean isActive = bullet.update();

            // If the bullet is not active, remove it from the list
            if (!isActive) {
                bulletsToRemove.add(bullet);
                continue;
            }
            
            // Check for collision with Donkey Kong
            if (bullet.getBoundingBox().intersects(donkey.getBoundingBox())) {
                donkey.decreaseHealth(1);
                bulletsToRemove.add(bullet);
                continue;
            }
            
            // Check for collision with platforms
            for (Platform platform : platforms) {
                if (bullet.getBoundingBox().intersects(platform.getBoundingBox())) {
                    bulletsToRemove.add(bullet);
                    break;
                }
            }
            
            // Check for collision with monkeys (if in level 2)
            if (monkeys != null) {
                for (NormalMonkey monkey : monkeys) {
                    if (monkey != null && monkey.isAlive() && 
                        bullet.getBoundingBox().intersects(monkey.getBoundingBox())) {
                        monkey.hit(); // This works for both NormalMonkey and IntelligentMonkey
                        bulletsToRemove.add(bullet);
                        break;
                    }
                }
            }
        }
        
        // Remove bullets that hit something or went off-screen
        bullets.removeAll(bulletsToRemove);
    }

    /**
     * Handles Mario's interaction with platforms to determine if he is standing on one.
     *
     * @param platforms An array of {@link Platform} objects representing the platforms in the game.
     * @param hammer    A {@link Hammer} object (not used in this method, but might be for future logic).
     * @return {@code true} if Mario is standing on a platform, {@code false} otherwise.
     */
    private boolean handlePlatforms(Platform[] platforms, Hammer hammer) {
        boolean onPlatform = false;

        // We'll only snap Mario to a platform if he's moving downward (velocityY >= 0)
        // so we don't kill his jump in mid-air.
        if (velocityY >= 0) {
            for (Platform platform : platforms) {
                Rectangle marioBounds    = getBoundingBox();
                Rectangle platformBounds = platform.getBoundingBox();

                if (marioBounds.intersects(platformBounds)) {
                    double marioBottom = marioBounds.bottom();
                    double platformTop = platformBounds.top();

                    // If Mario's bottom is at or above the platform's top
                    // and not far below it (a small threshold based on velocity)
                    if (marioBottom <= platformTop + velocityY) {
                        // Snap Mario so his bottom = the platform top
                        double newY = platformTop - (marioImage.getHeight() / 2);
                        this.setY(newY);
                        velocityY = 0;
                        isJumping = false;
                        onPlatform = true;
                        break; // We found a platform collision
                    }
                }
            }
        }
        return onPlatform;
    }

    /**
     * Handles Mario's interaction with ladders, allowing him to climb up or down.
     *
     * @param input   The {@link Input} object that checks for user key presses.
     * @param ladders An array of {@link Ladder} objects representing ladders in the game.
     * @return {@code true} if Mario is on a ladder, {@code false} otherwise.
     */
    private boolean handleLadders(Input input, Ladder[] ladders) {
        boolean isOnLadder = false;
        for (Ladder ladder : ladders) {
            double ladderLeft  = ladder.getX() - (ladder.getWidth() / 2);
            double ladderRight = ladder.getX() + (ladder.getWidth() / 2);
            double marioRight  = getX() + (marioImage.getWidth() / 2);
            double marioBottom = getY() + (marioImage.getHeight() / 2);
            double ladderTop    = ladder.getY() - (ladder.getHeight() / 2);
            double ladderBottom = ladder.getY() + (ladder.getHeight() / 2);

            if (isTouchingLadder(ladder)) {
                // Check horizontal overlap so Mario is truly on the ladder
                if (marioRight - marioImage.getWidth() / 2 > ladderLeft && marioRight - marioImage.getWidth() / 2 < ladderRight) {
                    isOnLadder = true;

                    // Stop Mario from sliding up when not moving
                    if (!input.isDown(Keys.UP) && !input.isDown(Keys.DOWN)) {
                        velocityY = 0;  // Prevent sliding inertia effect
                    }

                    // ----------- Climb UP -----------
                    if (input.isDown(Keys.UP)) {
                        this.setY(getY() - CLIMB_SPEED);
                        velocityY = 0;
                    }

                    // ----------- Climb DOWN -----------
                    if (input.isDown(Keys.DOWN)) {
                        double nextY = getY() + CLIMB_SPEED;
                        double nextBottom = nextY + (marioImage.getHeight() / 2);

                        if (marioBottom > ladderTop && nextBottom <= ladderBottom) {
                            this.setY(nextY);
                            velocityY = 0;
                        } else if (marioBottom == ladderBottom) {
                            velocityY = 0;
                        } else if (ladderBottom - marioBottom < CLIMB_SPEED) {
                            this.setY(getY() + ladderBottom - marioBottom);
                            velocityY = 0;
                        }
                    }
                }
            } else if (marioBottom == ladderTop && input.isDown(Keys.DOWN) && (marioRight - marioImage.getWidth() / 2 > ladderLeft && marioRight - marioImage.getWidth() / 2  < ladderRight)) {
                double nextY = getY() + CLIMB_SPEED;
                this.setY(nextY);
                velocityY = 0; // ignore gravity
            } else if (marioBottom == ladderBottom && input.isDown(Keys.DOWN) && (marioRight - marioImage.getWidth() / 2 > ladderLeft && marioRight - marioImage.getWidth() / 2  < ladderRight)) {
                velocityY = 0; // ignore gravity
            }
        }
        return isOnLadder;
    }

    /** 
     * Handles horizontal movement based on player input. 
     * 
     * @param input The player's input (keyboard/mouse).
     */
    private void handleHorizontalMovement(Input input) {
        if (input.isDown(Keys.LEFT)) {
            this.setX(getX() - MOVE_SPEED);
            isFacingRight = false;
        } else if (input.isDown(Keys.RIGHT)) {
            this.setX(getX() + MOVE_SPEED);
            isFacingRight = true;
        }
    }

    /** 
     * Updates Mario's sprite based on his current state (hammer, blaster, direction).
     */
    private void updateSprite() {
        // Remember the old image and its bottom
        Image oldImage = marioImage;
        double oldHeight = oldImage.getHeight();
        double oldBottom = getY() + (oldHeight / 2);

        // Determine which image to use based on current state
        if (hasHammer) {
            marioImage = isFacingRight ? MARIO_HAMMER_RIGHT_IMAGE : MARIO_HAMMER_LEFT_IMAGE;
        } else if (hasBlaster && bulletCount > 0) {
            marioImage = isFacingRight ? MARIO_BLASTER_RIGHT_IMAGE : MARIO_BLASTER_LEFT_IMAGE;
        } else {
            marioImage = isFacingRight ? MARIO_RIGHT_IMAGE : MARIO_LEFT_IMAGE;
        }

        // Now recalc Mario's bottom with the new image
        double newHeight = marioImage.getHeight();
        double newBottom = getY() + (newHeight / 2);

        // Shift 'y' so the bottom edge is the same as before
        double shift = newBottom - oldBottom;
        this.setY(getY() - shift);

        // Update the recorded width/height to match the new image
        width = marioImage.getWidth();
        height = newHeight;
    }

    /** 
     * Handles collecting the hammer if Mario is in contact with it. 
     * 
     * @param hammer The hammer object to check collision with.
     */
    private void handleHammerCollection(Hammer hammer) {
        if (!hammer.isCollected() && isTouchingHammer(hammer)) {
            setHasHammer(true);
            hammer.collect();
        }
    }

    /** 
     * Handles collecting blasters if Mario is in contact with them.
     * 
     * @param blasters Array of blaster objects in the level.
     */
    private void handleBlasterCollection(Blaster[] blasters) {
        for (Blaster blaster : blasters) {
            if (!blaster.isCollected() && isTouchingBlaster(blaster)) {
                setHasBlaster(true, blaster.getInitialBullets());
                blaster.collect();
            }
        }
    }

    /** 
     * Handles jumping if Mario is on a platform and jump is requested.
     * 
     * @param onPlatform Whether Mario is on a platform.
     * @param wantsToJump Whether Mario wants to jump.
     */
    private void handleJumping(boolean onPlatform, boolean wantsToJump) {
        // If Mario is on a platform and wants to jump, set the jump velocity
        if (onPlatform && wantsToJump) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
        }

        // If Mario is below the bottom of the screen, set his position to the bottom of the screen
        double bottomOfMario = getY() + (marioImage.getHeight() / 2);
        if (bottomOfMario > Window.getHeight()) {
            this.setY(Window.getHeight() - (marioImage.getHeight() / 2));
            velocityY = 0;
            isJumping = false;
        }
    }

    /**
     * Enforces screen boundaries to prevent Mario from moving out of bounds.
     */
    private void enforceBoundaries() {
        // Calculate half the width of the Mario image
        double halfW = marioImage.getWidth() / 2;

        // Prevent Mario from moving beyond the left edge of the screen
        if (getX() < halfW) {
            this.setX(halfW);
        }

        // Prevent Mario from moving beyond the right edge of the screen
        double maxX = Window.getWidth() - halfW;
        if (getX() > maxX) {
            this.setX(maxX);
        }

        // Calculate Mario's bottom edge position
        double bottomOfMario = getY() + (marioImage.getHeight() / 2);

        // Prevent Mario from falling below the bottom of the screen
        if (bottomOfMario > Window.getHeight()) {
            // Reposition Mario to stand on the bottom edge
            this.setY(Window.getHeight() - (marioImage.getHeight() / 2));

            // Stop vertical movement and reset jumping state
            velocityY = 0;
            isJumping = false;
        }
    }

    /**
     * Draws Mario and all active bullets on the screen.
     */
    @Override
    public void draw() {
        // Draw Mario
        marioImage.draw(getX(), getY());
        
        // Draw all active bullets
        for (Bullet bullet : bullets) {
            bullet.draw();
        }
    }

    /**
     * Checks if Mario is touching a ladder.
     *
     * @param ladder The ladder object to check collision with.
     * @return {@code true} if Mario is touching the ladder, {@code false} otherwise.
     */
    private boolean isTouchingLadder(Ladder ladder) {
        Rectangle marioBounds = getBoundingBox();
        return marioBounds.intersects(ladder.getBoundingBox());
    }

    /**
     * Checks if Mario is touching the hammer.
     *
     * @param hammer The hammer object to check collision with.
     * @return {@code true} if Mario is touching the hammer, {@code false} otherwise.
     */
    private boolean isTouchingHammer(Hammer hammer) {
        Rectangle marioBounds = getBoundingBox();
        return marioBounds.intersects(hammer.getBoundingBox());
    }

    /**
     * Checks if Mario is touching a blaster.
     *
     * @param blaster The blaster object to check collision with.
     * @return {@code true} if Mario is touching the blaster, {@code false} otherwise.
     */
    private boolean isTouchingBlaster(Blaster blaster) {
        Rectangle marioBounds = getBoundingBox();
        return marioBounds.intersects(blaster.getBoundingBox());
    }

    /**
     * Checks if Mario is touching a barrel.
     *
     * @param barrel The barrel object to check collision with.
     * @return {@code true} if Mario is touching the barrel, {@code false} otherwise.
     */
    public boolean isTouchingBarrel(Barrel barrel) {
        Rectangle marioBounds = getBoundingBox();
        return marioBounds.intersects(barrel.getBoundingBox());
    }

    /**
     * Checks if Mario has reached Donkey Kong.
     *
     * @param donkey The Donkey object to check collision with.
     * @return {@code true} if Mario has reached Donkey Kong, {@code false} otherwise.
     */
    public boolean hasReached(Donkey donkey) {
        Rectangle marioBounds = getBoundingBox();
        return marioBounds.intersects(donkey.getBoundingBox());
    }

    /**
     * Determines if Mario successfully jumps over a barrel.
     *
     * @param barrel The barrel object to check.
     * @return {@code true} if Mario successfully jumps over the barrel, {@code false} otherwise.
     */
    public boolean jumpOver(Barrel barrel) {
        return isJumping
                && Math.abs(this.getX() - barrel.getX()) <= 1
                && (this.getY() < barrel.getY())
                && ((this.getY() + height / 2) >= (barrel.getY() + barrel.getBarrelImage().getHeight() / 2
                - (JUMP_STRENGTH * JUMP_STRENGTH) / (2 * Physics.MARIO_GRAVITY) - height / 2));
    }
}