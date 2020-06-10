import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


/**
 * Class for the Buy Panel, where you can buy Towers, Observe how much money the player has
 * and the key bind instructions.
 */
public class BuyPanel {

    private static final Image BUY_PANEL = new Image("res/images/buypanel.png");
    private static final Image TANK_IMAGE = new Image("res/images/tank.png");
    private static final Image SUPER_TANK_IMAGE = new Image("res/images/supertank.png");
    private static final Image AIRPLANE_IMAGE = new Image("res/images/airsupport.png");
    private static final Font CURRENT_MONEY_FONT = new Font("res/fonts/DejaVuSans-Bold.ttf",50);
    private static final Font TOWER_PRICE_FONT = new Font("res/fonts/DejaVuSans-Bold.ttf",20);
    private static final Font KEY_BINDS_FONT = new Font("res/fonts/DejaVuSans-Bold.ttf",14);
    private static final int BUY_PANEL_POSITION_X = Window.getWidth()/2;
    private static final int BUY_PANEL_POSITION_Y = 50;
    private static final int KEY_BINDS_POSITION_X = Window.getWidth()/2;
    private static final int KEY_BINDS_POSITION_Y = 25;
    private static final int FIRST_TOWER_IMAGE_POSITION_X = 64;
    private static final int DISTANCE_BETWEEN_TOWER_IMAGES = 120;
    private static final int TOWER_IMAGE_POSITION_Y = 40;
    private static final int FIRST_PRICE_POSITION_X = 36;
    private static final int PRICE_POSITION_Y = 90;
    private static final int CURRENT_MONEY_POSITION_X = Window.getWidth()-200;
    private static final int CURRENT_MONEY_POSITION_Y = 65;
    private static final int TANK_PRICE = 250;
    private static final int SUPER_TANK_PRICE = 600;
    private static final int AIRPLANE_PRICE = 500;
    private static final Rectangle BUY_PANEL_BOUNDING_BOX = new Rectangle(BUY_PANEL.getBoundingBoxAt(new Point(BUY_PANEL_POSITION_X, BUY_PANEL_POSITION_Y)));

    /**
     * Getter for the Bounding Box of the Panel
     *
     * @return the Rectangle representing the Buy Panel
     */
    public static Rectangle getBuyPanelBoundingBox() {
        return BUY_PANEL_BOUNDING_BOX;
    }

    /**
     * Update the Buy Panel
     *
     * @param player The Current Player and his stats
     * @param mousePosition The current position of the mouse on the screen
     * @param wasLeftButtonPressed Check if the left button has been pressed
     * @return The Tower which the player has selected to buy
     */
    public String updateBuyPanel(Player player, Point mousePosition, boolean wasLeftButtonPressed) {
        DrawOptions textColour = new DrawOptions();
        Colour fontColour;

        BUY_PANEL.draw(BUY_PANEL_POSITION_X, BUY_PANEL_POSITION_Y);

        //Display how much money the player currently has
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.UK);
        CURRENT_MONEY_FONT.drawString(String.format("$%s",numberFormat.format(player.getCurrentMoney())),CURRENT_MONEY_POSITION_X,CURRENT_MONEY_POSITION_Y);

