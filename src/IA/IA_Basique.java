package IA;

import game.Domino;
import game.DominoPart;
import game.GameManager;
import game.Player;
import game.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IA_Basique {

    static public ArrayList<Integer> scoreEtPositionDominoBasique (Domino domino, Player player){
        ArrayList<ArrayList<Integer>> bestPositionsWithScore = new ArrayList<>();
        System.out.println("SALE PUTE");
        System.out.println("player = " +player);
        System.out.println("player.board = "+player.board);
        for (int i = 0; i<player.board.size -1  ; i++){
            for (int k = 0; k<player.board.size ; k++){
                if (domino.canBePlaced(i,k,i+1,k,player.board)){
                    Board boardCopy = player.board.copy();
                    boardCopy.set(i,k,domino.part1);
                    boardCopy.set(i+1,k,domino.part2);
                    ArrayList<Integer> positionEtScore = new ArrayList<>();
                    positionEtScore.addAll(Arrays.asList(i,k,i+1,k,boardCopy.calculateScore()));
                    bestPositionsWithScore.add(positionEtScore);

                }
            }
        }
        for (int i = 0; i<player.board.size ; i++){
            for (int k = 0; k<player.board.size - 1 ; k++){
                if (domino.canBePlaced(i,k,i,k+1,player.board)){
                    Board boardCopy = player.board.copy();
                    boardCopy.set(i,k,domino.part1);
                    boardCopy.set(i,k+1,domino.part2);
                    int score = boardCopy.calculateScore();
                    ArrayList<Integer> positionEtScore = new ArrayList<>();
                    positionEtScore.addAll(Arrays.asList(i,k,i,k+1,score));
                    bestPositionsWithScore.add(positionEtScore);
                }
            }
        }
        for (int i = 0; i<player.board.size -1 ; i++){
            for (int k = 0; k<player.board.size ; k++){
                if (domino.canBePlaced(i+1,k,i,k,player.board)){
                    Board boardCopy = player.board.copy();
                    boardCopy.set(i+1,k,domino.part1);
                    boardCopy.set(i,k,domino.part2);
                    int score = boardCopy.calculateScore();
                    ArrayList<Integer> positionEtScore = new ArrayList<>();
                    positionEtScore.addAll(Arrays.asList(i+1,k,i,k,score));
                    bestPositionsWithScore.add(positionEtScore);
                }
            }
        }
        for (int i = 0; i<player.board.size ; i++){
            for (int k = 0; k<player.board.size -1  ; k++){
                if (domino.canBePlaced(i,k+1,i,k,player.board)){
                    Board boardCopy = player.board.copy();
                    boardCopy.set(i,k+1,domino.part1);
                    boardCopy.set(i,k,domino.part2);
                    int score = boardCopy.calculateScore();
                    ArrayList<Integer> positionEtScore = new ArrayList<>();
                    positionEtScore.addAll(Arrays.asList(i,k+1,i,k,score));
                    bestPositionsWithScore.add(positionEtScore);
                }
            }
        }
        int i = 0;
        while (i < bestPositionsWithScore.size() - 1) { //on trie la liste en fonction du score
            if (bestPositionsWithScore.get(i).get(4) < bestPositionsWithScore.get(i + 1).get(4)) {
                ArrayList<Integer> copy = new ArrayList<>(bestPositionsWithScore.get(i));
                bestPositionsWithScore.remove(new ArrayList<>(bestPositionsWithScore.get(i)));
                bestPositionsWithScore.add(copy);
                i--;
            }
            i = i+1;
        }
        if(bestPositionsWithScore.size()==0){
            return null;
        }
        return bestPositionsWithScore.get(0); //on return les coordonnées de la meilleure position avec le meilleur score (on prend juste la première position parmi toutes celles qui ont le meilleur score)
    }

    static public boolean placeDominoBasique(Domino domino){
        if(scoreEtPositionDominoBasique(domino, domino.player)==null){
            domino.player.litter=true;
            return false;
        }
        else{
            ArrayList<Integer> bestPositions = new ArrayList<>(scoreEtPositionDominoBasique(domino,domino.player));
            domino.player.board.set(bestPositions.get(0),bestPositions.get(1), domino.part1);
            domino.player.board.set(bestPositions.get(2),bestPositions.get(3), domino.part2);
            return true;
        }
    }

    static public Domino chooseDominoBasique(List<Domino> list, Player player){
        ArrayList<Domino> dominosToChoose = new ArrayList<>();
        ArrayList<Domino> dominosRejected = new ArrayList<>();
        for (Domino domino : list){
            if (domino.player == null && scoreEtPositionDominoBasique(domino,player) != null) {
                dominosToChoose.add(domino);
            }
            if (domino.player == null && scoreEtPositionDominoBasique(domino,player) == null) {
                dominosRejected.add(domino);
            }
        }
        if(dominosToChoose.size()==0)return dominosRejected.get(0);
        int i = 0;
        while (i < dominosToChoose.size() - 1) { //on trie la liste en fonction du score
            if (scoreEtPositionDominoBasique(dominosToChoose.get(i),player).get(4) < scoreEtPositionDominoBasique(dominosToChoose.get(i+1),player).get(4)) {
                Domino copy = dominosToChoose.get(i);
                dominosToChoose.remove(i);
                dominosToChoose.add(copy);
                i--;
            }
            i = i+1;
        }
        return dominosToChoose.get(0);
    }


}




