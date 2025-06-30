package objects;

import bagel.util.Rectangle;

/**
 * Abstract Class that extended from Entity to be a base class for Bullet and Banana classes
 * 
 * @author Minh Triet Pham
 */
public abstract class ProjectileEntity extends Entity{
    /** 
     * Whether this entity is still exist on the screen or not
     */
    private boolean active = true;

    /**
     * The distance travelled by the entity - to check if it has reached its maximum distance
     */
    private double distanceTravelled = 0;

    /** 
     * Signal of the direction for this entity
     */
    private final boolean isFacingRight;

    /**
     * Constructs a ProjectileEntity at the specified position and specified direction
     * @param startX The initial x-coordinate of this entity
     * @param startY The initial y-coordinate of this entity
     * @param isFacingRight Whether the entity is facing right or not 
     */
    public ProjectileEntity(double startX, double startY, boolean isFacingRight) {
        super(startX, startY);
        this.isFacingRight = isFacingRight;
    }

    /**
     * Abstract methods that will be overridden in Bullet and Banana classes
     * 
     * @return Whether the entity is still active
     */
    public abstract boolean update();

    /**
     * Abstract methods that will be overridden in Bullet and Banana classes
     * 
     * @return The bounding box of the entity
     */
    public abstract Rectangle getBoundingBox();

    /**
     * Abstract methods that will be overridden in Bullet and Banana classes
     * Draw the entity
     */
    public abstract void draw();

    /**
     * Deactivates the entity, removing it from the game.
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Checks if the entity is still active in the game.
     *
     * @return True if active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Return the distance travelled of a ProjectileEntity
     * 
     * @return The distance travelled of a ProjectileEntity
     */
    public double getDistanceTravelled() {
        return this.distanceTravelled;
    }

    /**
     * Set a new distance travelled of a ProjectileEntity
     * 
     * @param distanceTravelled The new distance travelled of a ProjectileEntity
     */
    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    /**
     * Return whether this ProjectileEntity is facing right or not
     * 
     * @return Whether this ProjectileEntity is facing right or not
     */
    public boolean isFacingRight() {
        return this.isFacingRight;
    }
}
