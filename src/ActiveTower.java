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
    private final Image towerImage;
    private final Image projectileType;
    private final Point tankPosition;
    private final int damage;
    private final int attackRadius;
    private final int projectileCooldown;
    private final Rectangle tankBoundingBox;
    private final Rectangle rangeBoundingBox;
    private int frameCounter;
    private double currentTankRotation;
    private final List<Projectile> currentProjectiles;

    /**
     *
     * Constructor for the Abstract ActiveTower Class
     *
     * @param tankPosition The position for where the Active Tower was placed
     * @param damage The amount of Damage the calling subclass can do to enemies
     * @param attackRadius The radius of attack the calling subclass can shoot within
     * @param projectileCooldown The time in which the calling subclass needs to wait to shoot again
     * @param towerImage The PNG of the calling subclass
     * @param projectileType The Projectile PNG of the calling subclass
     */
    public ActiveTower(Point tankPosition, int damage, int attackRadius, int projectileCooldown, Image towerImage, Image projectileType){

        this.damage = damage;
        this.attackRadius = attackRadius;
        this.projectileCooldown = projectileCooldown;
        this.tankPosition = tankPosition;
        this.towerImage = towerImage;
        this.projectileType = projectileType;

        tankBoundingBox = new Rectangle(towerImage.getBoundingBoxAt(tankPosition));
        rangeBoundingBox = new Rectangle(tankPosition.x - attackRadius, tankPosition.y - attackRadius, 2*attackRadius,2*attackRadius);

        frameCounter = 0;

        currentProjectiles = new ArrayList<>();
        currentTankRotation = 0;
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
    public Rectangle getTankBoundingBox() {
        return tankBoundingBox;
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
    public void updateTank(List<Slicer> currentSlicers, int timescaleMultiplier) {

        //Find slicer shortest distance away that is in range
        DrawOptions rotator = new DrawOptions();
        Point whereToAttack = null;
        double closestDistanceAway = attackRadius, magnitude, angle;
        for(Slicer s: currentSlicers) {
            if(s!= null) {
                if(s.futureMove(timescaleMultiplier) != null) {
                    if (rangeBoundingBox.intersects(s.futureMove(timescaleMultiplier))) {


                        //Find the Magnitude and round it down to the nearest integer.
                        magnitude = Math.sqrt(Math.pow(s.futureMove(timescaleMultiplier).x - tankPosition.x, 2)
                                + Math.pow(s.futureMove(timescaleMultiplier).y - tankPosition.y, 2));


                        if (magnitude < closestDistanceAway) {
                            closestDistanceAway = magnitude;
                            whereToAttack = s.futureMove(timescaleMultiplier);
                        }
                    }
                }
            }
        }

        //Attack
        for(int i = 0 ; i < timescaleMultiplier; i++) {
            if (frameCounter == 0 && whereToAttack != null) {

                angle = Math.toDegrees(Math.atan2(whereToAttack.y - tankPosition.y, whereToAttack.x - tankPosition.x));
                if (angle < 0) {
                    angle += 360;
                }

                double rotation = ((angle + 90) * (Math.PI / 180.0));

                towerImage.draw(tankPosition.x, tankPosition.y, rotator.setRotation(rotation));

                currentTankRotation = rotation;
                frameCounter = projectileCooldown;
                currentProjectiles.add(new Projectile(tankPosition, angle, projectileType));

            } else if (frameCounter != 0) {
                frameCounter--;
                towerImage.draw(tankPosition.x, tankPosition.y, rotator.setRotation(currentTankRotation));
            } else {
                towerImage.draw(tankPosition.x, tankPosition.y, rotator.setRotation(currentTankRotation));
            }

            for (Projectile s : currentProjectiles) {
                if (s != null) {
                    s.updateProjectile();
                }
            }
        }

        for (Projectile s : currentProjectiles) {
            if (s != null) {
                s.drawProjectile();
            }
        }
    }

    /**
     * When There are no slicers on the map, no changes to the Tower Occur
     */
    public void updateStationaryTank() {
        DrawOptions rotator = new DrawOptions();
        towerImage.draw(tankPosition.x,tankPosition.y, rotator.setRotation(currentTankRotation));
    }
}
