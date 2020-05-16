import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ShadowDefend extends AbstractGame {

    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final String TITLE = "ShadowDefend";
    private static final String TMX_FILE = "res/levels/1.tmx";
    private static final String WAVE_EVENT_FILE = "res/levels/waves.txt";
    private final int numberOfWaves;
    private final TiledMap gameMap;
    private int timescaleMultiplier;
    private EnemyWave slicerWave;
    private int waveNumber;
    private int passedWaveEvents = 0;
    private boolean isEnemyWave = false;
    private int delay = 0;
    private WaveEvent currentWaveEvent;
    private final BuyPanel buyPanel;
    private final StatusPanel statusPanel;
    private final Player player;
    private String towerSelected;
    private String currentStatus;
    private final List<Airplane> airplanes;
    private final List<Tower> tanks;
    private final List<WaveEvent> waveEvents;
    int waveStatus;
    List<EnemyWave> slicerWaveEvent;
    private List<Slicer> currentEnemies;


    public ShadowDefend() {
        super(WIDTH, HEIGHT, TITLE);
        gameMap = new TiledMap(TMX_FILE);

        // This stops the visual glitch from occurring when pressing 'S'
        new Image("res/images/slicer.png");
        ///////////////////////////////////////////////////////////////
        //Set the timescale multiplier to the default value of 1
        timescaleMultiplier = 1;


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

            if(waveEventBreakdown.length == 5){

                waveEvents.set(i, new WaveEvent(Integer.parseInt(waveEventBreakdown[0]), Integer.parseInt(waveEventBreakdown[2]), waveEventBreakdown[3], Integer.parseInt(waveEventBreakdown[4])));

            }else{
                waveEvents.set(i, new WaveEvent(Integer.parseInt(waveEventBreakdown[0]), Integer.parseInt(waveEventBreakdown[2])));
            }

            i++;
        }



        currentWaveEvent = waveEvents.get(0);
        waveNumber = currentWaveEvent.getWave();
        waveStatus = 0;
        buyPanel = new BuyPanel();
        statusPanel = new StatusPanel();
        player = new Player();
        towerSelected = "NO TOWER SELECTED";
        currentStatus = "Awaiting Start";
        airplanes = new ArrayList<>();
        tanks = new ArrayList<>();
        currentEnemies = new ArrayList<>();
        slicerWaveEvent = new ArrayList<>();
        numberOfWaves = waveEvents.get(waveEvents.size()-1).getWave();
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
        Point mousePosition = input.getMousePosition();
        boolean leftKeyPressed = input.wasPressed(MouseButtons.LEFT);
        boolean rightKeyPressed = input.wasPressed(MouseButtons.RIGHT);



        if(towerSelected.equals("TANK") && leftKeyPressed && !gameMap.hasProperty((int) mousePosition.x,(int) mousePosition.y,"blocked")){
            tanks.add(new Tank(mousePosition,player));
            towerSelected = "NO TOWER SELECTED";
        }
        if(towerSelected.equals("SUPER TANK") && leftKeyPressed && !gameMap.hasProperty((int) mousePosition.x,(int) mousePosition.y,"blocked")){
            tanks.add(new SuperTank(mousePosition, player));
            towerSelected = "NO TOWER SELECTED";
        }
        if(towerSelected.equals("AIRPLANE") && leftKeyPressed && !gameMap.hasProperty((int) mousePosition.x,(int) mousePosition.y,"blocked")){
            airplanes.add(new Airplane(mousePosition, player));
            towerSelected = "NO TOWER SELECTED";
        }

        if(towerSelected.equals("TANK") && rightKeyPressed){
            towerSelected = "NO TOWER SELECTED";
        }
        if(towerSelected.equals("SUPER TANK") && rightKeyPressed){
            towerSelected = "NO TOWER SELECTED";
        }
        if(towerSelected.equals("AIRPLANE") && rightKeyPressed){
            towerSelected = "NO TOWER SELECTED";
        }



        String test = buyPanel.updateBuyPanel(player, mousePosition, leftKeyPressed);

        if(towerSelected.equals("NO TOWER SELECTED") && !test.equals("NO TOWER SELECTED")){
            towerSelected = test;
        }

        statusPanel.updateStatusPanel(player, waveNumber, timescaleMultiplier, currentStatus);


        if(!towerSelected.equals("NO TOWER SELECTED")&& !gameMap.hasProperty((int) mousePosition.x,(int) mousePosition.y,"blocked")){
            if(towerSelected.equals("TANK")){
                Image TANK = new Image("res/images/tank.png");
                TANK.draw(mousePosition.x,mousePosition.y);
                currentStatus = "Placing";
            }
            if(towerSelected.equals("SUPER TANK")){
                Image TANK = new Image("res/images/supertank.png");
                TANK.draw(mousePosition.x,mousePosition.y);
                currentStatus = "Placing";
            }
            if(towerSelected.equals("AIRPLANE")){
                Image AIRPLANE = new Image("res/images/airsupport.png");
                AIRPLANE.draw(mousePosition.x,mousePosition.y);
                currentStatus = "Placing";
            }
        }





        int i = 0;
        int airplaneStatus =0;
        for(Airplane s:airplanes){
            if(s!= null) {
                 airplaneStatus= s.updateAirplane(timescaleMultiplier);
            }
            if(airplaneStatus == 1){
                airplanes.set(i, null);
            }
            i++;
        }
        airplanes.removeAll(Collections.singleton(null));

        //If 'S' is pressed start a new enemy wave
        if (input.wasPressed(Keys.S) && !isEnemyWave) {
            slicerWaveEvent.add(new EnemyWave(currentWaveEvent.getNumberToSpawn(),currentWaveEvent.getEnemyType()));
            isEnemyWave = true;
            waveNumber = currentWaveEvent.getWave();
            currentStatus = "Wave in Progress";
        }

        //If 'L' is pressed increase the Timescale by 1
        if (input.wasPressed(Keys.L)) {
            timescaleMultiplier++;
        }

        //If 'K' is pressed decrease the Timescale by 1, but the Timescale cannot be less than 1
        if (input.wasPressed(Keys.K)) {
            if (timescaleMultiplier != 1) {
                timescaleMultiplier--;
            }
        }


        int k;

        if(!slicerWaveEvent.isEmpty()){
            k=0;
            int currentSize = slicerWaveEvent.size();
            for(EnemyWave s: slicerWaveEvent) {
                waveStatus = 0;


                if(s.getDelay() != 0){
                    s.setDelay(s.getDelay() - 1);

                    System.out.println(s.getDelay());
                    if(s.getDelay() == 0){
                        waveStatus = 1;
                        slicerWaveEvent.set(k,null);
                    }
                } else {

                    //If wave status is 0, do nothing
                    //If wave status is 1, that means all slicers have spawned
                    //If wave status is 2, that means all slicers of that wave event have either died or reached the end
                    waveStatus = s.nextWaveFrame(gameMap, timescaleMultiplier, player, tanks, airplanes, currentWaveEvent,currentEnemies);
                }

                if(waveStatus == 1){


                    //delay = waveEvents.get(passedWaveEvents).getSpawnDelay();

                    passedWaveEvents++;
                    if(passedWaveEvents == waveEvents.size()){
                        break;
                    }

                    currentWaveEvent = waveEvents.get(passedWaveEvents);
                    if(currentWaveEvent.getWave() == waveNumber) {

                        if (currentWaveEvent.getEventType().equalsIgnoreCase("spawn")) {

                            slicerWaveEvent.add(new EnemyWave(currentWaveEvent.getNumberToSpawn(), currentWaveEvent.getEnemyType()));
                        } else {

                            slicerWaveEvent.add(new EnemyWave(currentWaveEvent, timescaleMultiplier));

                        }
                    }
                } else if(waveStatus == 2){
                  //  System.out.println("TEST2");

                    slicerWaveEvent.set(k,null);

                }


                k++;
                if(k == currentSize){
                    break;
                }
            }


            slicerWaveEvent.removeAll(Collections.singleton(null));



           // System.out.println("TEST5");

            if(slicerWaveEvent.isEmpty()){


                if (passedWaveEvents > (waveEvents.size() - 1)) {
                    currentStatus = "Winner!";
                } else {
                    player.endOfWaveReward(waveNumber);
                    isEnemyWave = false;
                    waveNumber = currentWaveEvent.getWave();

                    for (Tower s : tanks) {
                        s.getCurrentProjectiles().clear();
                        s.getCurrentProjectiles().removeAll(Collections.singleton(null));
                    }

                    if (!currentStatus.equalsIgnoreCase("Winner!") && !currentStatus.equalsIgnoreCase("Placing")) {
                        currentStatus = "Awaiting Start";
                    }

                }
            }
        }
        for (Tower s : tanks) {
            if(isEnemyWave) {
                //assert slicerWave != null;
                s.updateTank(currentEnemies,timescaleMultiplier);
            }else {
                s.updateTank();
            }
        }
    }
}
