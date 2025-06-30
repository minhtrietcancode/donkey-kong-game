package screens;
import bagel.*;
import java.util.Properties;

/**
 * A class representing the home screen of the game.
 * This screen displays the game title, a prompt for the player, and a background image.
 * 
 * @author Minh Triet Pham
 */
public class HomeScreen extends AbstractScreen {
    /**
     * The title text displayed at the top
     */
    private final String TITLE;

    /**
     * The instruction prompt (e.g., "PRESS ENTER TO START")
     */
    private final String PROMPT;

    /**
     * The font used for the title
     */
    private final Font TITLE_FONT;

    /**
     * The font used for the prompt
     */
    private final Font PROMPT_FONT;

    /**
     * The vertical position of the title
     */
    private final int TITLE_Y;             

    /**
     * The vertical position of the prompt
     */
    private final int PROMPT_Y;            

    /**
     * Constructs the HomeScreen, loading images, fonts, and text properties.
     *
     * @param gameProps Properties file containing image paths and font details.
     * @param msgProps  Properties file containing title and prompt text.
     */
    public HomeScreen(Properties gameProps, Properties msgProps) {
        super(gameProps);

        // Load title and prompt text from properties
        TITLE = msgProps.getProperty("home.title");
        PROMPT = msgProps.getProperty("home.prompt");   // e.g., "PRESS ENTER TO START"

        // Load title font and its position
        TITLE_FONT = new Font(
                gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("home.title.fontSize"))
        );
        TITLE_Y = Integer.parseInt(gameProps.getProperty("home.title.y"));

        // Load prompt font and its position
        PROMPT_FONT = new Font(
                gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("home.prompt.fontSize"))
        );
        PROMPT_Y = Integer.parseInt(gameProps.getProperty("home.prompt.y"));
    }

    /**
     * Displays the home screen with the title and background.
     * Waits for the player to press ENTER to proceed.
     *
     * @param input The current mouse/keyboard input.
     * @return {@code true} if ENTER key is pressed (to start the game), {@code false} otherwise.
     */
    @Override
    public boolean update(Input input) {
        // 1) Draw the background image at the top-left corner
        drawBackground();

        // 2) Draw the game title, centered horizontally
        double titleX = Window.getWidth() / 2 - TITLE_FONT.getWidth(TITLE) / 2;
        TITLE_FONT.drawString(TITLE, titleX, TITLE_Y);

        // 3) Draw the prompt text (e.g., "PRESS ENTER TO START"), centered horizontally
        double promptX = Window.getWidth() / 2 - PROMPT_FONT.getWidth(PROMPT) / 2;
        PROMPT_FONT.drawString(PROMPT, promptX, PROMPT_Y);

        // 4) If ENTER is pressed, transition from the home screen to the game
        if (input.wasPressed(Keys.ENTER)) {
            return true;
        }

        // 5) Otherwise, remain on the home screen
        return false;
    }
}
