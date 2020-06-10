import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.List;

/**
 * Class for the projectile that the active towers shoot
 */
public class Projectile {

    private final static int PROJECTILE_SPEED = 10;

    private final Image projectileType;
    private double projectilePositionX;
    private double projectilePositionY;
    private final double angleToShootAt;
    private Rectangle projectileBoundingBox;
    private Point currentPosition;

    /**
     * Constructor for the Projectile
     *
     * @param projectileSpawnPoint The Position of the Active Tower which is shooting
     * @param angleToShootAt The angle that the slicer is relevant to the Active Tower
     * @param projectileType The Type of Projectile which belongs to that specific Active Tower
     */
    public Projectile(Point projectileSpawnPoint, double angleToShootAt, Image projectileType) {
        projectilePositionX = projectileSpawnPoint.x;
        projectilePositionY = projectileSpawnPoint.y;
        this.angleToShootAt = angleToShootAt;
        this.projectileType = projectileType;

        currentPosition = new Point(projectilePositionX,projectilePositionY);
        projectileBoundingBox = (projectileType.getBoundingBoxAt(currentPosition));
    }

    /**
     * A getter for the bounding box of the projectile
     *
     * @return The Rectangle of the projectile
     */
    public Rectangle getProjectileBoundingBox() {
        return projectileBoundingBox;
    }

    /**
     * Method to update the projectile as it moves towards a slicer
     */
    public void updateProjectile() {

        projectilePositionX = (int) ((PROJECTILE_SPEED * Math.cos(Math.toRadians(angleToShootAt))) + projectilePositionX);
        projectilePositionY = (int) ((PROJECTILE_SPEED * Math.sin(Math.toRadians(angleToShootAt))) + projectilePositionY);

        currentPosition = new Point(projectilePositionX,projectilePositionY);
        projectileBoundingBox = (projectileType.getBoundingBoxAt(currentPosition));

    }

    /**
     * Method to draw the projectile
     */
    public void drawProjectile(){
        projectileType.draw(projectilePositionX,projectilePositionY);
    }
}
