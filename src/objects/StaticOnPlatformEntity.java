package objects;

import bagel.util.Rectangle;

/**
 * Abstract Class that extended from Entity to be the base class for Donkey, Barrel, Ladder classes
 * These classes will stay static on the platform after falling down by gravity
 * 
 * @author Minh Triet Pham
 */
public abstract class StaticOnPlatformEntity extends Entity{
    /**
     * The initial velocity of the entity
     */
    private double velocityY = 0;

    /**
     * Constructs a new StaticOnPlatformEntity at the specified starting position.
     * @param startX The initial x-coordinate of Donkey.
     * @param startY The initial y-coordinate of Donkey.
     */
    public StaticOnPlatformEntity(double startX, double startY) {
        super(startX, startY);
    }

    /**
     * Return the current velocityY of this StaticOnPlatformEntity
     * 
     * @return The current velocityY of this StaticOnPlatformEntity
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Set a new velocityY for this StaticOnPlatformEntity
     * 
     * @param velocityY The new velocityY of this StaticOnPlatformEntity
     */
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Abstract methods that will be overridden in Donkey, Barrel and Ladder classes
     * Update the entity based on the platforms and gravity
     * 
     * @param platforms The array of platforms in the game
     */
    public abstract void update(Platform[] platforms);

    /**
     * Abstract methods that will be overridden in Donkey, Barrel and Ladder classes
     * Draw the entity
     */
    public abstract void draw();

    /**
     * Abstract methods that will be overridden in Donkey, Barrel and Ladder classes
     * Get the bounding box of the entity
     * 
     * @return The bounding box of the entity
     */
    public abstract Rectangle getBoundingBox();
}
