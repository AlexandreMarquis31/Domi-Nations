import game.gameManager;
import player.player;
import java.util.ArrayList;
import java.awt.Color;

public class Main {
    public static void main(String[] args){
        gameManager game = new gameManager();
        ArrayList<player> playerList = new ArrayList<player>();
        playerList.add(new player("Stan",new Color((float)0.5,(float)0.5,(float)0.5)));
        playerList.add(new player("Stan",new Color((float)0.5,(float)0.5,(float)0.5)));
        playerList.add(new player("Stan",new Color((float)0.5,(float)0.5,(float)0.5)));
        game.newGame(playerList,"None");
    }
}
