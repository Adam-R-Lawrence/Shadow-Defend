import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for the towers which are placed in a single position, they can
 * not move but only shoot and rotate towards enemies
 */
public abstract class ActiveTower {
    private final static int FULL_ANGLE = 360;
    private final static int HALF_ANGLE = FULL_ANGLE/2;
    private final static int QUARTER_ANGLE = FULL_ANGLE/4;

    private final Image towerImage;
    private final Image projectileType;
    private final Point towerPosition;
    private final int damage;
    private final int attackRadius;
    private final int projectileCooldown;
    private final Rectangle towerBoundingBox;
    private final Rectangle rangeBoundingBox;
    private int frameCounter = 0;
    private double currentTankRotation = 0;
    private final List<Projectile> currentProjectiles = new ArrayList<>();

    /**
     *
     * Constructor for the Abstract ActiveTower Class
     *
     * @param towerPosition The position for where the Active Tower was placed
     * @param damage The amount of Damage the calling subclass can do to enemies
     * @param attackRadius The radius of attack the calling subclass can shoot within
     * @param projectileCooldown The time in which the calling subclass needs to wait to shoot again
     * @param towerImage The PNG of the calling subclass
     * @param projectileType The Projectile PNG of the calling subclass
     */
    public ActiveTower(Point towerPosition, int damage, int attackRadius, int projectileCooldown, Image towerImage, Image projectileType) {

        this.damage = damage;
        this.attackRadius = attackRadius;
        this.projectileCooldown = projectileCooldown;
        this.towerPosition = towerPosition;
        this.towerImage = towerImage;
        this.projectileType = projectileType;

        towerBoundingBox = new Rectangle(towerImage.getBoundingBoxAt(towerPosition));
        rangeBoundingBox = new Rectangle(towerPosition.x - attackRadius, towerPosition.y - attackRadius, 2*attackRadius,2*attackRadius);
    }

    /**
     * Getter for the Damage of the Active Tower
     * @return The amount of damage this Active Tower can Do
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Getter for the Active Tower Bounding Boc
     * @return The physical bounds of this Active Tower
     */
    public Rectangle getTowerBoundingBox() {
        return towerBoundingBox;
    }

    /**
     * Getter for the list of the current Projectiles that the Tower is shooting
     * @return The List of Current Projectiles
     */
    public List<Projectile> getCurrentProjectiles() {
        return currentProjectiles;
    }

    /**
     * Update the Active Tower, perform rotations and shooting if enemy is within attack radius
     *
     * @param currentSlicers The List of current Slicers which are present on the map
     * @param timescaleMultiplier The current Timescale Of the Game
     */
    public void updateActiveTower(List<Slicer> currentSlicers, int timescaleMultiplier) {

        double closestDistanceAway = attackRadius, magnitude, angle;
        DrawOptions rotator = new DrawOptions();
        Point whereToShoot = null;

        //Find slicer shortest distance away that is in range
        for(Slicer s: currentSlicers) {
            if(s!= null) {
                if(s.futureMove(timescaleMultiplier) != null) {
                    if (rangeBoundingBox.intersects(s.futureMove(timescaleMultiplier))) {

                        //Find the Magnitude and round it down to the nearest integer.
                        magnitude = Math.sqrt(Math.pow(s.futureMove(timescaleMultiplier).x - towerPosition.x, 2)
                                + Math.pow(s.futureMove(timescaleMultiplier).y - towerPosition.y, 2));

                        if (magnitude < closestDistanceAway) {
                            closestDistanceAway = magnitude;
                            whereToShoot = s.futureMove(timescaleMultiplier);
                        }
                    }
                }
            }
        }

        //Shoot a projectile at that slicer
        for(int i = 0 ; i < timescaleMultiplier; i++) {
            if (frameCounter == 0 && whereToShoot != null) {

                angle = Math.toDegrees(Math.atan2(whereToShoot.y - towerPosition.y, whereToShoot.x - towerPosition.x));
                if (angle < 0) {
                    angle += FULL_ANGLE;
                }

                double rotation = ((angle + QUARTER_ANGLE) * (Math.PI / HALF_ANGLE));

                towerImage.draw(towerPosition.x, towerPosition.y, rotator.setRotation(rotation));

                currentTankRotation = rotation;
                frameCounter = projectileCooldown;
                currentProjectiles.add(new Projectile(towerPosition, angle, projectileType));

            } else if (frameCounter != 0) {
                frameCounter--;
                towerImage.draw(towerPosition.x, towerPosition.y, rotator.setRotation(currentTankRotation));
            } else {
                towerImage.draw(towerPosition.x, towerPosition.y, rotator.setRotation(currentTankRotation));
            }

            //Update all current Projectiles
            for (Projectile s : currentProjectiles) {
                if (s != null) {
                    s.updateProjectile();

                }
            }
        }

        //Draw all the current projectiles
        for (Projectile s : currentProjectiles) {
            if (s != null) {
                s.drawProjectile();
            }
        }
    }

    /**
     * When There are no slicers on the map, no changes to the Tower Occur
     */
    public void updateStationaryActiveTower() {
        DrawOptions rotator = new DrawOptions();
        towerImage.draw(towerPosition.x, towerPosition.y, rotator.setRotation(currentTankRotation));
    }
}
