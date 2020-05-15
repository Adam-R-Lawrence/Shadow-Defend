public class WaveEvent {

    private final int wave;
    private int numberToSpawn;
    private String enemyType;
    private final int spawnDelay;
    private final String eventType;

    public WaveEvent(int wave, int numberToSpawn, String enemyType, int spawnDelay) {
        this.wave = wave;
        this.numberToSpawn = numberToSpawn;
        this.enemyType = enemyType;
        this.spawnDelay = spawnDelay;
        eventType = "spawn";
    }


    public WaveEvent(int wave, int spawnDelay) {
        this.wave = wave;
        this.spawnDelay = spawnDelay;
        eventType = "delay";
    }

    public int getWave() {
        return wave;
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
