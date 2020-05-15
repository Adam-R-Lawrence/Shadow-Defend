import bagel.Image;
import bagel.util.Point;

public class Tank extends Tower{
    private final static Image TANK_IMAGE = new Image("res/images/tank.png");
    private final static Image TANK_PROJECTILE = new Image("res/images/tank_projectile.png");
    private final static int DAMAGE = 1;
    private final static int ATTACK_RADIUS = 100;
    private final static int PROJECTILE_COOLDOWN = 1000;
    private final static int PROJECTILE_COOLDOWN_FRAMES = 60 * (PROJECTILE_COOLDOWN/1000);
    private final static int PRICE = 250;

    public Tank(Point tankPosition, Player player){
        super(tankPosition,DAMAGE, ATTACK_RADIUS,PROJECTILE_COOLDOWN_FRAMES,TANK_IMAGE, TANK_PROJECTILE);
        player.decreaseMoney(PRICE);

    }
}
