import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for the Passive Tower, an Airplane that travels across the map dropping explosives
 */
public class Airplane {

    private static final Image AIRPLANE = new Image("res/images/airsupport.png");
    private static final int PIXELS_PER_FRAME = 5;
    private static final int HORIZONTAL_DIRECTION = 0;
    private static final int VERTICAL_DIRECTION = 1;
    private static final int PRICE = 500;
    private static final int FRAMES_PER_SECOND = 60;
    private static final double FULL_ANGLE_RADIANS = Math.PI;
    private static final double HALF_ANGLE_RADIANS = Math.PI/2;

    private static int nextDirection = HORIZONTAL_DIRECTION;
    private final int currentDirection;
    private double currentPositionX;
    private double currentPositionY;
    private int randomNumFrames = 0;
    private final List<Explosive> currentExplosives = new ArrayList<>();

    /**
     * Constructor for the Airplane
     *
     * @param whereToFly The Point where the Plane was placed
     * @param player The current player to deduct the price of the Tower from
     */
    public Airplane(Point whereToFly, Player player) {

        player.decreaseMoney(PRICE);
        currentDirection = nextDirection;
        currentPositionX = whereToFly.x;
        currentPositionY = whereToFly.y;

        //Determine which direction to fly
        if (nextDirection == HORIZONTAL_DIRECTION) {
            nextDirection = VERTICAL_DIRECTION ;
            currentPositionX = 0;
        } else {
            nextDirection = HORIZONTAL_DIRECTION;
            currentPositionY = 0;
        }
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
    public boolean updateAirplane(int timescaleMultiplier) {
        boolean hasExploded = false;
        DrawOptions rotator = new DrawOptions();

        for(int i =0; i < timescaleMultiplier; i++) {

            //Calculate the new position
            if (currentDirection == HORIZONTAL_DIRECTION) {
                currentPositionX = currentPositionX + PIXELS_PER_FRAME;
            } else {
                currentPositionY = currentPositionY + PIXELS_PER_FRAME;
            }

            //Determine whether or not to drop an explosive
            if ((((currentPositionX < Window.getWidth()) && (currentPositionX > 0)) && ((currentPositionY < Window.getHeight()) && (currentPositionY > 0)))) {
                if (randomNumFrames == 0) {
                    randomNumFrames = ThreadLocalRandom.current().nextInt(1, 4) * FRAMES_PER_SECOND;
                }

                randomNumFrames--;
                if (randomNumFrames == 0) {
                    currentExplosives.add(new Explosive(new Point(currentPositionX, currentPositionY)));
                }
            }
        }

        //Render the Airplane
        if (currentDirection == HORIZONTAL_DIRECTION) {
            AIRPLANE.draw(currentPositionX, currentPositionY, rotator.setRotation(HALF_ANGLE_RADIANS));
        } else {
            AIRPLANE.draw(currentPositionX, currentPositionY, rotator.setRotation(FULL_ANGLE_RADIANS));
        }

        //Update all current Explosions
        int i = 0;
        for(Explosive s : currentExplosives) {
            if(s!=null) {
                hasExploded = s.updateExplosion(timescaleMultiplier);
            }
        }
        currentExplosives.removeAll(Collections.singleton(null));

        //Return if the airplane is still valid or not
        return (((!(currentPositionX > Window.getWidth())) && (!(currentPositionX < 0))) && ((!(currentPositionY > Window.getHeight())) && (!(currentPositionY < 0)))) || currentExplosives.size() != 0;
    }

    /**
     * Static Method to remove all explosives that have exploded
     *
     * @param airplanes List of airplanes that are currently on the map
     */
    public static void removeHasExplodedExplosives(List<Airplane> airplanes) {
        for(Airplane s : airplanes) {
            int i = 0;

            for(Explosive t : s.getCurrentExplosives()) {
                if(t.getExplosionBounds() != null) {
                    s.getCurrentExplosives().set(i, null);
                }
                i++;
            }
            s.getCurrentExplosives().removeAll(Collections.singleton(null));
        }
    }

}
