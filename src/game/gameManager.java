package game;

import UI.graphicsManager;
import javafx.util.Pair;
import player.player;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class gameManager {
    private ArrayList<player> listPlayers = new ArrayList<player>();
    private ArrayList<player> listKings = new ArrayList<player>();
    private List<domino> listDominos = new ArrayList<>();
    private List<domino> selectableDominos;
    private ArrayList<domino> selectedDominos;
    private String specialRule;
    private int totalKings;
    private graphicsManager gManager;
    private boolean ligne = false;
    private boolean harmony = false;
    private int manches = 1;
    public player currentPlayer = null;
    public domino currentDomino = null;
    public static final Object lock = new Object();

    public gameManager(graphicsManager g) {
        gManager = g;
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
                listDominos.add(new domino(new dominoPart(infos[1], Integer.parseInt(infos[0])), new dominoPart(infos[3], Integer.parseInt(infos[2])), Integer.parseInt(infos[4])));
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

    public void newGame(ArrayList<player> list, String rule, int m) {
        if (rule.equals("Duel")){
            for (player player : list){
                player.setSize(13);
            }
            graphicsManager.sizePart = 20;
        } else {
            listDominos = listDominos.subList(0, (12 * (list.size())));
        }
        if (rule.equals("Harmonie")){
            harmony = true;
        }
        manches = m;
        gManager.manches = m;
        Collections.shuffle(listDominos);
        listPlayers = list;
        specialRule = rule;
        totalKings = listPlayers.size();
        if (listPlayers.size() == 2) {
            listKings.addAll(listPlayers);
            listKings.addAll(listPlayers);
            totalKings = 4;
        } else {
            listKings.addAll(listPlayers);
        }
        selectedDominos = new ArrayList<>();
        selectableDominos = Arrays.asList(new domino[totalKings]);
        Collections.shuffle(listKings);
        gManager.setPlayersUI(listPlayers);
        start();
    }

    private void start() {
        newLineDominos(ligne ? 1 : 0);
        ligne = !ligne;
        System.out.println(listKings);
        for (player player : listKings) {
            currentPlayer = player;
            gManager.setCurrentPlayer(player);
            chooseDomino(player);
        }
        while (listDominos.size() > 0) {
            newTurn();
        }
        selectedDominos.sort(DominoComparator);
        for (domino domino : selectedDominos) {
            placeDomino(domino);
        }
        for (player player : listPlayers) {
            player.cumuledScore += calculateScore(player.board);
        }
        gManager.showScores(listPlayers,manches);
    }

    private void newTurn() {
        System.out.println(listDominos.size());
        ArrayList<domino> dominoToPlace = new ArrayList<>(selectedDominos);
        dominoToPlace.sort(DominoComparator);
        newLineDominos(ligne ? 1 : 0);
        ligne = !ligne;
        selectedDominos.clear();
        for (domino domino : dominoToPlace) {
            currentPlayer = domino.player;
            placeDomino(domino);
            chooseDomino(domino.player);
        }
    }

    private void chooseDomino(player p) {
        gManager.labelConsigne.setText("Choisissez votre domino.");
        synchronized (lock) {
            while (p.state != player.STATE_DOMINOSELECTED) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        p.state = player.STATE_IDLE;
        selectedDominos.add(currentDomino);
    }

    private void placeDomino(domino domino ) {
        gManager.setCurrentPlayer(domino.player);
        domino.player.state = player.STATE_PLACINGDOMINO;
        currentDomino = domino;
        gManager.labelConsigne.setText("Placez votre domino.");
        System.out.println(domino);
        synchronized (lock) {
            while (domino.player.state != player.STATE_IDLE) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void newLineDominos(int n) {
        listDominos.subList(0, totalKings).sort(DominoComparator);
        gManager.newLineDominos(this,listDominos.subList(0, totalKings), n);
        for (int i = totalKings - 1; i > -1; i--) {
            System.out.println(listDominos.get(i));
            selectableDominos.set(i, listDominos.get(i));
            listDominos.remove(i);
        }
    }

    private int calculateScore(dominoPart[][]  board){
        dominoPart[][]  tempBoard = board.clone();
        int score = 0;
        for (int i = 0; i< board.length; i++){
            for (int k = 0; k < board[i].length; k++){
                if (!board[i][k].type.equals("vide")){
                    Pair<Integer,Integer> pair =calculateScoreZone(tempBoard,tempBoard[i][k],i,k);
                    score += pair.getKey()*pair.getValue();
                }
            }
        }
        boolean bonus = true;
        if (harmony){
            for (dominoPart[] column : board){
                for (dominoPart part : column){
                    if (part.type.equals("vide")){
                        bonus = false;
                    }
                }
            }
            if (bonus){
                score = score +5 ;
            }
        }
        return score;
    }
    private Pair<Integer,Integer> calculateScoreZone(dominoPart[][]  board,dominoPart part,int y , int x){
        int totalArea = 1;
        int totalCrown = part.crown;
        Pair <Integer,Integer> newPair = new Pair<>(0,0);
        String type = part.type;
        part.type = "vide";
        if(x < board[y].length-1 && board[y][x+1].type.equals(type)){
            newPair = calculateScoreZone(board,board[y][x+1],y,x+1);
        }
        if( x > 0 && board[y][x-1].type.equals(type)){
            newPair = calculateScoreZone(board,board[y][x-1],y,x-1);
        }
        if(y < board.length-1 && board[y+1][x].type.equals(type)){
            newPair = calculateScoreZone(board,board[y+1][x],y+1,x);
        }
        if(y > 0 && board[y-1][x].type.equals(type)){
            newPair = calculateScoreZone(board,board[y-1][x],y-1,x);
        }
        totalArea += newPair.getKey();
        totalCrown += newPair.getValue();
        return new Pair<>(totalArea,totalCrown);
    }

    private Comparator<domino> DominoComparator = (domino m1, domino m2) -> Integer.compare(m2.number, m1.number);
}