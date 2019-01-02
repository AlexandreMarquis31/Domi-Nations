import UI.graphicsManager;
import game.gameManager;
import player.player;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) {
        //build the GUI on a new thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                graphicsManager gManager = new graphicsManager();
                gManager.startGraphics();
                //create a new thread to keep the GUI responsive while the game runs
                Thread thread = new Thread() {
                    public void run() {
                        gameManager game = new gameManager(gManager);
                        ArrayList<player> playerList = new ArrayList<player>();
                        playerList.add(new player("Stan",new Color((float)0,(float)1,(float)0)));
                        playerList.add(new player("Marc",new Color((float)0,(float)0,(float)1)));
                        playerList.add(new player("Jean",new Color((float)1,(float)0,(float)0)));

                        game.newGame(playerList,"None");
                    }
                };
                thread.start();
            }
        });
    }
}

