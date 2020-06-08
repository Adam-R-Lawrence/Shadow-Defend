import bagel.Window;

/**
 * Class that holds all of the player's current stats
 */
public class Player {

    private final static int STARTING_LIVES = 25;
    private final static int STARTING_MONEY = 500;
    private final static int BASE_WAVE_REWARD = 150;
    private final static int CURRENT_WAVE_BONUS = 100;
    private int lives;
    private int money;

    /**
     * Constructor for th player, setting the initial values
     */
    public Player(){
         lives = STARTING_LIVES;
         money = STARTING_MONEY;
    }

    /**
     * Getter for how many lives the player has left
     *
     * @return The number of lives remaining
     */
    public int getLives() {
        return lives;
    }

    /**
     * Getter for how much money the player has
     *
     * @return The current amount of money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Method to decrease the amount of lives a player has,
     * and to check if the player has run out of lives, which
     * closes the game
     *
     * @param livesLost The amount of lives the player has lost
     */
    public void decreaseLives(int livesLost) {
        lives = lives - livesLost;

        //Check if player has no remaining lives
        if(lives == 0){
            //Close the Game
            Window.close();
        }
    }

    /**
     * method for rewarding the player with bonus money when
     * every wave is cleared
     *
     * @param waveJustFinished Wave number just completed
     */
    public void endOfWaveReward(int waveJustFinished){
        money = money + (BASE_WAVE_REWARD +(waveJustFinished * CURRENT_WAVE_BONUS));
    }

    /**
     * Method to increase the current balance of the player
     *
     * @param moneyIncrease The amount of money gained
     */
    public void increaseMoney(int moneyIncrease) {
        money = money + moneyIncrease;
    }

    /**
     * Method to decrease the current balance of the player
     *
     * @param moneyDecrease The amount of money lost
     */
    public void decreaseMoney(int moneyDecrease) {
        money = money - moneyDecrease;
    }
}
