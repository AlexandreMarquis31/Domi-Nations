package game;

import UI.graphicsManager;
import javafx.util.Pair;
import player.player;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class gameManager  {
    private ArrayList<player> listPlayers = new ArrayList<player>();
    private ArrayList<player> listKings = new ArrayList<player>();
    private List<domino> listDominos = new ArrayList<>();
    private List<domino> selectableDominos =  Arrays.asList(new domino[3]);
    private List<Pair<domino,player>> selectedDominos = Arrays.asList(new Pair[3]);
    private String specialRule;
    private int totalKings;
    private graphicsManager gManager;
    public gameManager(graphicsManager g) {
        this.gManager = g;
        importDominos();
    }

    private void importDominos() {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader("dominos.csv"));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] infos = line.split(cvsSplitBy);
                listDominos.add(new domino(new dominoPart(infos[1], Integer.parseInt(infos[0])), new dominoPart(infos[3],Integer.parseInt(infos[2])), Integer.parseInt(infos[4])));
            }
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

    public void newGame(ArrayList<player> list, String rule) {
        Collections.shuffle(listDominos);
        listPlayers = list;
        specialRule = rule;
        listDominos = listDominos.subList(0,(12*(listPlayers.size())));
        totalKings = listPlayers.size();
        if (listPlayers.size() == 2) {
            for (player player : listPlayers) {
                player.kings = 2;
            }
            totalKings = 4;
        }
        for (player player : listPlayers) {
            for (int k = 0; k< player.kings; k++){
                listKings.add(player);
            }
        }
        Collections.shuffle(listKings);
        gManager.setListPlayers(listPlayers);
        start();
    }
    private void start(){
        System.out.println(listDominos.size());
        newLineDomino();
        for (player player: listKings) {
            gManager.currentPlayer.setText(player.name);
            gManager.currentPlayer.setForeground(player.color);
            chooseDomino(player);
        }
        while(listDominos.size() >0){
            newTurn();
        }
    }

    private void newTurn() {
        System.out.println(listDominos.size());
        ArrayList<Pair<domino,player>> dominoToPlace = new ArrayList<>(selectedDominos);
        dominoToPlace.sort(PairComparator);
        newLineDomino();
        for (Pair<domino,player> pair : dominoToPlace) {
            gManager.currentPlayer.setText(pair.getValue().name);
            gManager.currentPlayer.setForeground(pair.getValue().color);
            placeDomino(pair.getValue(),pair.getKey());
            chooseDomino(pair.getValue());
        }
    }
    private void placeDomino(player player, domino domino){
        System.out.println(player.name+" placez votre domino (x):");
        Scanner scan = new Scanner(System.in);
        int choixX = scan.nextInt();
        System.out.println(player.name+" placez votre domino (y):");
        int choixY = scan.nextInt();
        System.out.println(player.name+" placez votre domino (h , v , rh , rv):");
        scan.nextLine();
        while (!scan.hasNextLine());
        String choix = scan.nextLine();
        switch (choix){
            case "h" :
                player.board[choixY][choixX] = domino.part1;
                player.board[choixY][choixX+1] = domino.part2;
                break;
            case "v" :
                player.board[choixY][choixX] = domino.part1;
                player.board[choixY+1][choixX] = domino.part2;
                break;
            case "rh" :
                player.board[choixY][choixX] = domino.part2;
                player.board[choixY][choixX+1] = domino.part1;
                break;
            case "rv" :
                player.board[choixY][choixX] = domino.part2;
                player.board[choixY+1][choixX] = domino.part1;
                break;
        }
        player.showBoard();
    }
    private void chooseDomino(player player){
        System.out.println(player.name+" choisissez votre domino (nombre de 0 à 3 ou 2) :");
        Scanner scan = new Scanner(System.in);
        int choix = scan.nextInt();
        selectedDominos.set(totalKings-choix-1,new Pair<>(selectableDominos.get(totalKings-choix-1),player));
    }
    private void newLineDomino(){
        listDominos.subList(0,totalKings).sort(DominoComparator);
        gManager.setSelectableDominos(listDominos.subList(0,totalKings));
        for (int i = totalKings-1; i > -1; i--) {
            System.out.println(listDominos.get(i));
            selectableDominos.set(i,listDominos.get(i));
            listDominos.remove(i);
        }
    }
    private Comparator<Pair<domino, player>> PairComparator = (Pair<domino, player> m1, Pair<domino, player> m2)->Integer.compare(m2.getKey().number,m1.getKey().number);
    private Comparator<domino> DominoComparator = (domino m1, domino m2)->Integer.compare(m2.number,m1.number);
}