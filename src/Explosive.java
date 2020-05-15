import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import static bagel.Drawing.drawRectangle;

public class Explosive {
    private static final Image EXPLOSIVE = new Image("res/images/explosive.png");
    private static final int BLAST_RADIUS = 200;
    private static final int DETONATE_TIMER = 2;

    private final int damage = 500;
    private int frameCounter;
    private final Point pointToDrop;
    private int delete;

    private  Rectangle explosionBounds;



    public int getDamage() {
        return damage;
    }


    public Rectangle getExplosionBounds() {
        return explosionBounds;
    }

    public Explosive(Point pointToDrop){
        this.pointToDrop = pointToDrop;

        explosionBounds = null;
        frameCounter = 0;
        delete = 0;
    }

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
