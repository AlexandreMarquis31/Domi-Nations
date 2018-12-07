package game;
import player.player;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class gameManager {
    ArrayList<player> listPlayers = new ArrayList<player>();
    ArrayList<domino> listDominos = new ArrayList<domino>();
    public gameManager() {
        importDominos("dominos.csv");
    }
    public void importDominos(String dominosCSVPath){
        String csvFile = dominosCSVPath;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] infos = line.split(cvsSplitBy);
                listDominos.add(new domino(new dominoPart(infos[1],Boolean.parseBoolean(infos[0])),new dominoPart(infos[3],Boolean.parseBoolean(infos[2])),  Integer.parseInt(infos[4]) ));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void newGame(String rules, ArrayList<player> listPlayers){
        Collections.shuffle(listDominos);
        this.listPlayers = listPlayers;
    }
}
