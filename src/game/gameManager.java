package game;

import player.player;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;

public class gameManager {
    ArrayList<player> listPlayers = new ArrayList<player>();
    ArrayList<Color> listKings = new ArrayList<Color>();
    ArrayList<domino> listDominos = new ArrayList<domino>();
    ArrayList<HashMap<domino,Color>> displayedDomino = new ArrayList<HashMap<domino,Color>>();
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
        for (Iterator<player> i = listPlayers.iterator(); i.hasNext(); ) {
            player player = i.next();
            for (int k = 0; k< player.kings; k++){
                listKings.add(player.color);
            }
        }
        Collections.shuffle(this.listKings);
        newTurn();
        System.out.println(listDominos.size());

    }

    public void newTurn() {
        this.displayedDomino = new LinkedHashMap<domino, Color>();
        for (int i = 0; i < totalKings; i++) {
            System.out.println(listDominos.get(i));
            displayedDomino.add(new HashMap<domino,Color>(listDominos.get(i),null));
        }
        this.listDominos = new ArrayList<domino>(this.listDominos.subList(totalKings, listDominos.size()));
        for (Iterator<Color> i = this.listKings.iterator(); i.hasNext(); ) {
            Color color = i.next();
            chooseDomino(color);
        }
    }
    public void chooseDomino(Color color){
        System.out.println("Joueur de couleur: "+color+" choisissez votre domino (nombre de 1 à 4 ou 3) :");
        Scanner scan = new Scanner(System.in);
        int choix = scan.nextInt();
        System.out.println(choix);
        displayedDomino.get(choix).replace(displayedDomino.get(choix));
        System.out.println(displayedDomino);
    }

}
