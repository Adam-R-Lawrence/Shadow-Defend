import bagel.map.TiledMap;
import org.lwjgl.system.CallbackI;

import java.util.Collections;
import java.util.List;

/**
 * @author arlawrence
 *
 * Enemy Wave Class, To coordinate an Enemy Wave Level
 *
 */
public class EnemyWave {

    private final static int FRAMES_IN_A_SECOND = 60;
    private final static int MILLISECONDS_IN_A_SECOND = 1000;
    private final static int NO_SPAWN_DELAY = -1;
    private final static int WAVE_EVENT_NOT_FINISHED_SPAWNING = 0;
    private final static int WAVE_EVENT_FINISHED_SPAWNING = 1;
    private final static int ALL_SLICERS_HAVE_DIED_OR_REACHED_END = 2;
    private final static int FINAL_SLICER_TO_REACH_END = 1;
    private final static int SLICER_HAS_REACHED_THE_END = 2;
    private final static int SLICER_HAS_DIED = 3;
    private final static int NUMBER_OF_CHILDREN_APEX_SLICER = 4;
    private final static int NUMBER_OF_CHILDREN_MEGA_SLICER = 2;
    private final static int NUMBER_OF_CHILDREN_SUPER_SLICER = 2;

    private int frameCounter = 0;
    private int numberOfEnemiesSpawned = 0;
    private int numberOfEnemiesInWave = 0;
    private int currentSpawnDelay = 0;
    private String enemyType;

    /**
     * Add a Spawn Wave Event
     *
     * @param waveEvent A spawn Wave Event
     */
    public void addWaveEvent(WaveEvent waveEvent) {

        numberOfEnemiesInWave = numberOfEnemiesInWave + waveEvent.getNumberToSpawn();
        enemyType = waveEvent.getEnemyType();
        currentSpawnDelay = NO_SPAWN_DELAY;
    }

    /**
     * Add a Delay Wave Event
     *
     * @param delay How long the Delay will be for
     * @param timescaleMultiplier The current Timescale Of the Game
     */
    public void addWaveEvent(int delay, int timescaleMultiplier) {
        this.currentSpawnDelay = (int) ((delay * (1.0 * FRAMES_IN_A_SECOND/MILLISECONDS_IN_A_SECOND))/timescaleMultiplier);
    }

    /**
     *
     * Method to update the wave, i.e. let enemies move to their next position
     *
     * @param map The game's tilemap
     * @param timescaleMultiplier The current time multiplayer of the game
     */
    public int nextWaveFrame(TiledMap map, int timescaleMultiplier, Player player, List<ActiveTower> tanks, List<Airplane> airplanes, WaveEvent waveEvent, List<Slicer> enemiesInWave) {

        int statusOfWaveEvent = WAVE_EVENT_NOT_FINISHED_SPAWNING;

        //Check if its time to spawn in another enemy
        if(currentSpawnDelay <= 0) {
            for (int i = 0; i < timescaleMultiplier; i++) {

                if ((frameCounter) % ((int) (FRAMES_IN_A_SECOND * (1.0 * waveEvent.getSpawnDelay() /MILLISECONDS_IN_A_SECOND)))
                        == 0 && numberOfEnemiesSpawned < numberOfEnemiesInWave) {

                    //Determine which type of slicer to spawn
                    if (enemyType.equals("slicer")) {
                        enemiesInWave.add(new RegularSlicer(map.getAllPolylines().get(0), player));
                    }
                    if (enemyType.equals("superslicer")) {
                        enemiesInWave.add(new SuperSlicer(map.getAllPolylines().get(0), player));
                    }
                    if (enemyType.equals("megaslicer")) {
                        enemiesInWave.add(new MegaSlicer(map.getAllPolylines().get(0), player));
                    }
                    if (enemyType.equals("apexslicer")) {
                        enemiesInWave.add(new ApexSlicer(map.getAllPolylines().get(0), player));
                    }

                    numberOfEnemiesSpawned++;

                    if (numberOfEnemiesSpawned == numberOfEnemiesInWave) {

                        statusOfWaveEvent = WAVE_EVENT_FINISHED_SPAWNING;
                    }
                }
                frameCounter++;
            }
        } else{
            currentSpawnDelay = currentSpawnDelay - timescaleMultiplier;
            frameCounter = 0;
            System.out.println(currentSpawnDelay);

            if(currentSpawnDelay <= 0) {
                statusOfWaveEvent = WAVE_EVENT_FINISHED_SPAWNING;
                currentSpawnDelay = NO_SPAWN_DELAY;
            }

        }

        int waveStatus;
        //Update all the current Enemies
        for(int i = 0 ; i < enemiesInWave.size(); i++) {
            if(enemiesInWave.get(i) != null) {

                waveStatus = enemiesInWave.get(i).nextMove(timescaleMultiplier, i, numberOfEnemiesInWave, tanks, airplanes);

                //If this is the final slicer, and has reached the end
                if (waveStatus == FINAL_SLICER_TO_REACH_END) {
                    return ALL_SLICERS_HAVE_DIED_OR_REACHED_END;
                }

                //If slicer has reached the end of the polyline, remove it from the game
                if (waveStatus == SLICER_HAS_REACHED_THE_END) {
                    enemiesInWave.set(i, null);
                }

                //If slicer has died, spawn it's child if applicable
                if (waveStatus == SLICER_HAS_DIED) {

                    switch (enemiesInWave.get(i).getEnemyType()) {
                        case "SuperSlicer":
                            for (int j = 0; j < NUMBER_OF_CHILDREN_SUPER_SLICER; j++) {
                                enemiesInWave.add(new RegularSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                            }
                            break;
                        case "MegaSlicer":
                            for (int j = 0; j < NUMBER_OF_CHILDREN_MEGA_SLICER; j++) {
                                enemiesInWave.add(new SuperSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                            }
                            break;
                        case "ApexSlicer":
                            for (int j = 0; j < NUMBER_OF_CHILDREN_APEX_SLICER; j++) {
                                enemiesInWave.add(new MegaSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                            }
                            break;
                    }
                    enemiesInWave.set(i, null);
                }
            }
        }

        enemiesInWave.removeAll(Collections.singleton(null));

        if ((enemiesInWave.size() == 0) &&  (numberOfEnemiesInWave == numberOfEnemiesSpawned) && (currentSpawnDelay == NO_SPAWN_DELAY)) {
            return ALL_SLICERS_HAVE_DIED_OR_REACHED_END;
        }

        return statusOfWaveEvent;
    }
}
