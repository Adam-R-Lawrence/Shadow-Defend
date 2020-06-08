import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for the Passive Tower, Airplane that travels across the map dropping explosives
 */
public class Airplane {

    private static final Image AIRPLANE = new Image("res/images/airsupport.png");
    private static final int PIXELS_PER_FRAME = 5;
    private static final int HORIZONTAL_DIRECTION = 0;
    private static final int VERTICAL_DIRECTION = 1;
    private static final int PRICE = 500;

    private static int nextDirection = HORIZONTAL_DIRECTION;
    private final int currentDirection;
    private double currentPositionX;
    private double currentPositionY;
    private int randomNumFrames = 0;
    private final List<Explosive> currentExplosives;

    /**
     * Constructor for the Airplane
     *
     * @param whereToFly The Point where the Plane was placed
     * @param player The current player to deduct the price of the Tower from
     */
    public Airplane(Point whereToFly, Player player){

        player.decreaseMoney(PRICE);
        currentDirection = nextDirection;
        currentPositionX = whereToFly.x;
        currentPositionY = whereToFly.y;
        if (nextDirection == HORIZONTAL_DIRECTION){
            nextDirection = VERTICAL_DIRECTION ;
            currentPositionX = 0;
        } else{
            nextDirection = HORIZONTAL_DIRECTION;
            currentPositionY = 0;
        }


        currentExplosives = new ArrayList<>();
    }

    /**
     * Getter for the current explosives that have been dropped by this airplane
     *
     * @return List of current explosives
     */
    public List<Explosive> getCurrentExplosives() {
        return currentExplosives;
    }

    /**
     * Update the Airplane
     *
     * @param timescaleMultiplier The current Timescale Of the Game
     * @return Whether the plane needs to be removed from the game, i.e. left the map & all explosives have exploded
     */
    public int updateAirplane(int timescaleMultiplier){
        int status = 0;
        DrawOptions rotator = new DrawOptions();

        for(int i =0; i < timescaleMultiplier; i++) {
            if (currentDirection == HORIZONTAL_DIRECTION) {
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

        if (currentDirection == HORIZONTAL_DIRECTION){
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

        if((((currentPositionX > Window.getWidth()) || (currentPositionX < 0) ) || (   (currentPositionY > Window.getHeight()) || (currentPositionY < 0)))  && currentExplosives.size() == 0){
            return 1;
        }

        return 0;
    }
}
