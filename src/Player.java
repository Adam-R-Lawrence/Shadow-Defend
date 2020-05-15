public class Player {

    private final static int STARTING_LIVES = 25;
    private final static int STARTING_MONEY = 1000000;
    private final static int BASE_WAVE_REWARD = 150;
    private final static int CURRENT_WAVE_BONUS = 100;
    private int lives;
    private int money;


    public Player(){
         lives = STARTING_LIVES;
         money = STARTING_MONEY;
    }


    public int getLives() {
        return lives;
    }

    public int getMoney() {
        return money;
    }

    public void decreaseLives(int livesLost) {
        lives = lives - livesLost;

        //Check if lives is below 1
        //Update game as a loser
    }

    public void endOfWaveReward(int waveJustFinished){
        money = money + (BASE_WAVE_REWARD +(waveJustFinished * CURRENT_WAVE_BONUS));
    }

    public void increaseMoney(int moneyIncrease) {
        money = money + moneyIncrease;
    }

    public void decreaseMoney(int moneyDecrease) {
        money = money - moneyDecrease;
    }
}
