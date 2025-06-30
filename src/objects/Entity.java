package objects;

/**
 * Base class for all objects in the game
 * Set the x and y coordinates of the entity
 * 
 * @author Minh Triet Pham 
 */
public abstract class Entity {
    /**
     * The x coordinates of the Entity
     */
    private double x;

    /**
     * The y coordinates of the Entity
     */
    private double y;

    /**
     * Constructs a Hammer at the specified position.
     *
     * @param x The initial x-coordinate of the entity
     * @param y The initial y-coordinate of the entity
     */
    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinates of the Entity
     * 
     * @return The x-coordinate of the entity
     */
    public double getX() { return this.x; }

    /**
     * Get the y coordinates of the Entity
     * 
     * @return The y-coordinate of the entity
     */
    public double getY() { return this.y; }

    /**
     * Set the x coordinates of the Entity
     * 
     * @param x The new x-coordinate of the entity
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set the y coordinates of the Entity
     * 
     * @param y The new y-coordinate of the entity
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Abstract method that will be overridden later by subclasses
     */
    public abstract void draw();
}
