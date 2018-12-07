package game;
import player.player;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class gameManager {
    ArrayList<player> listPlayers = new ArrayList<player>();
    ArrayList<domino> listDominos = new ArrayList<domino>();

    public gameManager() {
        String csvFile = "dominos.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] infos = line.split(cvsSplitBy);
                System.out.println(infos[0] + "  "+infos[1] + "  " + infos[2] + "  "+ infos[3] + "  "+ infos[4] + "  ");
                listDominos.add(new domino(new dominoPart(infos[1],Boolean.parseBoolean(infos[0])),new dominoPart(infos[3],Boolean.parseBoolean(infos[2])),  Integer.parseInt(infos[4]) ));
            }
            System.out.println(listDominos);
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
}
