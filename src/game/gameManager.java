package game;

import player.player;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

public class gameManager {
    ArrayList<player> listPlayers = new ArrayList<player>();
    ArrayList<domino> listDominos = new ArrayList<domino>();
    String spetialRule;
    int totalKings;

    public gameManager() {
        importDominos("dominos.csv");
    }

    public void importDominos(String dominosCSVPath) {
        String csvFile = dominosCSVPath;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] infos = line.split(cvsSplitBy);
                listDominos.add(new domino(new dominoPart(infos[1], Boolean.parseBoolean(infos[0])), new dominoPart(infos[3], Boolean.parseBoolean(infos[2])), Integer.parseInt(infos[4])));
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

    public void newGame(ArrayList<player> listPlayers, String spetialRule) {
        Collections.shuffle(listDominos);
        this.listPlayers = listPlayers;
        this.spetialRule = spetialRule;
        ArrayList<domino> list = listDominos;
        Collections.shuffle(this.listPlayers);
        System.out.println(list.size());
        for (int i = 47; i > 47 - (12 * (4 - listPlayers.size())); i--) {
            list.remove(i);
        }
        System.out.println(list.size());
        this.listDominos = list;
        totalKings = listPlayers.size();
        if (listPlayers.size() == 2) {
            for (Iterator<player> i = listPlayers.iterator(); i.hasNext(); ) {
                player player = i.next();
                player.kings = 2;
            }
            totalKings = 4;
        }
        newTurn();
        System.out.println(listDominos.size());
    }

    public void newTurn() {
        for (int i = 0; i < totalKings; i++) {
            System.out.println(listDominos.get(i));
        }
        this.listDominos = new ArrayList<domino>(this.listDominos.subList(totalKings, listDominos.size()));
        for (Iterator<player> i = this.listPlayers.iterator(); i.hasNext(); ) {
            chooseDomino(player);
        }
    }
    public void chooseDomino(player player){
        System.out.println(listDominos.get(i));
    }

}
