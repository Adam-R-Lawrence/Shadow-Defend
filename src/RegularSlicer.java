import bagel.Image;
import bagel.util.Point;
import java.util.List;

/**
 * @author arlawrence
 *
 * Subclass of Slicer Superclass
 * This creates the Regular Slicer Enemy
 *
 */
public class RegularSlicer extends Slicer {

    private final static Image SLICER_PNG = new Image("res/images/slicer.png");
    private final static int HEALTH = 1;
    private final static double SPEED = 2;
    private final static int REWARD = 2;
    private final static int PENALTY = 1;
    private final static double SPEED_FACTOR_OF_PARENT = 1.5;

    /**
     * Constructor for an Regular Slicer
     *
     * @param polyline The polyline for the slicer to follow around the map
     * @param player The current player
     */
    public RegularSlicer(List<Point> polyline, Player player) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player);
    }

    /**
     * Constructor for an Regular Slicer when it has spawned from a Super Slicer
     *
     * @param polyline The polyline for the slicer to follow around the map
     * @param player The current player
     * @param whereParentDied The position where its' parent died
     * @param movementsParentDid the amount of movements the parent did
     * @param polylinePointsPassed The amount of polylines passed by it's parent
     */
    public RegularSlicer(List<Point> polyline, Player player , Point whereParentDied, int movementsParentDid, int polylinePointsPassed) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player, whereParentDied, (int) (movementsParentDid * (SPEED_FACTOR_OF_PARENT /SPEED)),polylinePointsPassed);
    }
}
