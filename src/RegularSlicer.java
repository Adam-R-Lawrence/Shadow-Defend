import bagel.Image;
import bagel.util.Point;
import java.util.List;

/**
 * @author arlawrence
 *
 * Subclass of Enemy Superclass
 * This creates a Slicer Enemy
 *
 */
public class RegularSlicer extends Slicer {

    private final static Image SLICER_PNG = new Image("res/images/slicer.png");
    private final static int HEALTH = 1;
    private final static double SPEED = 2;
    private final static int REWARD = 2;
    private final static int PENALTY = 1;
    private final static int NUMBER_SLICERS_SPAWNED_ON_DEATH = 0;
    private final static int SLICER_HIERARCHY = 1;

    public RegularSlicer(List<Point> polyline, Player player) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player);
    }

    public RegularSlicer(List<Point> polyline, Player player , Point whereParentDied, int movementsParentDid, int polylinePointsPassed) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player, whereParentDied,movementsParentDid,polylinePointsPassed);
    }
}
