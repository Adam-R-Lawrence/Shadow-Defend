import bagel.Image;
import bagel.util.Colour;
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

    private int frameCounter;
    private final Point pointToDrop;
    private int delete;
    private Rectangle explosionBounds;

    /**
     * Constructor for the Explosive
     *
     * @param pointToDrop Where the explosive is to drop
     */
    public Explosive(Point pointToDrop){
        this.pointToDrop = pointToDrop;

        explosionBounds = null;
        frameCounter = 0;
        delete = 0;
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
     * @return If the Explosive has been detonated or not
     */
    public int updateExplosion(int timescaleMultiplier){

        EXPLOSIVE.draw(pointToDrop.x,pointToDrop.y);

        for(int i = 0; i< timescaleMultiplier; i++) {
            if (delete == 1) {
                return 1;
            }
            if (frameCounter == DETONATE_TIMER * 60) {
                explosionBounds = new Rectangle(pointToDrop.x - BLAST_RADIUS, pointToDrop.y - BLAST_RADIUS, 2 * BLAST_RADIUS, 2 * BLAST_RADIUS);

                drawRectangle(pointToDrop.x - 200, pointToDrop.y - 200, 400, 400, Colour.WHITE);


                //return 1;
                delete = 1;
            }

            frameCounter++;
        }
        return 0;
    }
}
