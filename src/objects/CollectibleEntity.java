package objects;

import bagel.util.Rectangle;

/**
 * Abstract Class extends Entity, in order to be extended by Hammer and Blaster - the
 * 2 objects that can be collected by Mario
 * 
 * @author Minh Triet Pham
 */
public abstract class CollectibleEntity extends Entity{
    /**
     * Whether the object has been collected
     */
    private boolean isCollected;

    /**
     * Constructs a CollectibleEntity at the specified position and initialize the isCollected flag to be false
     * @param startX The initial x-coordinate of this entity
     * @param startY The initial y-coordinate of this entity
     */
    public CollectibleEntity(double startX, double startY) {
        super(startX, startY);
        this.isCollected = false;
    }

    /**
     * Method to mark the signal of this object to be collected
     */
    public void collect() {
        this.isCollected = true;
    }

    /**
     * Return the signal of the object to check whether it has been collected or not
     * 
     * @return Whether the object has been collected
     */
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Abstract Method that will be overridden by 2 concrete classes: Hammer and Blaster
     * Draw the object
     */
    public abstract void draw();

    /**
     * Abstract Method that will be overridden by 2 concrete classes: Hammer and Blaster
     * Get the bounding box of the object
     * 
     * @return The bounding box of the object
     */
    public abstract Rectangle getBoundingBox();
}
