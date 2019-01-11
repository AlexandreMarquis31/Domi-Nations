package game;

import UI.Application;
import UI.GraphicsManager;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameManager {
    public enum Rule {
        MIDDLEEARTH,
        HARMONY,
        DUEL,
        DYNASTY
    }

    public static ArrayList<Player> listPlayers = new ArrayList<>();
    private ArrayList<Player> listKings = new ArrayList<>();
    private List<Domino> listDominos = new ArrayList<>();
    private List<Domino> selectableDominos;
    private ArrayList<Domino> selectedDominos;
    public static EnumSet<Rule> specialRules;
    private int totalKings;
    private GraphicsManager gManager;
    private boolean ligne = false;
    public Player currentPlayer = null;
    public Domino currentDomino = null;
    public static final Object lock = new Object();

    public GameManager(GraphicsManager g) {
        gManager = g;
        importDominos();
        Collections.shuffle(listDominos);
    }

    private void importDominos() {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(new File(GameManager.class.getClassLoader().getResource("dominos.csv").getPath())));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] infos = line.split(cvsSplitBy);
                listDominos.add(new Domino(new DominoPart(infos[1], Integer.parseInt(infos[0])), new DominoPart(infos[3], Integer.parseInt(infos[2])), Integer.parseInt(infos[4])));
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

    public void newGame(ArrayList<Player> list, EnumSet<Rule> rules) {
        if (rules == null) {
            rules = EnumSet.noneOf(GameManager.Rule.class);
        }
        if (rules.contains(GameManager.Rule.DUEL)) {
            for (Player player : list) {
                player.newBoard(13);
            }
            GraphicsManager.sizePart = 20;
        } else {
            listDominos = listDominos.subList(0, (12 * (list.size())));
            GraphicsManager.sizePart = 30;
        }
        if (rules.contains(Rule.DYNASTY)) {
            Application.getInstance().manches = 3;
        }
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
        selectableDominos = Arrays.asList(new Domino[totalKings]);
        Collections.shuffle(listKings);
        gManager.setPlayersUI(listPlayers);
        start();
    }

    private void start() {
        newLineDominos(ligne ? 1 : 0);
        ligne = !ligne;
        for (Player player : listKings) {
            currentPlayer = player;
            gManager.setCurrentPlayer(player);
            chooseDomino(player);
        }
        while (listDominos.size() > 0) {
            newTurn();
        }
        selectedDominos.sort(DominoComparator);
        for (Domino domino : selectedDominos) {
            currentPlayer = domino.player;
            gManager.setCurrentPlayer(domino.player);
            placeDomino(domino);
        }
        for (Player player : listPlayers) {
            player.cumuledScore += calculateScorePlayer(player);
        }
        gManager.showScores(listPlayers);
    }

    private void newTurn() {
        System.out.println(listDominos.size());
        ArrayList<Domino> dominoToPlace = new ArrayList<>(selectedDominos);
        dominoToPlace.sort(DominoComparator);
        newLineDominos(ligne ? 1 : 0);
        ligne = !ligne;
        selectedDominos.clear();
        for (Domino domino : dominoToPlace) {
            currentPlayer = domino.player;
            placeDomino(domino);
            chooseDomino(domino.player);
        }
    }

    private void chooseDomino(Player p) {
        gManager.labelConsigne.setText("Choisissez votre Domino.");
        if (p.ia) {
            Player adv = null;
            if (listPlayers.size() ==2){
                if (listPlayers.get(0) == p) adv = listPlayers.get(1);
                if (listPlayers.get(1) == p) adv = listPlayers.get(0);
            }
            Domino dominoSelect = dominoSelection(p,adv);
            System.out.println(dominoSelect);
            dominoSelect.player = p;
            currentDomino = dominoSelect;
        } else {
            synchronized (lock) {
                while (p.currentState != Player.state.DOMINOSELECTED) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        p.currentState = Player.state.IDLE;
        selectedDominos.add(currentDomino);
    }

    private void placeDomino(Domino domino) {
        gManager.setCurrentPlayer(domino.player);
        domino.player.currentState = Player.state.PLACINGDOMINO;
        currentDomino = domino;
        gManager.labelConsigne.setText("Placez votre Domino.");
        if (domino.player.ia){
            ArrayList<Integer> coords = placeDominoIA(domino);
            domino.player.board[coords.get(1)][coords.get(0)] = domino.part1;
            domino.player.board[coords.get(2)][coords.get(3)] = domino.part2;
            domino.player .currentState = Player.state.IDLE;
        } else {
            synchronized (lock) {
                while (domino.player.currentState != Player.state.IDLE) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

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

    private int calculateScorePlayer(Player player) {
        int score;
        score = calculateScoreBoard(player.board);
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
            if (player.board[minY + (player.size - 1 / 4)][minX + (player.size - 1 / 4)].type.equals("Chateau")) {
                score += 10;
            }
        }
        return score;
    }

    private int calculateScoreBoard(DominoPart[][] board) {
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board[i].length; k++) {
                if (!board[i][k].type.equals("vide") && !board[i][k].counted) {
                    Pair<Integer, Integer> pair = calculateScoreZone(board, board[i][k], i, k);
                    score += pair.getKey() * pair.getValue();
                    //System.out.println(board[i][k].type + "   -   "+pair.getKey() * pair.getValue());
                }
            }
        }
        for (DominoPart[] dominoParts : board) {
            for (DominoPart dominoPart : dominoParts) {
                dominoPart.counted = false;
            }
        }
        return score;
    }

    //calculate the score of a certain zone , return the number of cases and the number of crowns
    private Pair<Integer, Integer> calculateScoreZone(DominoPart[][] board, DominoPart part, int y, int x) {
        int totalArea = 1;
        int totalCrown = part.crown;
        Pair<Integer, Integer> newPair = new Pair<>(0, 0);
        String type = part.type;
        part.counted = true;
        if (x < board[y].length - 1 && board[y][x + 1].type.equals(type) && !board[y][x + 1].counted) {
            newPair = calculateScoreZone(board, board[y][x + 1], y, x + 1);
        }
        if (x > 0 && board[y][x - 1].type.equals(type) && !board[y][x - 1].counted) {
            newPair = calculateScoreZone(board, board[y][x - 1], y, x - 1);
        }
        if (y < board.length - 1 && board[y + 1][x].type.equals(type) && !board[y + 1][x].counted) {
            newPair = calculateScoreZone(board, board[y + 1][x], y + 1, x);
        }
        if (y > 0 && board[y - 1][x].type.equals(type) && !board[y - 1][x].counted) {
            newPair = calculateScoreZone(board, board[y - 1][x], y - 1, x);
        }
        totalArea += newPair.getKey();
        totalCrown += newPair.getValue();
        return new Pair<>(totalArea, totalCrown);
    }


//Fonctions pour l'IA

    private ArrayList<ArrayList<Integer>> meilleuresPositionsDomino(Domino domino, Player player) {
        ArrayList<ArrayList<Integer>> meilleuresPositionsDomino = new ArrayList<>();
        ArrayList<ArrayList<Integer>> positionsAvecTrous = new ArrayList<>();
        DominoPart[][] boardCopy = new DominoPart[player.board.length][player.board.length];
        for (int m = 0; m < player.board.length; m++) {
            for (int k = 0; k < player.board.length; k++) {
                boardCopy[m][k] = new DominoPart(player.board[m][k].type, player.board[m][k].crown);
            }
        }
        for (int x1 = 0; x1 < player.board.length; x1++) {
            for (int y1 = 0; y1 < player.board.length; y1++) {
                for (int x2 = x1 - 1; x2 < x1 + 1; x2++) {
                    for (int y2 = y1 - 1; y2 < y1 + 1; y2++) {
                        if (x2 < player.board.length && x2 >= 0 && y2 < player.board.length && y2 >= 0) {
                            if (x1 != x2 || y1 != y2) {
                                if (x1 == x2 || y1 == y2) {
                                    if (domino.canBePlaced(x1, y1, x2, y2, boardCopy)) {
                                        boardCopy[y1][x1] = new DominoPart(domino.part1.type, domino.part1.crown);
                                        boardCopy[y2][x2] = new DominoPart(domino.part2.type, domino.part2.crown);
                                        int score = calculateScoreBoard(boardCopy);
                                        ArrayList<Integer> positionPossibleDomino = new ArrayList<Integer>();
                                        positionPossibleDomino.add(score);
                                        positionPossibleDomino.add(x1);
                                        positionPossibleDomino.add(y1);
                                        positionPossibleDomino.add(x2);
                                        positionPossibleDomino.add(y2);
                                        if(verifPasDeTrou(boardCopy)==true) {
                                            meilleuresPositionsDomino.add(positionPossibleDomino);
                                        } else {
                                            positionsAvecTrous.add(positionPossibleDomino);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<Integer>> liste = new ArrayList<>(meilleuresPositionsDomino);
        if(meilleuresPositionsDomino.size()==0) {
            liste = new ArrayList<>(positionsAvecTrous);
        }
        int i = 0;
        while (i < liste.size() - 1) {
            if (liste.get(i).get(0) < liste.get(i + 1).get(0)) {
                ArrayList<Integer> copy = liste.get(i);
                liste.remove(i);
                liste.add(copy);
                i = i - 1;
            }
            i++;
        }
        ArrayList<ArrayList<Integer>> liste2 = liste;
        for (ArrayList<Integer> integers : liste) {
            if (integers.get(0) < liste.get(0).get(0)) {
                liste2.remove(integers);
            }
        }
        return liste2; //return la liste de toutes les meilleures positions correspondant au meilleur score pour le joueur
    }


    private ArrayList<ArrayList<Integer>> pointsDoublet(Domino domino1, Domino domino2, Player player) {
        ArrayList<ArrayList<Integer>> positionsPossiblesDomino1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> bestPositions = new ArrayList<>();
        for (int x1 = 0; x1 < player.board.length; x1++) {
            for (int y1 = 0; y1 < player.board.length; y1++) {
                for (int x2 = x1 - 1; x2 < x1 + 1; x2++) {
                    for (int y2 = y1 - 1; y2 < y1 + 1; y2++) {
                        if (x2 < player.board.length && x2 >= 0 && y2 < player.board.length && y2 >= 0) {
                            if (x1 != x2 || y1 != y2) {
                                if (x1 == x2 || y1 == y2) {
                                    if (domino1.canBePlaced(x1, y1, x2, y2, player.board) && verifPasDeTrou(player.board)) {
                                        ArrayList<Integer> positionPossible = new ArrayList<>();
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
        }
        for (int i = 0; i < positionsPossiblesDomino1.size(); i++) {
            DominoPart[][] boardCopy = new DominoPart[player.board.length][player.board.length];
            for (int m = 0; m < player.board.length; m++) {
                for (int k = 0; k < player.board.length; k++) {
                    boardCopy[m][k] = new DominoPart(player.board[m][k].type, player.board[m][k].crown);
                }
            }
            boardCopy[positionsPossiblesDomino1.get(i).get(1)][positionsPossiblesDomino1.get(i).get(0)] = new DominoPart(domino1.part1.type, domino1.part1.crown);
            boardCopy[positionsPossiblesDomino1.get(i).get(3)][positionsPossiblesDomino1.get(i).get(2)] = new DominoPart(domino1.part2.type, domino1.part2.crown);
            //Ajouter sur boardCopy le domino1 en coords x1,y1,x2,y2 dans la liste positionsPossiblesDomino1
            for (int x1 = 0; x1 < boardCopy.length; x1++) {
                for (int y1 = 0; y1 < boardCopy.length; y1++) {
                    for (int x2 = x1 - 1; x2 < x1 + 1; x2++) {
                        for (int y2 = y1 - 1; y2 < y1 + 1; y2++) {
                            if (x2 < player.board.length && x2 >= 0 && y2 < player.board.length && y2 >= 0) {
                                if (x1 != x2 || y1 != y2) {
                                    if (x1 == x2 || y1 == y2) {
                                        if (domino2.canBePlaced(x1, y1, x2, y2, boardCopy) && verifPasDeTrou(boardCopy)) {
                                            DominoPart[][] boardCopy2 = new DominoPart[player.board.length][player.board.length];
                                            for (int j = 0; j < player.board.length; j++) {
                                                for (int h = 0; h < player.board.length; h++) {
                                                    boardCopy2[j][h] = new DominoPart(boardCopy[j][h].type, boardCopy[j][h].crown);
                                                }
                                            }
                                            boardCopy2[y1][x1] = new DominoPart(domino2.part1.type, domino2.part1.crown);
                                            boardCopy2[y2][x2] = new DominoPart(domino2.part2.type, domino2.part2.crown);
                                            //on a bien ajouté le domino2 sur le board copié où il y avait le domino1
                                            int score = calculateScoreBoard(boardCopy2);
                                            ArrayList<Integer> positionsDominos1et2 = new ArrayList<>();
                                            positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(0));
                                            positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(1));
                                            positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(2));
                                            positionsDominos1et2.add(positionsPossiblesDomino1.get(i).get(3));
                                            positionsDominos1et2.add(x1);
                                            positionsDominos1et2.add(y1);
                                            positionsDominos1et2.add(x2);
                                            positionsDominos1et2.add(y2);
                                            positionsDominos1et2.add(score);
                                            if (bestPositions.size() == 0 || bestPositions.get(0).get(8) == score) {
                                                bestPositions.add(positionsDominos1et2);
                                            }
                                            if (bestPositions.get(0).get(8) < score) {
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
        }
        //System.out.println(bestPositions);
        return bestPositions; //dans bestpositions on a les toutes les coordonnées de domino1 et domino2 (supposant que domino1 a été placé en premier) qui correspondent au meilleur score possible du doublet, et ce score est également dedans
        //donc on a une arraylist d'arraylists dans lesquels on a (x1,y1,x2,y2[pour domino1],x1,y1,x2,y2[pour domino2],score)
    }

    //cette fonction va servir à dire, pour chaque doublet de dominos, quelle sera la différence de score avec l'adversaire (il aura forcément les deux autres)
    private ArrayList<Object[]> differenceDoubletsAvecAdversaire(Player IA, Player adversaire) {
        ArrayList<Object[]> listeDifferencesDoublets = new ArrayList<>();
        for (int i = 0; i < selectableDominos.size() - 1; i++) {
            for (int j = i + 1; j < selectableDominos.size(); j++) {
                int scorePourMoi = pointsDoublet(selectableDominos.get(i),selectableDominos.get(j),IA).get(0).get(8);
                int scorePourAdv = 0;
                int placeDomino1Adv = 0;
                int placeDomino2Adv = 0;
                //je considère que je (l'IA) est le joueur 0
                for (int k = 0; k < selectableDominos.size() - 1; k++) {
                    for (int l = k + 1; l < selectableDominos.size(); l++) {
                        if (k != i && k != j && l != i && l != j) {
                            scorePourAdv = pointsDoublet( selectableDominos.get(k),selectableDominos.get(l), adversaire).get(0).get(8);
                            placeDomino1Adv = k;
                            placeDomino2Adv = l;

                        }
                    }
                }
                int difference = scorePourMoi - scorePourAdv;
                Object[] doubletEtDifference = new Object[5];
                doubletEtDifference[0] = selectableDominos.get(i);
                doubletEtDifference[1] = selectableDominos.get(j);
                doubletEtDifference[2] = selectableDominos.get(placeDomino1Adv);
                doubletEtDifference[3] = selectableDominos.get(placeDomino2Adv);
                doubletEtDifference[4] = difference;
                listeDifferencesDoublets.add(doubletEtDifference);

            }
        }
        return listeDifferencesDoublets;

    }

    private ArrayList<Object[]> triListeDoublets(Player IA, Player adversaire) {
        ArrayList<Object[]> liste = differenceDoubletsAvecAdversaire(IA, adversaire);
        int i = 0;
        while (i < liste.size() - 1) {
            if ((int) liste.get(i)[4] < (int) liste.get(i + 1)[4]) {
                Object[] copy = liste.get(i);
                liste.remove(i);
                liste.add(copy);
                i = i - 1;
            }
            i++;
        }
        return liste;
        //return la liste de doublets triée de manière décroissante selon la différence
    }

    //cette fonction renvoie une liste entre 1 et 4 (inclus) dominos qui restent à être joués
    private ArrayList<Player> pickOrder() {
        ArrayList<Domino> selectedDominosRestants = new ArrayList<>(selectedDominos);
        ArrayList<Player> pickOrder = new ArrayList<>();
        for (int i = 0; i < selectedDominos.size(); i++) {
            for (int j = 0; j < selectableDominos.size(); j++) {
                if (selectedDominos.get(i) == selectableDominos.get(j)) {
                    selectedDominosRestants.remove(selectedDominos.get(i));
                }
            }
        }
        //trier la liste des dominos à jouer restants en fonction de leur numéro grâce au DominoComparator
        selectedDominosRestants.sort(DominoComparator);
        for (int k = 0; k < selectedDominosRestants.size(); k++) {
            pickOrder.add(selectedDominosRestants.get(k).player);
        }
        return pickOrder;
    }

    //cette fonction renvoie le Domino à sélectionner parmi ceux restants une fois qu'on a joué son tour
    //on considère qu'on ne l'appelle que quand c'est au tour de l'IA de jouer
    private Domino dominoSelection(Player IA, Player adversaire) {
        ArrayList<Object[]> listeDoublets = triListeDoublets(IA, adversaire);
        ArrayList<Player> pickOrder = pickOrder();
        ArrayList<Domino> selectedDominosRestants = selectedDominos;
        List<Domino> selectableDominosRestants = new ArrayList<Domino>();
        List<Domino> selectableDominosDejaPris = new ArrayList<Domino>();

        for (int i = 0; i < selectableDominos.size(); i++) { //création de deux listes : les dominos qui ont déjà été choisis et ceux encore libres
            if (selectableDominos.get(i).player == null) {
                selectableDominosRestants.add(selectableDominos.get(i));
            } else {
                selectableDominosDejaPris.add(selectableDominos.get(i));
            }
        }
        ArrayList<Object[]> listeDoubletsRestants = new ArrayList<Object[]>(listeDoublets);
        //on va maintenant essayer de supprimer les options qui ne sont plus possibles
        for (int i = 0; i < listeDoublets.size(); i++) {
            if (((Domino) listeDoublets.get(i)[0]).player == adversaire || ((Domino) listeDoublets.get(i)[1]).player == adversaire) {
                listeDoubletsRestants.remove(listeDoublets.get(i));
            }
        }
        ArrayList<Object[]> listeDoubletsRestantsTailleDouble = new ArrayList<>();
        for (Object[] listeDoubletsRestant : listeDoubletsRestants) {
            Object[] doublet = new Object[2];
            Object[] doubletInverse = new Object[2];
            doublet[0] = listeDoubletsRestant[0];
            doublet[1] = listeDoubletsRestant[1];
            doubletInverse[0] = doublet[1];
            doubletInverse[1] = doublet[0];
            listeDoubletsRestantsTailleDouble.add(doublet);
            listeDoubletsRestantsTailleDouble.add(doubletInverse);
        }
        switch (pickOrder.size()) {
            case 1:
                return selectableDominosRestants.get(0);
            case 2:
                for (int i = 0; i < listeDoubletsRestants.size(); i++) {
                    for (int j = 0; i < selectableDominosRestants.size(); j++) {
                        if (listeDoubletsRestants.get(i)[0] == selectableDominosRestants.get(j) || listeDoubletsRestants.get(i)[1] == selectableDominosRestants.get(j)) {
                            return selectableDominosRestants.get(j);
                        }
                    }
                }
                return selectableDominosRestants.get(0); //dans le cas où on n'a rien de viable. Voir pour prendre celui qui est le plus profitable à l'autre
            case 3:
                //on commence par regarder le cas où on a pick en premier et dans ce cas on a juste a compléter le doublet
                if (selectableDominosDejaPris.get(0).player == IA) {
                    return (Domino) listeDoubletsRestants.get(0)[1];
                } else {
                    if (pickOrder.get(0) == pickOrder.get(1)) {
                        return (Domino) listeDoubletsRestants.get(0)[0];
                    } else {//Objectifs :
                        //faire une fonction qui calcule le Domino restant le plus profitable pour l'adversaire, trier par intérêt pour adversaire dans une liste
                        //enlever de ma listeDoubletsRestants les doublets où je prends en 2e ce qu'il va prendre en 1er et celui où je prends en 1er ce qu'il veut prendre en 1er et en 2e ce qu'il veut prendre en 2e
                        //prendre le premier Domino de mon premier doublet de listeDoubletsRestants
                        int domino1ScoreAdv = meilleuresPositionsDomino(selectableDominosRestants.get(0), adversaire).get(0).get(0);
                        int domino2ScoreAdv = meilleuresPositionsDomino(selectableDominosRestants.get(1), adversaire).get(0).get(0);
                        int domino3ScoreAdv = meilleuresPositionsDomino(selectableDominosRestants.get(2), adversaire).get(0).get(0);
                        ArrayList<Object[]> listeDominosScorePourAdv = new ArrayList<>();
                        Object[] doubletDomino1ScoreAdv = new Object[2];
                        Object[] doubletDomino2ScoreAdv = new Object[2];
                        Object[] doubletDomino3ScoreAdv = new Object[2];
                        doubletDomino1ScoreAdv[0] = selectableDominosRestants.get(0);
                        doubletDomino1ScoreAdv[1] = domino1ScoreAdv;
                        doubletDomino2ScoreAdv[0] = selectableDominosRestants.get(1);
                        doubletDomino2ScoreAdv[1] = domino2ScoreAdv;
                        doubletDomino3ScoreAdv[0] = selectableDominosRestants.get(2);
                        doubletDomino3ScoreAdv[1] = domino3ScoreAdv;
                        listeDominosScorePourAdv.add(doubletDomino1ScoreAdv);
                        listeDominosScorePourAdv.add(doubletDomino2ScoreAdv);
                        listeDominosScorePourAdv.add(doubletDomino3ScoreAdv);
                        ArrayList<Object[]> listeDominosScorePourAdvTriee = listeDominosScorePourAdv;
                        int k = 0;
                        while (k < listeDominosScorePourAdvTriee.size() - 1) {
                            if ((int) listeDominosScorePourAdvTriee.get(k)[1] < (int) listeDominosScorePourAdvTriee.get(k + 1)[1]) {
                                Object[] copy = listeDominosScorePourAdvTriee.get(k);
                                listeDominosScorePourAdvTriee.remove(k);
                                listeDominosScorePourAdvTriee.add(copy);
                                k = k - 1;
                            }
                            k++;
                        } //on a maintenant (le code est pas terrible, j'aurais pu simplifier aevc des boucles) la liste des dominos restants en ordre décroissant d'intérêt pour l'adversaire

                        for (int i = 0; i < listeDoubletsRestantsTailleDouble.size(); i++) {
                            if (listeDoubletsRestantsTailleDouble.get(i)[1] == listeDominosScorePourAdvTriee.get(0)[0]) {
                                listeDoubletsRestantsTailleDouble.remove(listeDoubletsRestants.get(i));
                            }
                            if (listeDoubletsRestantsTailleDouble.get(i)[0] == listeDominosScorePourAdvTriee.get(0)[0] && listeDoubletsRestantsTailleDouble.get(i)[1] == listeDominosScorePourAdvTriee.get(1)[0]) {
                                listeDoubletsRestantsTailleDouble.remove(listeDoubletsRestants.get(i));
                            }
                        }
                        return (Domino) listeDoubletsRestantsTailleDouble.get(0)[0];


                    }
                }
            case 4:
                if (pickOrder.get(0) == pickOrder.get(1)) { //on est dans le cas MMAA
                    return (Domino) listeDoubletsRestants.get(0)[0];
                }
                int domino1ScoreAdv = meilleuresPositionsDomino(selectableDominosRestants.get(0), adversaire).get(0).get(0);
                int domino2ScoreAdv = meilleuresPositionsDomino(selectableDominosRestants.get(1), adversaire).get(0).get(0);
                int domino3ScoreAdv = meilleuresPositionsDomino(selectableDominosRestants.get(2), adversaire).get(0).get(0);
                int domino4ScoreAdv = meilleuresPositionsDomino(selectableDominosRestants.get(2), adversaire).get(0).get(0);
                ArrayList<Object[]> listeDominosScorePourAdv = new ArrayList<>();
                Object[] doubletDomino1ScoreAdv = new Object[2];
                Object[] doubletDomino2ScoreAdv = new Object[2];
                Object[] doubletDomino3ScoreAdv = new Object[2];
                Object[] doubletDomino4ScoreAdv = new Object[2];
                doubletDomino1ScoreAdv[0] = selectableDominosRestants.get(0);
                doubletDomino1ScoreAdv[1] = domino1ScoreAdv;
                doubletDomino2ScoreAdv[0] = selectableDominosRestants.get(1);
                doubletDomino2ScoreAdv[1] = domino2ScoreAdv;
                doubletDomino3ScoreAdv[0] = selectableDominosRestants.get(2);
                doubletDomino3ScoreAdv[1] = domino3ScoreAdv;
                doubletDomino4ScoreAdv[0] = selectableDominosRestants.get(3);
                doubletDomino4ScoreAdv[1] = domino4ScoreAdv;
                listeDominosScorePourAdv.add(doubletDomino1ScoreAdv);
                listeDominosScorePourAdv.add(doubletDomino2ScoreAdv);
                listeDominosScorePourAdv.add(doubletDomino3ScoreAdv);
                listeDominosScorePourAdv.add(doubletDomino4ScoreAdv);
                ArrayList<Object[]> listeDominosScorePourAdvTriee = listeDominosScorePourAdv;
                int k = 0;
                while (k < listeDominosScorePourAdvTriee.size() - 1) {
                    if ((int) listeDominosScorePourAdvTriee.get(k)[1] < (int) listeDominosScorePourAdvTriee.get(k + 1)[1]) {
                        Object[] copy = listeDominosScorePourAdvTriee.get(k);
                        listeDominosScorePourAdvTriee.remove(k);
                        listeDominosScorePourAdvTriee.add(copy);
                        k = k - 1;
                    }
                    k++;
                }
                if (pickOrder.get(1) == pickOrder.get(2)) { //on est dans le cas MAAM
                    for (int i = 0; i < listeDoubletsRestantsTailleDouble.size(); i++) {
                        if (listeDoubletsRestantsTailleDouble.get(i)[1] == listeDominosScorePourAdvTriee.get(0)[0]) {
                            listeDoubletsRestantsTailleDouble.remove(listeDoubletsRestantsTailleDouble.get(i));
                        }
                        if (listeDoubletsRestantsTailleDouble.get(i)[0] == listeDominosScorePourAdvTriee.get(0)[0] && listeDoubletsRestantsTailleDouble.get(i)[1] == listeDominosScorePourAdvTriee.get(1)[0]) {
                            listeDoubletsRestantsTailleDouble.remove(listeDoubletsRestantsTailleDouble.get(i));
                        }
                        if (listeDoubletsRestantsTailleDouble.get(i)[0] == listeDominosScorePourAdvTriee.get(1)[0] && listeDoubletsRestantsTailleDouble.get(i)[1] == listeDominosScorePourAdvTriee.get(2)[0]) {
                            listeDoubletsRestantsTailleDouble.remove(listeDoubletsRestantsTailleDouble.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < listeDoubletsRestantsTailleDouble.size(); i++) {
                        if (listeDoubletsRestantsTailleDouble.get(i)[1] == listeDominosScorePourAdvTriee.get(0)[0]) {
                            listeDoubletsRestantsTailleDouble.remove(listeDoubletsRestantsTailleDouble.get(i));
                        }
                        if (listeDoubletsRestantsTailleDouble.get(i)[0] == listeDominosScorePourAdvTriee.get(0)[0] && listeDoubletsRestantsTailleDouble.get(i)[1] == listeDominosScorePourAdvTriee.get(1)[0]) {
                            listeDoubletsRestantsTailleDouble.remove(listeDoubletsRestantsTailleDouble.get(i));
                        }
                    }
                    return (Domino) listeDoubletsRestantsTailleDouble.get(0)[0];
                }
                return selectableDominosRestants.get(0); //dans le cas
        }
        ArrayList<Object[]> meilleursDominosAdversaire = new ArrayList<>();
        for(int i = 0 ; i<selectableDominosRestants.size() ; i++){
            ArrayList<ArrayList<Integer>> scoreDominoAdversaire = meilleuresPositionsDomino(selectableDominosRestants.get(i),adversaire);
            Object[] scoreDominoAdversaire2 = new Object[3];
            scoreDominoAdversaire2[0] = selectableDominosRestants.get(i);
            scoreDominoAdversaire2[1] = scoreDominoAdversaire.get(0);
            meilleursDominosAdversaire.add(scoreDominoAdversaire2);
        }
        int p = 0;
        while(p<meilleursDominosAdversaire.size()-1){
            if( (int) meilleursDominosAdversaire.get(p)[1]< (int) meilleursDominosAdversaire.get(p+1)[1]){
                Object[] copy = meilleursDominosAdversaire.get(p);
                meilleursDominosAdversaire.remove(copy);
                meilleursDominosAdversaire.add(copy);
                p--;
            }
        }

        if(meilleursDominosAdversaire.size() != 0){
            return (Domino) meilleursDominosAdversaire.get(0)[0];
        }
        return selectableDominosRestants.get(0);
    }


    private Comparator<Domino> DominoComparator = (Domino m1, Domino m2) -> Integer.compare(m2.number, m1.number);

    private boolean verifPasDeTrou(DominoPart[][] board) {
       for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                if (board[x][y].type.equals("vide")) {
                    if ((x+1<board.length &&!board[x + 1][y].type.equals("vide"))|| Domino.biggerThanBoard(x, y, x + 1, y, board) || x == board.length - 1) {
                        if ((x>0 &&!board[x - 1][y].type.equals("vide")) || Domino.biggerThanBoard(x, y, x - 1, y, board) || x == 0) {
                            if ((y>0 && !board[x][y - 1].type.equals("vide") )|| Domino.biggerThanBoard(x, y, x, y - 1, board) || y == 0) {
                                if ((y+1<board.length && !board[x][y + 1].type.equals("vide") )|| Domino.biggerThanBoard(x, y, x, y + 1, board) || y == board.length - 1) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private Domino dominoSuivantSelected (Player player, Domino dominoToPlace){
        ArrayList<Domino> dominosSelectedForPlayer = new ArrayList<Domino>();
        for(int i = 0; i<selectedDominos.size() ; i++){
            if(selectedDominos.get(i).player == player){
                dominosSelectedForPlayer.add(selectableDominos.get(i));
            }
        }
        if(dominosSelectedForPlayer.get(0)==dominoToPlace){
            return dominosSelectedForPlayer.get(1);
        }
        return dominosSelectedForPlayer.get(0);
    }

    private ArrayList<Integer> placeDominoIA(Domino dominoToPlace){
        Domino domino2 = dominoSuivantSelected (dominoToPlace.player, dominoToPlace);
        ArrayList<ArrayList<Integer>> coordonneesDoublets = pointsDoublet(dominoToPlace,domino2,dominoToPlace.player);
        ArrayList<Integer> coordonneesDominoToPlace = new ArrayList<>();
        if(coordonneesDoublets.size()>0) {
            for (int i = 0; i < 4; i++) {
                coordonneesDominoToPlace.add(coordonneesDoublets.get(0).get(i));
            }
            return coordonneesDominoToPlace;
        }
        else{
            ArrayList<ArrayList<Integer>> listeCoordonneesSiPasDeDoublet = meilleuresPositionsDomino(dominoToPlace, dominoToPlace.player);
            ArrayList<Integer> coordonneesSiPasDeDoublet = new ArrayList<>();
            if(listeCoordonneesSiPasDeDoublet.size()==0){
                ArrayList<Integer> listePoubelle = new ArrayList<>();
                listePoubelle.add(-1);
                listePoubelle.add(-1);
                listePoubelle.add(-1);
                listePoubelle.add(-1);
                return listePoubelle;
            }
            for(int i = 1 ; i<5 ; i++){
                coordonneesSiPasDeDoublet.add(listeCoordonneesSiPasDeDoublet.get(0).get(i));
            }
            return coordonneesSiPasDeDoublet;

        }
    }
}