package game;

import UI.graphicsManager;
import javafx.util.Pair;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class gameManager {
    public enum Rule {
        MIDDLEEARTH,
        HARMONY,
        DUEL ,
        DYNASTY
    }

    private ArrayList<player> listPlayers = new ArrayList<player>();
    private ArrayList<player> listKings = new ArrayList<player>();
    private List<domino> listDominos = new ArrayList<>();
    private List<domino> selectableDominos;
    private ArrayList<domino> selectedDominos;
    private EnumSet<Rule> specialRules;
    private int totalKings;
    private graphicsManager gManager;
    private boolean ligne = false;
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

    public void newGame(ArrayList<player> list,EnumSet<Rule> rules, int m) {
        if (rules == null){
            rules = EnumSet.noneOf(gameManager.Rule.class);
        }
        if (rules.contains(gameManager.Rule.DUEL)) {
            for (player player : list) {
                player.setSize(13);
            }
            graphicsManager.sizePart = 20;
        } else {
            listDominos = listDominos.subList(0, (12 * (list.size())));
        }
        manches = m;
        gManager.manches = m;
        Collections.shuffle(listDominos);
        listPlayers = list;
        specialRules = rules;
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
            player.cumuledScore += calculateScorePlayer(player);

        }
        gManager.showScores(listPlayers, manches);
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
            while (p.currentState != player.state.DOMINOSELECTED) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        p.currentState = player.state.IDLE;
        selectedDominos.add(currentDomino);
    }

    private void placeDomino(domino domino) {
        gManager.setCurrentPlayer(domino.player);
        domino.player.currentState = player.state.PLACINGDOMINO;
        currentDomino = domino;
        gManager.labelConsigne.setText("Placez votre domino.");
        System.out.println(domino);
        synchronized (lock) {
            while (domino.player.currentState != player.state.IDLE) {
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
        gManager.newLineDominos(this, listDominos.subList(0, totalKings), n);
        for (int i = totalKings - 1; i > -1; i--) {
            System.out.println(listDominos.get(i));
            selectableDominos.set(i, listDominos.get(i));
            listDominos.remove(i);
        }
    }
    private int calculateScorePlayer(player player){
        int score = 0;
        score=calculateScoreBoard(player.board);
        if (specialRules.contains(Rule.HARMONY) && !player.litter) {
            score += 5;
        }
        if (specialRules.contains(Rule.MIDDLEEARTH)) {
            int minX = 0;
            int minY = 0;
            for (int i = player.board.length - 1; i >= 0; i--) {
                for (int k = 0; k < player.board.length; k++) {
                    if (!player.board[i][k].type.equals("vide")) {
                        minY = i;
                    }
                }
            }
            for (int i = player.board.length - 1; i >= 0; i--) {
                for (int k = 0; k < player.board.length; k++) {
                    if (!player.board[k][i].type.equals("vide")) {
                        minX = i;
                    }
                }
            }
            if (player.board[minY+(player.size-1/4)][minX+(player.size-1/4)].type.equals("Chateau")){
                score += 10;
            }

        }
        return score;
    }
    private int calculateScoreBoard(dominoPart[][] board) {
        dominoPart[][] tempBoard = board.clone();
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board[i].length; k++) {
                if (!board[i][k].type.equals("vide")) {
                    Pair<Integer, Integer> pair = calculateScoreZone(tempBoard, tempBoard[i][k], i, k);
                    score += pair.getKey() * pair.getValue();
                }
            }
        }
        boolean bonus = true;
        return score;
    }

    private Pair<Integer, Integer> calculateScoreZone(dominoPart[][] board, dominoPart part, int y, int x) {
        int totalArea = 1;
        int totalCrown = part.crown;
        Pair<Integer, Integer> newPair = new Pair<>(0, 0);
        String type = part.type;
        part.type = "vide";
        if (x < board[y].length - 1 && board[y][x + 1].type.equals(type)) {
            newPair = calculateScoreZone(board, board[y][x + 1], y, x + 1);
        }
        if (x > 0 && board[y][x - 1].type.equals(type)) {
            newPair = calculateScoreZone(board, board[y][x - 1], y, x - 1);
        }
        if (y < board.length - 1 && board[y + 1][x].type.equals(type)) {
            newPair = calculateScoreZone(board, board[y + 1][x], y + 1, x);
        }
        if (y > 0 && board[y - 1][x].type.equals(type)) {
            newPair = calculateScoreZone(board, board[y - 1][x], y - 1, x);
        }
        totalArea += newPair.getKey();
        totalCrown += newPair.getValue();
        return new Pair<>(totalArea, totalCrown);
    }