        //Tank Tower
        TANK_IMAGE.draw(FIRST_TOWER_IMAGE_POSITION_X, TOWER_IMAGE_POSITION_Y);
        if(player.getCurrentMoney() < TANK_PRICE) {
            fontColour = Colour.RED;
        } else {
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",TANK_PRICE),FIRST_PRICE_POSITION_X,PRICE_POSITION_Y, textColour.setBlendColour(fontColour));
        Rectangle tankBoundingBox = new Rectangle(TANK_IMAGE.getBoundingBoxAt(new Point(FIRST_TOWER_IMAGE_POSITION_X,TOWER_IMAGE_POSITION_Y)));
        if(tankBoundingBox.intersects(mousePosition)) {
            if (wasLeftButtonPressed) {
                if(player.getCurrentMoney() >= TANK_PRICE) {
                    //Return tha TANK tower was selected
                    return "TANK";
                }
            }
        }

        //Super Tank Tower
        SUPER_TANK_IMAGE.draw(FIRST_TOWER_IMAGE_POSITION_X + DISTANCE_BETWEEN_TOWER_IMAGES, TOWER_IMAGE_POSITION_Y);
        if(player.getCurrentMoney() < SUPER_TANK_PRICE) {
            fontColour = Colour.RED;
        } else {
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",SUPER_TANK_PRICE),FIRST_PRICE_POSITION_X + DISTANCE_BETWEEN_TOWER_IMAGES,PRICE_POSITION_Y, textColour.setBlendColour(fontColour));
        Rectangle superTankBoundingBox = new Rectangle(SUPER_TANK_IMAGE.getBoundingBoxAt(new Point(FIRST_TOWER_IMAGE_POSITION_X+DISTANCE_BETWEEN_TOWER_IMAGES,TOWER_IMAGE_POSITION_Y)));
        if(superTankBoundingBox.intersects(mousePosition)) {
            if (wasLeftButtonPressed) {
                if(player.getCurrentMoney() >= SUPER_TANK_PRICE) {
                    //Return tha SUPER TANK tower was selected
                    return "SUPER TANK";
                }
            }
        }

        //Airplane Tower
        AIRPLANE_IMAGE.draw(FIRST_TOWER_IMAGE_POSITION_X + 2 * DISTANCE_BETWEEN_TOWER_IMAGES, TOWER_IMAGE_POSITION_Y);
        if(player.getCurrentMoney() < AIRPLANE_PRICE) {
            fontColour = Colour.RED;
        } else {
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",AIRPLANE_PRICE),FIRST_PRICE_POSITION_X + 2 * DISTANCE_BETWEEN_TOWER_IMAGES,PRICE_POSITION_Y, textColour.setBlendColour(fontColour));
        Rectangle airplaneBoundingBox = new Rectangle(AIRPLANE_IMAGE.getBoundingBoxAt(new Point(FIRST_TOWER_IMAGE_POSITION_X + 2 * DISTANCE_BETWEEN_TOWER_IMAGES,TOWER_IMAGE_POSITION_Y)));
        if(airplaneBoundingBox.intersects(mousePosition)) {
            if (wasLeftButtonPressed) {
                if(player.getCurrentMoney() >= AIRPLANE_PRICE) {
                    //Return tha AIRPLANE tower was selected
                    return "AIRPLANE";
                }
            }
        }

        //Display the Key Bind instructions to the player
        KEY_BINDS_FONT.drawString("Key binds:\n\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale",KEY_BINDS_POSITION_X,KEY_BINDS_POSITION_Y);

        return "NO TOWER SELECTED";
    }

    /**
     *
     * Method to place a tower on the map
     *
     * @param towerType The tower type which is currently being placed
     * @param mousePosition The current position of the mouse on the screen
     * @param wasLeftButtonClicked Check if the left button has been pressed
     * @param wasRightButtonClicked Check if the right button has been pressed
     * @param currentTanks The current Tanks on the map
     * @param currentAirplanes The current Airplanes on the map
     * @param player The Current Player and his stats
     * @return If the Tower has Been Placed
     */
    public String placeTower(String towerType, Point mousePosition, boolean wasLeftButtonClicked, boolean wasRightButtonClicked, List<ActiveTower> currentTanks, List<Airplane> currentAirplanes, Player player) {

        if(((mousePosition.x > Window.getWidth()) || (mousePosition.x < 0) ) || (   (mousePosition.y > Window.getHeight()) || (mousePosition.y < 0))) {
            return towerType;
        }

        //Deselect the current tower selected
        if(wasRightButtonClicked) {
            return "NO TOWER SELECTED";
        }

        //Check if the Tank was selected
        if(towerType.equals("TANK")) {
            if(wasLeftButtonClicked) {
                currentTanks.add(new Tank(mousePosition,player));
                return "NO TOWER SELECTED";
            }
            TANK_IMAGE.draw(mousePosition.x,mousePosition.y);
        }

        //Check if the Super Tank was selected
        if(towerType.equals("SUPER TANK")) {
            if(wasLeftButtonClicked) {
                currentTanks.add(new SuperTank(mousePosition,player));
                return "NO TOWER SELECTED";
            }
            SUPER_TANK_IMAGE.draw(mousePosition.x,mousePosition.y);
        }

        //Check if the Air Plane was selected
        if(towerType.equals("AIRPLANE")) {
            if(wasLeftButtonClicked) {
                currentAirplanes.add(new Airplane(mousePosition,player));
                return "NO TOWER SELECTED";
            }
            AIRPLANE_IMAGE.draw(mousePosition.x,mousePosition.y);
        }

        //Tower has not been placed yet
        return towerType;
    }
}
