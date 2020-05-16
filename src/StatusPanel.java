import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;

public class StatusPanel {
    private static final Image STATUS_PANEL = new Image("res/images/statuspanel.png");
    private static final int FONT_SIZE = 20;
    private static final Font FONT = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);

    /**
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
        STATUS_PANEL.draw(Window.getWidth()/2.0, Window.getHeight() - 12.5);

        //Display the current wave
        text  = String.format("Wave: %d", currentWave);
        FONT.drawString(text,10, Window.getHeight()-6.25);

        //Display the current timescale, if timescale has been increase, draw it in green
        if (timescaleMultiplier > 1){
            timescaleMultiplierColour = Colour.GREEN;
        } else {
            timescaleMultiplierColour = Colour.WHITE;
        }
        text  = String.format("Time Scale: %d.0", timescaleMultiplier);
        FONT.drawString(text,200,Window.getHeight()-6.25, textColour.setBlendColour(timescaleMultiplierColour));

        /*
         * Display the Status of the game, one of 4 options:
         * "Winner!" - If the player has successfully passed all waves
         * "Placing" - If the player is placing a tower
         * "Wave in progress" - If there is a wave currently in progress
         * "Awaiting Start" - The game is awaiting the player to press 'S' to begin a new wave
         */
        text = String.format("Status: %s",currentStatus);
        FONT.drawString(text,500,Window.getHeight()-6.25);

        //Display the current amount of lives that the player has remaining
        text = String.format("Lives: %d", player.getLives());
        FONT.drawString(text,920,Window.getHeight()-6.25);
    }
}