//Fonctions pour l'IA

    //cette fonction sert à déterminer, pour un doublet de dominos, combien ce doublet peut rapporter au max à un joueur et toutes les coordonnées possibles correspondant à ce score
    private ArrayList<ArrayList<Integer>> pointsDoublet(domino domino1, domino domino2, player player) {
        ArrayList<ArrayList<Integer>> positionsPossiblesDomino1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> bestPositions = new ArrayList<>();
        int scorePrecedent = calculateScore(player.board);
        //vérifier que x2 et y2 ne sont pas à l'extérieur du terrain (quand x1 ou y1 sont à la limite)
        for (int x1 = 0; x1 < player.board.length; x1++) {
            for (int y1 = 0; y1 < player.board.length; y1++) {
                for (int x2 = x1 - 1; x2 < x1 + 1; x2++) {
                    for (int y2 = y1 - 1; y2 < y1 + 1; y2++) {
                        if (x1 != x2 || y1 != y2) {
                            if (x1 == x2 || y1 == y2) {
                                if (domino1.canBePlaced(x1, y1, x2, y2, player.board) == true) {
                                    ArrayList<Integer> positionPossible = new ArrayList<Integer>();
                                    positionPossible.add(x1);
                                    positionPossible.add(y1);
                                    positionPossible.add(x2);
                                    positionPossible.add(y2);
                                    positionsPossiblesDomino1.add(positionPossible);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < positionsPossiblesDomino1.size(); i++) {
            dominoPart[][] boardCopy = player.board;
            boardCopy[positionsPossiblesDomino1.get(i).get(1)][positionsPossiblesDomino1.get(i).get(0)]= domino1.part1;
            boardCopy[positionsPossiblesDomino1.get(i).get(3)][positionsPossiblesDomino1.get(i).get(2)]= domino1.part2;
            //Ajouter sur boardCopy le domino1 en coords x1,y1,x2,y2 dans la liste positionsPossiblesDomino1
            for (int x1 = 0; x1 < boardCopy.length; x1++) {
                for (int y1 = 0; y1 < boardCopy.length; y1++) {
                    for (int x2 = x1 - 1; x2 < x1 + 1; x2++) {
                        for (int y2 = y1 - 1; y2 < y1 + 1; y2++) {
                            if (x1 != x2 || y1 != y2) {
                                if (x1 == x2 || y1 == y2) {
                                    if (domino2.canBePlaced(x1, y1, x2, y2, player.board) == true) {
                                        int score = calculateScore(boardCopy) - scorePrecedent;
                                        ArrayList<Integer> positionsDominos1et2 = new ArrayList<Integer>();
                                        positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(0));
                                        positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(1));
                                        positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(2));
                                        positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(3));
                                        positionsDominos1et2.add(x1);
                                        positionsDominos1et2.add(y1);
                                        positionsDominos1et2.add(x2);
                                        positionsDominos1et2.add(y2);
                                        positionsDominos1et2.add(score);
                                        if(bestPositions.size()==0 || bestPositions.get(0).get(8)==score){
                                            bestPositions.add(positionsDominos1et2);
                                        }
                                        if(bestPositions.get(0).get(8)<score){
                                            bestPositions.clear();
                                            bestPositions.add(positionsDominos1et2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestPositions; //dans bestpositions on a les toutes les coordonnées de domino1 et domino2 (supposant que domino1 a été placé en premier) qui correspondent au meilleur score possible du doublet, et ce score est également dedans
        //donc on a une arraylist d'arraylists dans lesquels on a (x1,y1,x2,y2[pour domino1],x1,y1,x2,y2[pour domino2],score)
    }

    //cette fonction va servir à dire, pour chaque doublet de dominos, quelle sera la différence de score avec l'adversaire (il aura forcément les deux autres)
    private ArrayList<Object[]> differenceDoubletsAvecAdversaire (ArrayList listPlayers, ArrayList selectableDominos ){
        ArrayList<Object[]> listeDifferencesDoublets = new ArrayList<>();
        for(int i=0;i<selectableDominos.size()-1;i++){
            for(int j=i+1;j<selectableDominos.size();j++){
                int scorePourMoi = pointsDoublet((domino) selectableDominos.get(i),(domino) selectableDominos.get(j), (player) listPlayers.get(0)).get(0).get(8);
                int scorePourAdv = 0;
                int placeDomino1Adv = 0;
                int placeDomino2Adv = 0;
                //je considère que je (l'IA) est le joueur 0
                for(int k=0;k<selectableDominos.size()-1;k++){
                    for(int l=k+1;l<selectableDominos.size();l++){
                        if(k!=i && k!=j && l!=i && l!=j){
                            scorePourAdv = pointsDoublet((domino) selectableDominos.get(k),(domino) selectableDominos.get(l), (player) listPlayers.get(1)).get(0).get(8);
                            placeDomino1Adv = k;
                            placeDomino2Adv = l;

                        }
                    }
                }
                int difference = scorePourMoi - scorePourAdv;
                Object [] doubletEtDifference = new Object[5];
                doubletEtDifference[0]=selectableDominos.get(i);
                doubletEtDifference[1]=selectableDominos.get(j);
                doubletEtDifference[2]=selectableDominos.get(placeDomino1Adv);
                doubletEtDifference[3]=selectableDominos.get(placeDomino2Adv);
                doubletEtDifference[4]=difference;
                listeDifferencesDoublets.add(doubletEtDifference);

            }
        }
        return listeDifferencesDoublets;

    }

    private ArrayList<Object[]> triListeDoublets(){
        ArrayList<Object[]> liste = differenceDoubletsAvecAdversaire(listPlayers,(ArrayList) selectableDominos);
        int i=0;
        while( i<liste.size()-1 ){
            if( (int) liste.get(i)[4] < (int) liste.get(i+1)[4]){
                Object[] copy = liste.get(i);
                liste.remove(i) ;
                liste.add(copy);
                i=i-1;
            }
            i++;
        }
        return liste;
        //return la liste de doublets triée de manière décroissante selon la différence
    }

    private Comparator<domino> DominoComparator = (domino m1, domino m2) -> Integer.compare(m2.number, m1.number);
}