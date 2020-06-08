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
 * Slicer Superclass
 */
public abstract class Slicer {


    private final static int FULL_CIRCLE_ANGLE = 360;
    private final static int EIGHTH_ANGLE = FULL_CIRCLE_ANGLE/8;
    private final static double SIXTEENTH_ANGLE = FULL_CIRCLE_ANGLE/16.0;
    private final static double RADIANS_CONVERTER = Math.PI/180;
    private final static Image SLICER = new Image("res/images/slicer.png");

    private final Image enemyPNG;
    private int polylinePointsPassed;
    private int movementsDone;
    private Point currentPosition;
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
     * Constructor for a slicer
     *
     * @param polyline The Slicer's path through the map
     * @param slicerPNG The calling subclass's Slicer PNG
     * @param health The calling subclass's health
     * @param speed The calling subclass's speed
     * @param reward The calling subclass's reward on death
     * @param penalty The calling subclass's penalty on reaching the end of the polyline
     * @param player The current player playing the game
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

    /**
     * Constructor for a Slicer which has spawned from a dead slicer
     *
     * @param polyline The Slicer's path through the map
     * @param slicerPNG The calling subclass's Slicer PNG
     * @param health The calling subclass's health
     * @param speed The calling subclass's speed
     * @param reward The calling subclass's reward on death
     * @param penalty The calling subclass's penalty on reaching the end of the polyline
     * @param player The current player playing the game
     * @param whereParentDied The position where its' parent died
     * @param movementsParentDid the amount of movements the parent did
     * @param polylinePointsPassed The amount of polylines passed by it's parent
     */
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

    /**
     * Getter for how many movements this slicer has done between two polyline points
     *
     * @return The number of movements
     */
    public int getMovementsDone() {
        return movementsDone;
    }

    /**
     * Getter for where the slicer needs to move next
     *
     * @return The position for where the slicer needs to move
     */
    public Point getWhereToMove() {
        return whereToMove ;
    }

    /**
     * Getter for the Enemy Type of this particular slicer
     *
     * @return The subclass's enemy type
     */
    public String getEnemyType() {
        return enemyType;
    }

    /**
     * Getter for how many polylines points this slicer has passed
     *
     * @return the number of points passed
     */
    public int getPolylinePointsPassed() {
        return polylinePointsPassed;
    }

    /**
     * This moves the Slicer into its next position in the polyline
     *
     * @param timescaleMultiplier The current timescale of the game
     * @param enemyNumber The unique ID for the slicer out of all current slicers
     * @param numberOfEnemiesInWave How many enemies are currently in the wave
     * @param tanks List of all the current Active Towers
     * @param airplanes List of all the current Airplanes
     * @return the status of the slicer
     */
    public int nextMove(int timescaleMultiplier, int enemyNumber, int numberOfEnemiesInWave, List<ActiveTower> tanks, List<Airplane> airplanes){

        Point currentPolylinePoint;
        Point nextPolylinePoint;
        double xDistanceApart, yDistanceApart;
        double xMovementPerFrame, yMovementPerFrame;


        //Find the next place to move along the polyline, maximum of 1px
        for(int i = 0; i < timescaleMultiplier; i++) {
            if(polyline.size() != polylinePointsPassed) {
                nextPolylinePoint = polyline.get(polylinePointsPassed);
                currentPolylinePoint = polyline.get(polylinePointsPassed - 1);

                //Find the Magnitude and round it down to the nearest integer.
                double magnitude = Math.sqrt(Math.pow(nextPolylinePoint.x - currentPolylinePoint.x, 2)
                        + Math.pow(nextPolylinePoint.y - currentPolylinePoint.y, 2));
                int numberOfStepsNeeded = (int) ((Math.floor(magnitude)) / speed);

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

    /**
     * Return the future move of the slicer
     * (To be used for the Active Tower's Shooting Algorithm)
     *
     * @param timescaleMultiplier The current time scale of the game
     * @return The point where the slicer will move to in the future
     */
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
        for (int i = 0; i < timescaleMultiplier; i++) {
            if (polyline.size() != tempPolyLinesPassed) {
                nextPolylinePoint = polyline.get(tempPolyLinesPassed);
                currentPolylinePoint = polyline.get(tempPolyLinesPassed - 1);

                //Find the Magnitude and round it down to the nearest integer.
                double magnitude = Math.sqrt(Math.pow(nextPolylinePoint.x - currentPolylinePoint.x, 2)
                        + Math.pow(nextPolylinePoint.y - currentPolylinePoint.y, 2));
                int numberOfStepsNeeded = (int) ((Math.floor(magnitude)) / speed);

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


    /**
     *
     * Rotate the Slicer based on which direction it is moving and draw it on the frame
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

    /**
     * Check if the slicer has been hit by an projectile or explosive
     *
     * @param currentTanks List of the current Tanks
     * @param currentAirplanes List of the current Airplanes
     * @return The Status of the slicer
     */
    public int checkIfHit(List<ActiveTower> currentTanks, List<Airplane> currentAirplanes){

        List<Projectile> toRemove = new ArrayList<>();

        for(ActiveTower s : currentTanks){

            for(Projectile t : s.getCurrentProjectiles()){

                if(boundingBox.intersects(t.getProjectileBoundingBox().centre())){

                    health = health - s.getDamage();

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
