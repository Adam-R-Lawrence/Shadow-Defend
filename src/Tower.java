import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class Tower{
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

    private List<Projectile> currentProjectiles;


    public Tower(Point tankPosition, int damage, int attackRadius, int projectileCooldown, Image towerImage, Image projectileType){

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

    public int getDamage() {
        return damage;
    }


    public void updateTank(List<Slicer> currentSlicers, int timescaleMultiplier){

        //drawRectangle(tankPosition.x - ATTACK_RADIUS, tankPosition.y - ATTACK_RADIUS, 2*ATTACK_RADIUS,2*ATTACK_RADIUS, Colour.WHITE);

        //Find slicer shortest distance away that is in range
        DrawOptions rotator = new DrawOptions();
        Point whereToAttack = null;
        double closestDistanceAway = attackRadius,magnitude,angle;
        for(Slicer s: currentSlicers){
            if(s!= null) {
                if(s.getWhereToMove() != null) {
                    if (rangeBoundingBox.intersects(s.getWhereToMove())) {
                        //Find the Magnitude and round it down to the nearest integer.
                        magnitude = Math.sqrt(Math.pow(s.getWhereToMove().x - tankPosition.x, 2)
                                + Math.pow(s.getWhereToMove().y - tankPosition.y, 2));


                        if (magnitude < closestDistanceAway) {
                            closestDistanceAway = magnitude;
                            whereToAttack = s.getWhereToMove();
                        }
                    }
                }
            }
        }




        //Attack
        for(int i = 0 ; i < timescaleMultiplier;i++) {
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

    public void updateTank() {
        DrawOptions rotator = new DrawOptions();
        towerImage.draw(tankPosition.x,tankPosition.y, rotator.setRotation(currentTankRotation));
    }

    public List<Projectile> getCurrentProjectiles() {
        return currentProjectiles;
    }


}
