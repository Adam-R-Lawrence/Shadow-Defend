import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for the Shadow Defend Game
 */
public class ShadowDefend extends AbstractGame {

    //Constants
    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final String TITLE = "ShadowDefend";
    private static final String WAVE_EVENT_FILE = "res/levels/waves.txt";
    private static final String WINNER_STATUS = "Winner!";
    private static final String PLACING_STATUS = "Placing";
    private static final String WAVE_IN_PROGRESS_STATUS = "Wave In Progress";
    private static final String AWAITING_START_STATUS = "Awaiting Start";
    private final static int WAVE_EVENT_FINISHED_SPAWNING = 1;
    private final static int ALL_SLICERS_DESPAWNED = 2;
    private final static int SPAWN_EVENT_NUMBER_OF_PARAMETERS = 5;
    private final static int DEFAULT_MULTIPLIER = 1;

    //instance variables
    private TiledMap gameMap;
    private final BuyPanel buyPanel = new BuyPanel();
    private final StatusPanel statusPanel = new StatusPanel();
    private Player player = new Player();
    private List<Airplane> airplanes = new ArrayList<>();
    private List<ActiveTower> tanks = new ArrayList<>();
    private final List<WaveEvent> waveEvents;
    private final List<Slicer>  currentEnemies = new ArrayList<>();
    private int timescaleMultiplier;
    private int waveNumber;
    private int passedWaveEvents;
    private boolean isEnemyWave;
    private String towerBeingPlaced= "NO TOWER SELECTED";
    private WaveEvent currentWaveEvent;
    private EnemyWave currentWave;
    private String currentStatus;
    private int currentLevelNumber = 0;


