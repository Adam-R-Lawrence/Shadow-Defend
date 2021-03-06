import bagel.Image;
import bagel.util.Point;
import java.util.List;

/**
 * @author arlawrence
 *
 * Subclass of Slicer Superclass
 * This creates the Apex Slicer Enemy
 *
 */
public class ApexSlicer extends Slicer {

    private final static Image SLICER_PNG = new Image("res/images/apexslicer.png");
    private final static int HEALTH = 25;
    private final static double SPEED = 0.75;
    private final static int REWARD = 150;
    private final static int PENALTY = 16;

    /**
     * Constructor for an Apex Slicer
     *
     * @param polyline The polyline for the slicer to follow around the map
     * @param player The current player
     */
    public ApexSlicer(List<Point> polyline, Player player) {
        //Set the slicer position to its starting point
        super(polyline,SLICER_PNG, HEALTH, SPEED, REWARD, PENALTY, player);
    }
}
