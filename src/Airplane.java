import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Airplane {
    private static final Image AIRPLANE = new Image("res/images/airsupport.png");
    private static final int PIXELS_PER_FRAME = 5;
    private static String nextDirection = "Horizontally";
    private final String currentDirection;
    private double currentPositionX;
    private double currentPositionY;
    private int randomNumFrames = 0;
    private final List<Explosive> currentExplosives;
    private static final int PRICE = 500;

    public Airplane(Point whereToFly, Player player){

        player.decreaseMoney(PRICE);
        currentDirection = nextDirection;
        currentPositionX = whereToFly.x;
        currentPositionY = whereToFly.y;
        if (nextDirection.equals("Horizontally")){
            nextDirection = "Vertically";
            currentPositionX = 0;
        } else{
            nextDirection = "Horizontally";
            currentPositionY = 0;
        }


        currentExplosives = new ArrayList<>();
    }


    public List<Explosive> getCurrentExplosives() {
        return currentExplosives;
    }


    public int updateAirplane(int timescaleMultiplier){
        int status = 0;
        DrawOptions rotator = new DrawOptions();

        for(int i =0; i < timescaleMultiplier; i++) {
            if (currentDirection.equals("Horizontally")) {
                currentPositionX = currentPositionX + PIXELS_PER_FRAME;
            } else {
                currentPositionY = currentPositionY + PIXELS_PER_FRAME;
            }

            if ((((currentPositionX < Window.getWidth()) && (currentPositionX > 0)) && ((currentPositionY < Window.getHeight()) && (currentPositionY > 0)))) {
                if (randomNumFrames == 0) {
                    randomNumFrames = ThreadLocalRandom.current().nextInt(1, 4) * 60;
                }

                randomNumFrames--;
                if (randomNumFrames == 0) {
                    currentExplosives.add(new Explosive(new Point(currentPositionX, currentPositionY)));
                }
            }
        }

        if (currentDirection.equals("Horizontally")){
            AIRPLANE.draw(currentPositionX, currentPositionY, rotator.setRotation(Math.PI / 2));
        } else{
            AIRPLANE.draw(currentPositionX, currentPositionY, rotator.setRotation(Math.PI));
        }

        //Update Explosions
        int i = 0;
        for(Explosive s : currentExplosives){
            if(s!=null){
                 status = s.updateExplosion(timescaleMultiplier);
            }
            if(status == 1){
                currentExplosives.set(i, null);
            }
            i++;
        }

        currentExplosives.removeAll(Collections.singleton(null));

        if( ((   (currentPositionX > Window.getWidth()) || (currentPositionX < 0) ) || (   (currentPositionY > Window.getHeight()) || (currentPositionY < 0)))  && currentExplosives.size() == 0){
            return 1;
        }

        return 0;
    }
}
