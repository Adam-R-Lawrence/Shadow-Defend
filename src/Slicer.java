import bagel.Image;
import bagel.util.Point;
import bagel.DrawOptions;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author arlawrence
 *
 * Enemy Superclass
 *
 */
public abstract class Slicer {

    private final Image enemyPNG;
    private int polylinePointsPassed;
    private int movementsDone;
    private final static int FULL_CIRCLE_ANGLE = 360;
    private final static int EIGHTH_ANGLE = FULL_CIRCLE_ANGLE/8;
    private final static double SIXTEENTH_ANGLE = FULL_CIRCLE_ANGLE/16.0;
    private final static double RADIANS_CONVERTER = Math.PI/180;


    private Point currentPosition;
    private final static Image SLICER = new Image("res/images/slicer.png");

    private Point whereToMove;

    private int health;
    private final double speed;
    private final int reward;
    private final int penalty;
    private Rectangle boundingBox;
    private final Player player;
    private final String enemyType;

    List<Point> polyline;


    /**
     * Enemy Constructor
     *
     * @param polyline The Map's polyline
     */
    protected Slicer(List<Point> polyline, Image slicerPNG, int health, double speed, int reward, int penalty, Player player) {
        //Set the Enemy position to its starting point
        this.health = health;
        this.speed = speed;
        this.reward = reward;
        this.penalty = penalty;
        this.enemyPNG = slicerPNG;
        this.polyline = polyline;
        this.player = player;

        currentPosition = polyline.get(0);

        movementsDone = 0;
        polylinePointsPassed = 1;

        enemyType = getClass().getName();
    }

    protected Slicer(List<Point> polyline, Image slicerPNG, int health, double speed, int reward, int penalty, Player player, Point whereParentDied, int movementsParentDid, int polylinePointsPassed) {
        //Set the Enemy position to its starting point
        this.health = health;
        this.speed = speed;
        this.reward = reward;
        this.penalty = penalty;
        this.enemyPNG = slicerPNG;
        this.polyline = polyline;
        this.player = player;
        whereToMove = null;


        currentPosition = whereParentDied;
        movementsDone = movementsParentDid;
        this.polylinePointsPassed = polylinePointsPassed;
        enemyType = getClass().getName();

    }


    public int getMovementsDone() {
        return movementsDone;
    }


    public Point getWhereToMove() {
        return whereToMove;
    }

    public String getEnemyType() {
        return enemyType;
    }


    /**
     *
     * This moves the Enemy into its next position in the polyline
     *
     * @param timescaleMultiplier The current time multiplayer of the game
     * @param enemyNumber The identity of the current enemy being moved
     */
    public int nextMove( int timescaleMultiplier, int enemyNumber, int numberOfEnemiesInWave, List<Tower> tanks, List<Airplane> airplanes){

        Point currentPolylinePoint;
        Point nextPolylinePoint;
        double xDistanceApart, yDistanceApart;
        double xMovementPerFrame, yMovementPerFrame;


        //Find the next place to move along the polyline, maximum of 1px
        for(int i = 0; i < timescaleMultiplier * speed; i++) {
            if(polyline.size() != polylinePointsPassed) {
                nextPolylinePoint = polyline.get(polylinePointsPassed);
                currentPolylinePoint = polyline.get(polylinePointsPassed - 1);

                //Find the Magnitude and round it down to the nearest integer.
                double magnitude = Math.sqrt(Math.pow(nextPolylinePoint.x - currentPolylinePoint.x, 2)
                        + Math.pow(nextPolylinePoint.y - currentPolylinePoint.y, 2));
                int numberOfStepsNeeded = (int) ((Math.floor(magnitude)));

                if (movementsDone != numberOfStepsNeeded) {
                    xDistanceApart = nextPolylinePoint.x - currentPolylinePoint.x;
                    yDistanceApart = nextPolylinePoint.y - currentPolylinePoint.y;

                    xMovementPerFrame = xDistanceApart / numberOfStepsNeeded;
                    yMovementPerFrame = yDistanceApart / numberOfStepsNeeded;

                    whereToMove = new Point(currentPosition.x + xMovementPerFrame,
                            currentPosition.y + yMovementPerFrame);


                    movementsDone++;
                    currentPosition = whereToMove;

                } else {
                    whereToMove = nextPolylinePoint;
                    polylinePointsPassed++;
                    movementsDone = 0;
                }




                if (i == (timescaleMultiplier - 1)) {
                    if (polyline.size() != polylinePointsPassed) {
                        rotateAndDraw(whereToMove);
                    }
                }
                boundingBox = new Rectangle(SLICER.getBoundingBoxAt(currentPosition));

                if(checkIfHit(tanks,airplanes) == 3){
                    return 3;
                }


            } else {

                player.decreaseLives(penalty);
                //If all enemies have reached the end of the polyline, close the game
                if (enemyNumber == numberOfEnemiesInWave - 1){
                    return 1;
                }
                return 2;
            }
        }
        return 0;
    }


