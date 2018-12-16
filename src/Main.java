import game.gameManager;
import player.player;
import java.util.ArrayList;
import java.awt.Color;

public class Main {
    public static void main(String[] args){
        gameManager game = new gameManager();
        ArrayList<player> playerList = new ArrayList<player>();
        playerList.add(new player("Stan",new Color((float)0,(float)1,(float)0)));
       // playerList.add(new player("Stan",new Color((float)0,(float)0,(float)1)));
        //playerList.add(new player("Stan",new Color((float)1,(float)0,(float)0)));
        //game.newGame(playerList,"None");
    }
}