    /**
     * Constructor for the Shadow Defend Game
     */
    public ShadowDefend() {
        super(WIDTH, HEIGHT, TITLE);

        // This stops the visual glitch from occurring when pressing 'S'
        new Image("res/images/slicer.png");
        ///////////////////////////////////////////////////////////////
        //Set the timescale multiplier to the default value of 1

        //Fill out all the Wave Events
        ArrayList<String> listOfWaveEvents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(WAVE_EVENT_FILE)))
        {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                listOfWaveEvents.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        waveEvents = Arrays.asList(new WaveEvent[listOfWaveEvents.size()]);

        int i = 0;
        for(String s:listOfWaveEvents){

            String[] waveEventBreakdown = s.split(",");

            if(waveEventBreakdown.length == SPAWN_EVENT_NUMBER_OF_PARAMETERS){

                waveEvents.set(i, new WaveEvent(Integer.parseInt(waveEventBreakdown[0]), Integer.parseInt(waveEventBreakdown[2]), waveEventBreakdown[3], Integer.parseInt(waveEventBreakdown[4])));

            }else{
                waveEvents.set(i, new WaveEvent(Integer.parseInt(waveEventBreakdown[0]), Integer.parseInt(waveEventBreakdown[2])));
            }

            i++;
        }

        //Start the first level
        nextLevel();
    }

    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    /**
     *
     * Updates the game state approximately 60 times a second, potentially reading from input.
     *
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    @Override
    protected void update(Input input) {
        gameMap.draw(0, 0, 0, 0, Window.getWidth(), Window.getHeight());
        Point currentMousePosition = input.getMousePosition();
        boolean wasLeftKeyPressed = input.wasPressed(MouseButtons.LEFT);
        boolean wasRightKeyPressed = input.wasPressed(MouseButtons.RIGHT);

        //Determine the Game Status (Based on a hierarchy)
        if(!currentStatus.equals(WINNER_STATUS)) {
            if (isEnemyWave) {
                currentStatus = WAVE_IN_PROGRESS_STATUS;
            } else {
                currentStatus = AWAITING_START_STATUS;
            }
        }

        //Check if the mouse is over a invalid position for placing a tower
        boolean isMouseOverAnInvalidPosition = false;
        for(ActiveTower s: tanks){
            if(s.getTowerBoundingBox().intersects(currentMousePosition)){
                isMouseOverAnInvalidPosition = true;
                break;
            }
        }
        if(BuyPanel.getBuyPanelBoundingBox().intersects(currentMousePosition)){
            isMouseOverAnInvalidPosition = true;
        }
        if(StatusPanel.getStatusPanelBoundingBox().intersects(currentMousePosition)){
            isMouseOverAnInvalidPosition = true;
        }



        //Out Of Bounds Check
        int fixedPositionX = (int) currentMousePosition.x;
        int fixedPositionY = (int) currentMousePosition.y;
        if(currentMousePosition.x >= WIDTH){
            fixedPositionX = WIDTH - 1;
        }
        if(currentMousePosition.y >= HEIGHT){
            fixedPositionY = HEIGHT - 1;

        }
        //Update the placing tower mechanism
        if(!isMouseOverAnInvalidPosition && !towerBeingPlaced.equals("NO TOWER SELECTED")&& !gameMap.hasProperty( fixedPositionX, fixedPositionY,"blocked")){
            towerBeingPlaced = buyPanel.placeTower(towerBeingPlaced, currentMousePosition, wasLeftKeyPressed, wasRightKeyPressed, tanks, airplanes, player);
        }


        //Update the Buy Panel (Return the tower selected)
        String towerSelected = buyPanel.updateBuyPanel(player, currentMousePosition, wasLeftKeyPressed);

        //Update the status a tower was selected to be bought
        if(towerBeingPlaced.equals("NO TOWER SELECTED") && !towerSelected.equals("NO TOWER SELECTED")){
            towerBeingPlaced = towerSelected;
        }
        if(!towerBeingPlaced.equals("NO TOWER SELECTED")){
            currentStatus = PLACING_STATUS;
        }

        //If 'S' is pressed start a new enemy wave
        if (input.wasPressed(Keys.S) && !isEnemyWave) {
            currentWave = new EnemyWave();
            if (currentWaveEvent.getEventType().equals("SPAWN")) {

                currentWave.addWaveEvent(currentWaveEvent);
            } else {
                currentWave.addWaveEvent(currentWaveEvent.getSpawnDelay(), timescaleMultiplier);
            }
            isEnemyWave = true;
            waveNumber = currentWaveEvent.getWaveNumber();
        }

        //If 'L' is pressed increase the Timescale by 1
        if (input.wasPressed(Keys.L)) {
            timescaleMultiplier++;
        }

        //If 'K' is pressed decrease the Timescale by 1, but the Timescale cannot be less than 1
        if (input.wasPressed(Keys.K)) {
            if (timescaleMultiplier != DEFAULT_MULTIPLIER) {
                timescaleMultiplier--;
            }
        }

        //Update all Airplanes, Remove them if they have flown off the screen and all explosions have exploded
        int i = 0;
        for(Airplane s : airplanes){
            if(!s.updateAirplane(timescaleMultiplier)){
                airplanes.set(i, null);
            }
            i++;
        }
        airplanes.removeAll(Collections.singleton(null));

        //If there is a wave ongoing, update the contents of that wave
        if(currentWave != null){


            int waveStatus = currentWave.nextWaveFrame(gameMap, timescaleMultiplier, player, tanks, airplanes, currentWaveEvent,currentEnemies);

            if(passedWaveEvents == waveEvents.size() || (waveStatus == ALL_SLICERS_DESPAWNED && waveEvents.size() == 1) ){

                if(waveStatus == ALL_SLICERS_DESPAWNED){
                    passedWaveEvents++;
                    currentWave = null;
                    waveNumber++;
                }
            }
            else if(waveStatus == WAVE_EVENT_FINISHED_SPAWNING || (waveStatus == ALL_SLICERS_DESPAWNED && waveEvents.get(passedWaveEvents).getWaveNumber() == waveNumber)) {
                passedWaveEvents++;

                //Spawn in the next Wave Event of this Current Wave
                if(passedWaveEvents != waveEvents.size()) {

                    currentWaveEvent = waveEvents.get(passedWaveEvents);
                    if (currentWaveEvent.getWaveNumber() == waveNumber) {

                        if (currentWaveEvent.getEventType().equals("SPAWN")) {

                            currentWave.addWaveEvent(currentWaveEvent);
                        } else {

                            currentWave.addWaveEvent(currentWaveEvent.getSpawnDelay(), timescaleMultiplier);
                        }
                    }
                }
            } else if(waveStatus == ALL_SLICERS_DESPAWNED){

                //All Wave Events have been completed (Slicers all Gone from all Wave Events)
                currentWave = null;
            }

            //If wave is finished
            if(currentWave == null && currentWaveEvent.getWaveNumber() != waveNumber){

                if (passedWaveEvents > (waveEvents.size() - 1)) {
                    currentStatus = WINNER_STATUS;
                    nextLevel();
                } else {
                    player.endOfWaveReward(waveNumber);
                    isEnemyWave = false;

                    //Clear all left over projectiles
                    for (ActiveTower s : tanks) {
                        s.getCurrentProjectiles().clear();
                        s.getCurrentProjectiles().removeAll(Collections.singleton(null));
                    }
                }
            }
        }

        //Update all Active Towers
        for (ActiveTower s : tanks) {
            if(isEnemyWave) {
                s.updateActiveTower(currentEnemies,timescaleMultiplier);
            }else {
                s.updateStationaryActiveTower();
            }
        }

        //Remove all explosives that have exploded
        Airplane.removeHasExplodedExplosives(airplanes);

        //Update the Status Panel
        if(waveNumber > waveEvents.get(waveEvents.size()-1).getWaveNumber()){
            waveNumber = waveEvents.get(waveEvents.size()-1).getWaveNumber();
        }
        statusPanel.updateStatusPanel(player, waveNumber, timescaleMultiplier, currentStatus);
    }

    /**
     *  Load the next level (If there is a next level)
     *  Reset the game state
     *
     */
    public void nextLevel(){
        currentLevelNumber++;

        String tmxFile =String.format("res/levels/%d.tmx",currentLevelNumber);
        if(!(new File(tmxFile).isFile())){
            return;
        }

        timescaleMultiplier = DEFAULT_MULTIPLIER;
        passedWaveEvents = 0;
        isEnemyWave = false;
        towerBeingPlaced= "NO TOWER SELECTED";
        currentWave = null;
        currentWaveEvent = waveEvents.get(0);
        waveNumber = currentWaveEvent.getWaveNumber();
        currentStatus = AWAITING_START_STATUS;
        airplanes = new ArrayList<>();
        tanks = new ArrayList<>();
        player = new Player();
        gameMap = new TiledMap(tmxFile);
        Airplane.resetDirection();
    }
}
