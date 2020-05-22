public class WaveEvent {

    private final int waveNumber;
    private int numberToSpawn;
    private String enemyType;
    private final int spawnDelay;
    private final String eventType;

    public WaveEvent(int waveNumber, int numberToSpawn, String enemyType, int spawnDelay) {
        this.waveNumber = waveNumber;
        this.numberToSpawn = numberToSpawn;
        this.enemyType = enemyType;
        this.spawnDelay = spawnDelay;
        eventType = "spawn";
    }


    public WaveEvent(int waveNumber, int spawnDelay) {
        this.waveNumber = waveNumber;
        this.spawnDelay = spawnDelay;
        eventType = "delay";
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getNumberToSpawn() {
        return numberToSpawn;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }

    public String getEventType() {
        return eventType;
    }


}
