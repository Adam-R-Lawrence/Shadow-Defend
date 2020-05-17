import bagel.map.TiledMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author arlawrence
 *
 * Enemy Wave Superclass
 *
 */
public class EnemyWave {



    //To track how many frames since the wave has started
    private int frameCounter;
    //private final List<Slicer> enemiesInWave;
    private int numberOfEnemiesSpawned;
    private int numberOfEnemiesInWave;
    private int currentNumberOfEnemies;
    private final static int FRAMES_IN_A_SECOND = 60;
    //private final static int SECONDS_BETWEEN_ENEMIES = 5;
    //private final static int FRAMES_BETWEEN_ENEMIES = SECONDS_BETWEEN_ENEMIES * FRAMES_IN_A_SECOND;
    private String enemyType;

    private int spawnDelay;
    private int delay;


    //Enemy Wave constructor
    protected EnemyWave(int numberOfEnemiesInWave, String enemyType) {
        this.numberOfEnemiesInWave = numberOfEnemiesInWave;
        //List<RegularSlicer> list = enemiesInWave.asList(array);

        frameCounter = 0;
        numberOfEnemiesSpawned = 0;
        //enemiesInWave = new ArrayList<>();
        currentNumberOfEnemies = 0;
        this.enemyType = enemyType;
    }

    protected EnemyWave(WaveEvent waveEvent, int timescaleMultiplier) {
        this.numberOfEnemiesInWave = 0;
        frameCounter = 0;
        numberOfEnemiesSpawned = 0;
        currentNumberOfEnemies = 0;
        this.enemyType = null;
        //enemiesInWave = null;
        delay = (int) ((waveEvent.getSpawnDelay() * (60 / 1000.0))/timescaleMultiplier);

    }



    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }




    public void addWaveEvent(WaveEvent waveEvent){

        numberOfEnemiesInWave = numberOfEnemiesInWave + waveEvent.getNumberToSpawn();
        spawnDelay = waveEvent.getSpawnDelay();
        enemyType = waveEvent.getEnemyType();
    }


    /**
     *
     * Method to update the wave, i.e. let enemies move to their next position
     *
     * @param map The game's tilemap
     * @param timescaleMultiplier The current time multiplayer of the game
     */
    public int nextWaveFrame(TiledMap map, int timescaleMultiplier, Player player, List<Tower> tanks, List<Airplane> airplanes, WaveEvent waveEvent, List<Slicer> enemiesInWave) {

        int test = 0;
        //Check if its time to spawn in another enemy
        for(int i = 0; i < timescaleMultiplier; i++) {
            if ((frameCounter) % ( (int) (FRAMES_IN_A_SECOND * (waveEvent.getSpawnDelay()/1000.0) ))
                    == 0 && numberOfEnemiesSpawned < numberOfEnemiesInWave) {

                //Spawn in a new enemy

                //Determine which type of slicer to spawn
                if(enemyType.equals("slicer")) {
                    enemiesInWave.add(new RegularSlicer(map.getAllPolylines().get(0),player));
                }
                if(enemyType.equals("superslicer")) {
                    enemiesInWave.add(new SuperSlicer(map.getAllPolylines().get(0),player));
                }
                if(enemyType.equals("megaslicer")) {
                    enemiesInWave.add(new MegaSlicer(map.getAllPolylines().get(0),player));
                }
                if(enemyType.equals("apexslicer")) {
                    enemiesInWave.add(new ApexSlicer(map.getAllPolylines().get(0),player));
                }

                numberOfEnemiesSpawned++;
                currentNumberOfEnemies++;


                if(numberOfEnemiesSpawned == numberOfEnemiesInWave){

                    test = 1;
                }
            }
            frameCounter++;
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
                        currentNumberOfEnemies--;
                    }

                    if (waveStatus == 3) {


                        switch (enemiesInWave.get(i).getEnemyType()) {
                            case "SuperSlicer":

                                for (int j = 0; j < 2; j++) {
                                    enemiesInWave.add(new RegularSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                                    currentNumberOfEnemies++;
                                }

                                break;
                            case "MegaSlicer":
                                for (int j = 0; j < 2; j++) {
                                    enemiesInWave.add(new SuperSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                                    currentNumberOfEnemies++;
                                }
                                break;
                            case "ApexSlicer":
                                for (int j = 0; j < 4; j++) {
                                    enemiesInWave.add(new MegaSlicer(map.getAllPolylines().get(0), player, enemiesInWave.get(i).getWhereToMove(), enemiesInWave.get(i).getMovementsDone(), enemiesInWave.get(i).getPolylinePointsPassed()));
                                    currentNumberOfEnemies++;
                                }
                                break;
                        }


                        enemiesInWave.set(i, null);
                        currentNumberOfEnemies--;


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