    public Point futureMove( int timescaleMultiplier) {
        Point currentPolylinePoint;
        Point nextPolylinePoint;
        double xDistanceApart, yDistanceApart;
        double xMovementPerFrame, yMovementPerFrame;

        int futureMovementsDone = movementsDone;
        Point futureMovement = null;
        int tempPolyLinesPassed = polylinePointsPassed;
        Point futurePosition = currentPosition;

        //Find the next place to move along the polyline, maximum of 1px
        for (int i = 0; i < timescaleMultiplier * speed; i++) {
            if (polyline.size() != tempPolyLinesPassed) {
                nextPolylinePoint = polyline.get(tempPolyLinesPassed);
                currentPolylinePoint = polyline.get(tempPolyLinesPassed - 1);

                //Find the Magnitude and round it down to the nearest integer.
                double magnitude = Math.sqrt(Math.pow(nextPolylinePoint.x - currentPolylinePoint.x, 2)
                        + Math.pow(nextPolylinePoint.y - currentPolylinePoint.y, 2));
                int numberOfStepsNeeded = (int) ((Math.floor(magnitude)));

                if (futureMovementsDone != numberOfStepsNeeded) {
                    xDistanceApart = nextPolylinePoint.x - currentPolylinePoint.x;
                    yDistanceApart = nextPolylinePoint.y - currentPolylinePoint.y;

                    xMovementPerFrame = xDistanceApart / numberOfStepsNeeded;
                    yMovementPerFrame = yDistanceApart / numberOfStepsNeeded;

                    futureMovement = new Point(futurePosition.x + xMovementPerFrame,
                            futurePosition.y + yMovementPerFrame);


                    futureMovementsDone++;
                    futurePosition = futureMovement;

                } else {
                    futureMovement = nextPolylinePoint;
                    tempPolyLinesPassed++;
                    futureMovementsDone = 0;
                }
            }
        }
        return futureMovement;
    }

    public int getPolylinePointsPassed() {
        return polylinePointsPassed;
    }

    /**
     *
     * Rotate the Enemy based on which direction it is moving and draw it on the frame
     *
     * @param whereToMove The point on the tilemap where the Enemy should move to
     */
    private void rotateAndDraw(Point whereToMove){
        Point currentPolylinePoint = polyline.get(polylinePointsPassed - 1);
        Point nextPolylinePoint = polyline.get(polylinePointsPassed);

        double upperBound, lowerBound;

        //Calculate the displacement
        double displacementX = nextPolylinePoint.x-currentPolylinePoint.x;
        double displacementY = nextPolylinePoint.y-currentPolylinePoint.y;

        //Find the angle between the last polyline point and the next polyline point
        double angle = Math.toDegrees(Math.atan2(displacementY, displacementX));
        angle = Math.abs((angle + Math.ceil( -angle / FULL_CIRCLE_ANGLE )
                * FULL_CIRCLE_ANGLE) - FULL_CIRCLE_ANGLE);


        /*
        * The rotator logic works by splitting up a full circle into 8 distinct
        * areas that have a range of 45 degrees. This creates 8 rotation
        * possibilities, based on the angle that was calculated above.
        */
        DrawOptions rotator = new DrawOptions();

        if (angle < SIXTEENTH_ANGLE || angle > FULL_CIRCLE_ANGLE - SIXTEENTH_ANGLE) {
            enemyPNG.draw(whereToMove.x,whereToMove.y);
        }
        for(int i = 0; i < FULL_CIRCLE_ANGLE; i = i + EIGHTH_ANGLE) {
            upperBound = i + (SIXTEENTH_ANGLE);
            if (i - SIXTEENTH_ANGLE < 0) {
                lowerBound = FULL_CIRCLE_ANGLE - SIXTEENTH_ANGLE;
            } else {
                lowerBound = i - SIXTEENTH_ANGLE;
            }

            //If the angle belongs in this area, rotate it accordingly and draw it on the map
            if (angle < upperBound && angle > lowerBound) {
                enemyPNG.draw(whereToMove.x,whereToMove.y,
                        rotator.setRotation(FULL_CIRCLE_ANGLE
                        * RADIANS_CONVERTER- (i * RADIANS_CONVERTER)));
                break;
            }
        }
    }


    public int checkIfHit(List<Tower> currentTanks, List<Airplane> currentAirplanes){


        List<Projectile> toRemove = new ArrayList<>();

        for(Tower s : currentTanks){

            for(Projectile t : s.getCurrentProjectiles()){

                if(boundingBox.intersects(t.getProjectileBoundingBox().centre())){

                    health = health - s.getDamage();
                    System.out.println(health);

                    toRemove.add(t);
                    //s.getCurrentProjectiles().set(j,null);

                    s.getCurrentProjectiles().removeAll(Collections.singleton(null));
                    if(health <= 0){
                        player.increaseMoney(reward);
                        s.getCurrentProjectiles().removeAll(toRemove);
                        return 3;

                    }
                }
            }
            s.getCurrentProjectiles().removeAll(toRemove);
        }


        for(Airplane s : currentAirplanes){
            for(Explosive t : s.getCurrentExplosives()){
                if(t.getExplosionBounds() != null) {
                    if (t.getExplosionBounds().intersects(currentPosition)) {

                        health = health - t.getDamage();
                        System.out.println(health);


                        if (health <= 0) {
                            player.increaseMoney(reward);
                            return 3;
                        }

                    }
                }
            }
        }
        return 0;
    }
}
