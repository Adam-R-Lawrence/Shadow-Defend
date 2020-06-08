import bagel.Image;
import bagel.util.Point;
import java.util.List;

/**
 * @author arlawrence
 *
 * Subclass of Slicer Superclass
 * This creates the Mega Slicer Enemy
 *
 */
public class MegaSlicer extends Slicer {

    private final static Image SLICER_PNG = new Image("res/images/megaslicer.png");
    private final static int HEALTH = 2;
    private final static double SPEED = 1.5;
    private final static int REWARD = 10;
    private final static int PENALTY = 4;
    private final static int NUMBER_SLICERS_SPAWNED_ON_DEATH = 2;
    private final static int SLICER_HIERARCHY = 3;

    /**
     * Constructor for an Mega Slicer
     *
     * @param polyline The polyline for the slicer to follow around the map
     * @param player The current player
     */
    public MegaSlicer(List<Point> polyline, Player player) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player);
    }

    /**
     * Constructor for an Mega Slicer when it has spawned from a Apex Slicer
     *
     * @param polyline The polyline for the slicer to follow around the map
     * @param player The current player
     * @param whereParentDied The position where its' parent died
     * @param movementsParentDid the amount of movements the parent did
     * @param polylinePointsPassed The amount of polylines passed by it's parent
     */
    public MegaSlicer(List<Point> polyline, Player player , Point whereParentDied, int movementsParentDid, int polylinePointsPassed) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player, whereParentDied,(int) (movementsParentDid * (0.75/SPEED)),polylinePointsPassed);
    }
}
