import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.text.NumberFormat;
import java.util.Locale;



public class BuyPanel {
    private static final Image BUY_PANEL = new Image("res/images/buypanel.png");
    private static final Image TANK = new Image("res/images/tank.png");
    private static final Image SUPER_TANK = new Image("res/images/supertank.png");
    private static final Image AIRPLANE = new Image("res/images/airsupport.png");
    private static final Font CURRENT_MONEY_FONT = new Font("res/fonts/DejaVuSans-Bold.ttf",50);
    private static final Font TOWER_PRICE_FONT = new Font("res/fonts/DejaVuSans-Bold.ttf",20);
    private static final Font INSTRUCTIONS_FONT = new Font("res/fonts/DejaVuSans-Bold.ttf",14);
    private static final int TANK_PRICE = 250;
    private static final int SUPER_TANK_PRICE = 600;
    private static final int AIRPLANE_PRICE = 500;


    public String updateBuyPanel(Player player, Point mousePosition, Boolean wasLeftButtonPressed) {
        DrawOptions textColour = new DrawOptions();
        Colour fontColour;

        BUY_PANEL.draw(Window.getWidth()/2.0, 50);

        //Display how much money the player currently has
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.UK);
        CURRENT_MONEY_FONT.drawString(String.format("$%s",numberFormat.format(player.getMoney())),Window.getWidth()-200,65);


        TANK.draw(64, 40);
        if(player.getMoney() < 250){
            fontColour = Colour.RED;
        }else{
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",TANK_PRICE),36,50+40, textColour.setBlendColour(fontColour));
        Rectangle tankBoundingBox = new Rectangle(TANK.getBoundingBoxAt(new Point(64,40)));
        if(tankBoundingBox.intersects(mousePosition)){
            if (wasLeftButtonPressed){
                if(player.getMoney() >= TANK_PRICE){
                    //Return the tower selected
                    return "TANK";
                }
            }
        }


        SUPER_TANK.draw(64 + 120, 40);
        if(player.getMoney() < 600){
            fontColour = Colour.RED;
        }else{
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",SUPER_TANK_PRICE),36+120,50+40, textColour.setBlendColour(fontColour));
        Rectangle superTankBoundingBox = new Rectangle(SUPER_TANK.getBoundingBoxAt(new Point(64+120,40)));
        if(superTankBoundingBox.intersects(mousePosition)){
            if (wasLeftButtonPressed){
                if(player.getMoney() >= SUPER_TANK_PRICE){
                    //Return the tower selected
                    return "SUPER TANK";
                }
            }
        }


        AIRPLANE.draw(64 + 240, 40);

        if(player.getMoney() < 500){
            fontColour = Colour.RED;
        }else{
            fontColour = Colour.GREEN;
        }
        TOWER_PRICE_FONT.drawString(String.format("$%d",AIRPLANE_PRICE),36+240,50+40, textColour.setBlendColour(fontColour));
        Rectangle airplaneBoundingBox = new Rectangle(AIRPLANE.getBoundingBoxAt(new Point(64+240,40)));
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
}
