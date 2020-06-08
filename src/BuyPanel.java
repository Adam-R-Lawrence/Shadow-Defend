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
    private static final Font INSTRUCTIONS_FONT = new Font("res/fonts/DejaVuSans-Bold.ttf",14);
    private static final int TANK_PRICE = 250;
    private static final int SUPER_TANK_PRICE = 600;
    private static final int AIRPLANE_PRICE = 500;
    private static final Rectangle BUY_PANEL_BOUNDING_BOX = new Rectangle(BUY_PANEL.getBoundingBoxAt(new Point(Window.getWidth()/2.0, 50)));

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

        BUY_PANEL.draw(Window.getWidth()/2.0, 50);

        //Display how much money the player currently has
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.UK);
        CURRENT_MONEY_FONT.drawString(String.format("$%s",numberFormat.format(player.getMoney())),Window.getWidth()-200,65);


        TANK_IMAGE.draw(64, 40);
        if(player.getMoney() < 250){
            fontColour = Colour.RED;
        }else{
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",TANK_PRICE),36,50+40, textColour.setBlendColour(fontColour));
        Rectangle tankBoundingBox = new Rectangle(TANK_IMAGE.getBoundingBoxAt(new Point(64,40)));
        if(tankBoundingBox.intersects(mousePosition)){
            if (wasLeftButtonPressed){
                if(player.getMoney() >= TANK_PRICE){
                    //Return the tower selected
                    return "TANK";
                }
            }
        }


        SUPER_TANK_IMAGE.draw(64 + 120, 40);
        if(player.getMoney() < 600){
            fontColour = Colour.RED;
        }else{
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",SUPER_TANK_PRICE),36+120,50+40, textColour.setBlendColour(fontColour));
        Rectangle superTankBoundingBox = new Rectangle(SUPER_TANK_IMAGE.getBoundingBoxAt(new Point(64+120,40)));
        if(superTankBoundingBox.intersects(mousePosition)){
            if (wasLeftButtonPressed){
                if(player.getMoney() >= SUPER_TANK_PRICE){
                    //Return the tower selected
                    return "SUPER TANK";
                }
            }
        }


        AIRPLANE_IMAGE.draw(64 + 240, 40);

        if(player.getMoney() < 500){
            fontColour = Colour.RED;
        }else{
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",AIRPLANE_PRICE),36+240,50+40, textColour.setBlendColour(fontColour));
        Rectangle airplaneBoundingBox = new Rectangle(AIRPLANE_IMAGE.getBoundingBoxAt(new Point(64+240,40)));
        if(airplaneBoundingBox.intersects(mousePosition)){
            if (wasLeftButtonPressed){
                if(player.getMoney() >= AIRPLANE_PRICE){
                    //Return the tower selected
                    return "AIRPLANE";
                }
            }
        }


        //Display the Key Bind instructions to the player
        INSTRUCTIONS_FONT.drawString("Key binds:\n\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale",Window.getWidth()/2.0,25);

        return "NO TOWER SELECTED";
    }


    /**
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
    public String placeTower(String towerType, Point mousePosition, boolean wasLeftButtonClicked, boolean wasRightButtonClicked, List<ActiveTower> currentTanks, List<Airplane> currentAirplanes, Player player){

        if(((mousePosition.x > Window.getWidth()) || (mousePosition.x < 0) ) || (   (mousePosition.y > Window.getHeight()) || (mousePosition.y < 0))){


            return towerType;
        }

        if(wasRightButtonClicked){
            return "NO TOWER SELECTED";
        }


        if(towerType.equals("TANK")){
            if(wasLeftButtonClicked){
                currentTanks.add(new Tank(mousePosition,player));
                return "NO TOWER SELECTED";
            }
            TANK_IMAGE.draw(mousePosition.x,mousePosition.y);
        }
        if(towerType.equals("SUPER TANK")){
            if(wasLeftButtonClicked){
                currentTanks.add(new SuperTank(mousePosition,player));
                return "NO TOWER SELECTED";
            }
            SUPER_TANK_IMAGE.draw(mousePosition.x,mousePosition.y);

        }
        if(towerType.equals("AIRPLANE")){
            if(wasLeftButtonClicked){
                currentAirplanes.add(new Airplane(mousePosition,player));
                return "NO TOWER SELECTED";
            }
            AIRPLANE_IMAGE.draw(mousePosition.x,mousePosition.y);

        }

        return towerType;
    }
}
