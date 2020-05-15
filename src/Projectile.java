import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Projectile {

    private final Image projectileType;
    private final static int PROJECTILE_SPEED = 10;
    private double projectilePositionX;
    private double projectilePositionY;
    private final double angleToShootAt;


    private Point currentPosition;

    public Projectile(Point projectileSpawnPoint, double angleToShootAt, Image projectileType){
        projectilePositionX = projectileSpawnPoint.x;
        projectilePositionY = projectileSpawnPoint.y;
        this.angleToShootAt = angleToShootAt;
        this.projectileType = projectileType;

        currentPosition = new Point(projectilePositionX,projectilePositionY);

    }


    public void updateProjectile(){


        projectilePositionX = (int) ((PROJECTILE_SPEED * Math.cos(Math.toRadians(angleToShootAt))) + projectilePositionX);
        projectilePositionY = (int) ((PROJECTILE_SPEED * Math.sin(Math.toRadians(angleToShootAt))) + projectilePositionY);




        currentPosition = new Point(projectilePositionX,projectilePositionY);
    }

    public void drawProjectile(){
        projectileType.draw(projectilePositionX,projectilePositionY);

    }

    public Point getCurrentPosition() {
        return currentPosition;
    }
}
