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

    //enemiesInWave is protected thus so it can be instantiated with subclass specific enemies
    //protected Slicer[] enemiesInWave;

    //To track how many frames since the wave has started
    private int frameCounter;
     private final List<Slicer> enemiesInWave;
    private int numberOfEnemiesSpawned;
    private final int numberOfEnemiesInWave;
    private int currentNumberOfEnemies;
    private final static int FRAMES_IN_A_SECOND = 60;
    //private final static int SECONDS_BETWEEN_ENEMIES = 5;
    //private final static int FRAMES_BETWEEN_ENEMIES = SECONDS_BETWEEN_ENEMIES * FRAMES_IN_A_SECOND;
    private final String enemyType;

    //Enemy Wave constructor
    protected EnemyWave(int numberOfEnemiesInWave, String enemyType) {
        this.numberOfEnemiesInWave = numberOfEnemiesInWave;
        //List<RegularSlicer> list = enemiesInWave.asList(array);

        frameCounter = 0;
        numberOfEnemiesSpawned = 0;
        enemiesInWave = new ArrayList<>();
        currentNumberOfEnemies = 0;
        this.enemyType = enemyType;
    }


    public List<Slicer> getEnemiesInWave() {
        return enemiesInWave;
    }


    /**
     *
     * Method to update the wave, i.e. let enemies move to their next position
     *
     * @param map The game's tilemap
     * @param timescaleMultiplier The current time multiplayer of the game
     */
    public int nextWaveFrame(TiledMap map, int timescaleMultiplier, Player player, List<Tower> tanks, List<Airplane> airplanes, WaveEvent waveEvent) {

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
            }
            frameCounter++;
        }

        int waveStatus;
        //Update all the current Enemies
        for(int i = 0 ; i < currentNumberOfEnemies; i++) {
                if(enemiesInWave.get(i)!=null) {

                    waveStatus = enemiesInWave.get(i).nextMove(timescaleMultiplier, i, numberOfEnemiesInWave, tanks, airplanes);
                    if (waveStatus == 1) {
                        return waveStatus;
                    }

                    if (waveStatus == 2) {
                        enemiesInWave.set(i, null);
                        currentNumberOfEnemies--;
                    }

                    if (waveStatus == 3) {
                        //System.out.println(enemiesInWave.get(i).getPolylinePointsPassed());


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
                    enemiesInWave.removeAll(Collections.singleton(null));

                }
        }

        if ((currentNumberOfEnemies == 0) &&  (numberOfEnemiesInWave == numberOfEnemiesSpawned)) {
            return 1;
        }

        return 0;
    }
}
