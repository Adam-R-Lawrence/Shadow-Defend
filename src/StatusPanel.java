import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Class for the Status Panel
 */
public class StatusPanel {
    private static final Image STATUS_PANEL = new Image("res/images/statuspanel.png");
    private static final int FONT_SIZE = 20;
    private static final Font FONT = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
    private static final Rectangle STATUS_PANEL_BOUNDING_BOX = new Rectangle(STATUS_PANEL.getBoundingBoxAt(new Point(Window.getWidth()/2.0, Window.getHeight() - 12.5)));
    private static final int DEFAULT_TIMESCALE = 1;
    private static final double STATUS_PANEL_POSITION_X = Window.getWidth()/2.0;
    private static final double STATUS_PANEL_POSITION_Y = Window.getHeight() - 12.5;
    private static final double TEXT_POSITION_Y = Window.getHeight()-6.25;
    private static final double WAVE_INFO_POSITION_X = 10;
    private static final double TIMESCALE_INFO_POSITION_X = 200;
    private static final double STATUS_INFO_POSITION_X = 500;
    private static final double LIVES_INFO_POSITION_X = 920;

    /**
     *
     * @return Rectangle of the Status Panel
     */
    public static Rectangle getStatusPanelBoundingBox() {
        return STATUS_PANEL_BOUNDING_BOX;
    }

    /**
     * Update the Status Panel
     *
     * @param player The player's current information
     * @param currentWave What Wave the player is currently on
     * @param timescaleMultiplier The timescale in which the game is being run at
     * @param currentStatus The current status of the game
     */
    public void updateStatusPanel(Player player, int currentWave, int timescaleMultiplier, String currentStatus) {
        String text;
        Colour timescaleMultiplierColour;
        DrawOptions textColour = new DrawOptions();
        STATUS_PANEL.draw(STATUS_PANEL_POSITION_X, STATUS_PANEL_POSITION_Y);

        //Display the current wave
        text  = String.format("Wave: %d", currentWave);
        FONT.drawString(text,WAVE_INFO_POSITION_X, TEXT_POSITION_Y);

        //Display the current timescale, if timescale has been increase, draw it in green
        if (timescaleMultiplier > DEFAULT_TIMESCALE) {
            timescaleMultiplierColour = Colour.GREEN;
        } else {
            timescaleMultiplierColour = Colour.WHITE;
        }
        text  = String.format("Time Scale: %d.0", timescaleMultiplier);
        FONT.drawString(text,TIMESCALE_INFO_POSITION_X,TEXT_POSITION_Y, textColour.setBlendColour(timescaleMultiplierColour));

        /*
         * Display the Status of the game, one of 4 options:
         * "Winner!" - If the player has successfully passed all waves
         * "Placing" - If the player is placing a tower
         * "Wave in progress" - If there is a wave currently in progress
         * "Awaiting Start" - The game is awaiting the player to press 'S' to begin a new wave
         */
        text = String.format("Status: %s",currentStatus);
        FONT.drawString(text,STATUS_INFO_POSITION_X,TEXT_POSITION_Y);

        //Display the current amount of lives that the player has remaining
        text = String.format("Lives: %d", player.getCurrentLives());
        FONT.drawString(text,LIVES_INFO_POSITION_X,TEXT_POSITION_Y);
    }
}