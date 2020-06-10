/**
 * Class to hold the information of a single wave event
 */
public class WaveEvent {

    private static final String SPAWN_EVENT = "SPAWN";
    private static final String DELAY_EVENT = "DELAY";

    private final int waveNumber;
    private int numberToSpawn;
    private String enemyType;
    private final int spawnDelay;
    private final String eventType;


    /**
     * Constructor for a Spawn Type Wave Event
     *
     * @param waveNumber The Wave Number that this Wave Event belongs to
     * @param numberToSpawn The number of slicers to spawn in the wave
     * @param enemyType The type of Slicer to spawn in this wave
     * @param spawnDelay The Spawn Delay Before the next Slicer Spawns
     */
    public WaveEvent(int waveNumber, int numberToSpawn, String enemyType, int spawnDelay) {
        this.waveNumber = waveNumber;
        this.numberToSpawn = numberToSpawn;
        this.enemyType = enemyType;
        this.spawnDelay = spawnDelay;
        eventType = SPAWN_EVENT;
    }

    /**
     * Constructor for a Delay Type Wave Event
     *
     * @param waveNumber The Wave Number that this Wave Event belongs to
     * @param spawnDelay The Spawn Delay Before the Next Wave Event Starts
     */
    public WaveEvent(int waveNumber, int spawnDelay) {
        this.waveNumber = waveNumber;
        this.spawnDelay = spawnDelay;
        eventType = DELAY_EVENT;
    }

    /**
     * Getter for the Wave Number
     *
     * @return The Wave Number
     */
    public int getWaveNumber() {
        return waveNumber;
    }

    /**
     * Getter for the Number to Spawn
     *
     * @return the Number to Spawn
     */
    public int getNumberToSpawn() {
        return numberToSpawn;
    }

    /**
     * Getter for the Enemy Type
     *
     * @return The Enemy Type
     */
    public String getEnemyType() {
        return enemyType;
    }

    /**
     * Getter for the Spawn Delay
     *
     * @return the Spawn Delay
     */
    public int getSpawnDelay() {
        return spawnDelay;
    }

    /**
     * Getter for the Event Type  (SPAWN or DELAY)
     *
     * @return the Event Type
     */
    public String getEventType() {
        return eventType;
    }
}
