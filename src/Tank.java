import bagel.Image;
import bagel.util.Point;

/**
 * Subclass of the Active Tower SuperClass
 * This class creates a Tank
 */
public class Tank extends ActiveTower {
    private final static Image TANK_IMAGE = new Image("res/images/tank.png");
    private final static Image TANK_PROJECTILE = new Image("res/images/tank_projectile.png");
    private final static int DAMAGE = 1;
    private final static int ATTACK_RADIUS = 100;
    private final static int PROJECTILE_COOLDOWN = 60;
    private final static int PRICE = 250;

    /**
     * Constructor for the Super Tank
     *
     * @param tankPosition The position where the Tank was Placed on The Map
     * @param player The current player to decrease the cost of purchase from
     */
    public Tank(Point tankPosition, Player player) {
        super(tankPosition,DAMAGE, ATTACK_RADIUS,PROJECTILE_COOLDOWN,TANK_IMAGE, TANK_PROJECTILE);
        player.decreaseMoney(PRICE);

    }
}
