import bagel.Image;
import bagel.util.Point;

public class SuperTank extends Tower{
    private final static Image TANK_IMAGE = new Image("res/images/supertank.png");
    private final static Image SUPER_TANK_PROJECTILE = new Image("res/images/supertank_projectile.png");
    private final static int DAMAGE = 3;
    private final static int ATTACK_RADIUS = 150;
    private final static int PROJECTILE_COOLDOWN = 500;
    private final static int PROJECTILE_COOLDOWN_FRAMES = (int) (60 * (PROJECTILE_COOLDOWN/1000.0));
    private final static int PRICE = 600;

    public SuperTank(Point tankPosition, Player player){
        super(tankPosition,DAMAGE, ATTACK_RADIUS,PROJECTILE_COOLDOWN_FRAMES,TANK_IMAGE, SUPER_TANK_PROJECTILE);
        player.decreaseMoney(PRICE);

    }
}
