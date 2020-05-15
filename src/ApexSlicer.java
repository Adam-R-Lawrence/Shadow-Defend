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
public class ApexSlicer extends Slicer {

    private final static Image SLICER_PNG = new Image("res/images/apexslicer.png");
    private final static int HEALTH = 25;
    //Fix this
    private final static double SPEED = 1;
    private final static int REWARD = 150;
    private final static int PENALTY = 16;
    private final static int NUMBER_SLICERS_SPAWNED_ON_DEATH = 4;
    private final static int SLICER_HIERARCHY = 4;

    public ApexSlicer(List<Point> polyline, Player player) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player);
    }

}
