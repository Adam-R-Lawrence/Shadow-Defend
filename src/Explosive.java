import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import static bagel.Drawing.drawRectangle;

/**
 * Class for an explosive that is dropped by the Airplane
 */
public class Explosive {

    private static final Image EXPLOSIVE = new Image("res/images/explosive.png");
    private static final int BLAST_RADIUS = 200;
    private static final int DETONATE_TIMER = 2;
    private final static int EXPLOSIVE_DAMAGE = 500;
    private final static int FRAMES_PER_SECOND = 60;

    private int explosionTimer = 0;
    private final Point pointToDrop;
    private boolean hasExploded = false;
    private Rectangle explosionBounds = null;

    /**
     * Constructor for the Explosive
     *
     * @param pointToDrop Where the explosive is to drop
     */
    public Explosive(Point pointToDrop){
        this.pointToDrop = pointToDrop;
    }

    /**
     * Getter for the Damage done by the explosive
     *
     * @return The Damage done by the explosive
     */
    public int getDamage() {
        return EXPLOSIVE_DAMAGE;
    }

    /**
     * Getter for the radius of the explosion
     *
     * @return The Rectangle indication the bounds of the Explosion
     */
    public Rectangle getExplosionBounds() {
        return explosionBounds;
    }

    /**
     * Method to update the Explosion
     *
     * @param timescaleMultiplier The current Timescale Of the Game
     * @return If the Explosive has exploded or not
     */
    public boolean updateExplosion(int timescaleMultiplier) {

        EXPLOSIVE.draw(pointToDrop.x,pointToDrop.y);

        for(int i = 0; i < timescaleMultiplier; i++) {
            if (hasExploded) {
                return true;
            }
            if (explosionTimer == DETONATE_TIMER * FRAMES_PER_SECOND) {
                //The Explosion has gone off
                explosionBounds = new Rectangle(pointToDrop.x - BLAST_RADIUS, pointToDrop.y - BLAST_RADIUS, 2 * BLAST_RADIUS, 2 * BLAST_RADIUS);
                hasExploded = true;
            }
            explosionTimer++;
        }

        //Explosion has not exploded yet
        return false;
    }
}
