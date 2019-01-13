package game;

import IA.IA_Basique;
import UI.Application;
import UI.DominoUI;
import UI.GraphicsManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class GameManager {
    private BufferedReader reader;
    public enum Rule {
        MIDDLEEARTH,
        HARMONY,
        DUEL,
        DYNASTY
    }

    public static ArrayList<Player> listPlayers = new ArrayList<>();
    private final ArrayList<Player> listKings = new ArrayList<>();
    private List<Domino> listDominos = new ArrayList<>();
    private List<Domino> selectableDominos;
    private ArrayList<Domino> selectedDominos;
    public static EnumSet<Rule> specialRules;
    private int totalKings;
    private final GraphicsManager gManager;
    private boolean line = false;
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
            InputStream in = getClass().getResourceAsStream("dominos.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
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
            GraphicsManager.sizePart = Application.getInstance().getWidth()/40;
        } else {
            listDominos = listDominos.subList(0, (12 * (list.size())));
            GraphicsManager.sizePart = (int)(Application.getInstance().getWidth()/26.5);
        }
        if (rules.contains(Rule.DYNASTY)) {
            Application.getInstance().turns = 3;
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
        newLineDominos(line ? 1 : 0);
        line = !line;
        for (Player player : listKings) {
            currentPlayer = player;
            gManager.setCurrentPlayer(player);
            chooseDomino(player);
        }
        while (listDominos.size() > 0) {
            newTurn();
        }
        selectedDominos.sort(InverseDominoComparator);
        for (Domino domino : selectedDominos) {
            currentPlayer = domino.player;
            gManager.setCurrentPlayer(domino.player);
            placeDomino(domino);
        }
        for (Player player : listPlayers) {
            player.totalScore += calculateScorePlayer(player);
        }
        gManager.showScores(listPlayers);
    }

    private void newTurn() {
        System.out.println(listDominos.size());
        ArrayList<Domino> dominoToPlace = new ArrayList<>(selectedDominos);
        dominoToPlace.sort(InverseDominoComparator);
        newLineDominos(line ? 1 : 0);
        line = !line;
        selectedDominos.clear();
        for (Domino domino : dominoToPlace) {
            currentPlayer = domino.player;
            placeDomino(domino);
            chooseDomino(domino.player);
        }
    }

    private void chooseDomino(Player p) {
        gManager.labelIndications.setText("Choisissez votre Domino.");
        if (p.ia) {
            Domino dominoSelect = IA_Basique.chooseDominoBasique(selectableDominos,p);
            Objects.requireNonNull(dominoSelect).player = p;
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
        gManager.labelIndications.setText("Placez votre Domino.");
        if (domino.player.ia) {
            IA_Basique.placeDominoBasique(domino);
            for (Component comp : gManager.getComponents()){
                if(comp instanceof DominoUI){
                    if (((DominoUI)comp).domino == domino){
                        gManager.remove(comp);
                    }
                }
            }
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
        score = player.board.calculateScore();
        if (specialRules.contains(Rule.HARMONY) && !player.litter) {
            score += 5;
        }
        if (specialRules.contains(Rule.MIDDLEEARTH)) {
            int minY = player.board.getMinY();
            int minX = player.board.getMinX();
            int maxY = player.board.getMaxY();
            int maxX = player.board.getMaxX();
            if (player.board.get(((minX+maxX)/2),((minY+maxY)/2)).type.equals("Chateau")) {
                score += 10;
            }
        }
        return score;
    }

    private final Comparator<Domino> DominoComparator = (Domino m1, Domino m2) -> Integer.compare(m2.number, m1.number);
    private final Comparator<Domino> InverseDominoComparator = Comparator.comparingInt((Domino m) -> m.number);
}