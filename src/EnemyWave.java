import bagel.map.TiledMap;

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

    private int frameCounter;
    private int numberOfEnemiesSpawned;
    private int numberOfEnemiesInWave = 0;
    private String enemyType;
    private int currentSpawnDelay;

    /**
     * Constructor for an Enemy Wave
     */
    protected EnemyWave() {

        currentSpawnDelay = 0;
        frameCounter = 0;
        numberOfEnemiesSpawned = 0;

    }

    /**
     * Add a Spawn Wave Event
     *
     * @param waveEvent A spawn Wave Event
     */
    public void addWaveEvent(WaveEvent waveEvent){

        numberOfEnemiesInWave = numberOfEnemiesInWave + waveEvent.getNumberToSpawn();
        enemyType = waveEvent.getEnemyType();
        currentSpawnDelay = -1;
    }

    /**
     * Add a Delay Wave Event
     *
     * @param delay How long the Delay will be for
     * @param timescaleMultiplier The current Timescale Of the Game
     */
    public void addWaveEvent(int delay, int timescaleMultiplier){
        this.currentSpawnDelay = (int) ((delay * (60 / 1000.0))/timescaleMultiplier);
    }

    /**
     *
     * Method to update the wave, i.e. let enemies move to their next position
     *
     * @param map The game's tilemap
     * @param timescaleMultiplier The current time multiplayer of the game
     */
    public int nextWaveFrame(TiledMap map, int timescaleMultiplier, Player player, List<ActiveTower> tanks, List<Airplane> airplanes, WaveEvent waveEvent, List<Slicer> enemiesInWave) {

        int test = 0;
        //Check if its time to spawn in another enemy
        if(currentSpawnDelay <= 0) {
            for (int i = 0; i < timescaleMultiplier; i++) {


                if ((frameCounter) % ((int) (FRAMES_IN_A_SECOND * (waveEvent.getSpawnDelay() / 1000.0)))
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

                        test = 1;
                    }
                }
                frameCounter++;
            }
        } else{
            currentSpawnDelay = currentSpawnDelay - timescaleMultiplier;
            frameCounter = 0;

            if(currentSpawnDelay <= 0){
                test= 1;
                currentSpawnDelay = -1;
            }

        }

        int waveStatus;
        //Update all the current Enemies
        for(int i = 0 ; i < enemiesInWave.size(); i++) {
            if(enemiesInWave.get(i)!=null) {

                waveStatus = enemiesInWave.get(i).nextMove(timescaleMultiplier, i, numberOfEnemiesInWave, tanks, airplanes);
                if (waveStatus == 1) {
                    return 2;
                }

                if (waveStatus == 2) {
                    enemiesInWave.set(i, null);
                }

                if (waveStatus == 3) {


                    switch (enemiesInWave.get(i).getEnemyType()) {
                        case "SuperSlicer":

                            for (int j = 0; j < 2; j++) {
                                enemiesInWave.add(new RegularSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                            }

                            break;
                        case "MegaSlicer":
                            for (int j = 0; j < 2; j++) {
                                enemiesInWave.add(new SuperSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                            }
                            break;
                        case "ApexSlicer":
                            for (int j = 0; j < 4; j++) {
                                enemiesInWave.add(new MegaSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                            }
                            break;
                    }

                    enemiesInWave.set(i, null);

                }
            }
        }

        enemiesInWave.removeAll(Collections.singleton(null));

        if ((enemiesInWave.size() == 0) &&  (numberOfEnemiesInWave == numberOfEnemiesSpawned)) {
            return 2;
        }
        return test;
    }
}
