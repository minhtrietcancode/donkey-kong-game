package screens;
import bagel.*;
import java.util.Properties;

/**
 * An abstract class that serves as a base for all game screens.
 * This class provides common functionality shared across different screens
 * such as background image handling and screen transitions.
 * 
 * @author Minh Triet Pham
 */
public abstract class AbstractScreen {
    /**
     * The background image for the screen
     */ 
    protected final Image BACKGROUND_IMAGE; 
    
    /**
     * Constructs the AbstractScreen with a background image.
     *
     * @param gameProps Properties file containing image paths and other configuration.
     */
    public AbstractScreen(Properties gameProps) {
        // Load the background image from properties
        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage"));
    }
    
    /**
     * Abstract method to update the screen state based on user input.
     * Each concrete screen class should implement this method to define its behavior.
     *
     * @param input The current mouse/keyboard input.
     * @return {@code true} if the screen should transition to the next screen, {@code false} otherwise.
     */
    public abstract boolean update(Input input);
    
    /**
     * Draws the background image for the screen.
     * This is a common operation across all screens.
     */
    protected void drawBackground() {
        BACKGROUND_IMAGE.drawFromTopLeft(0, 0);
    }
} 