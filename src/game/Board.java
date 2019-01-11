package game;

import javafx.util.Pair;

public class Board {
    DominoPart[][] array;
    public int size;
    Board(int num){
        size = num;
        array = new DominoPart[num][num];
        for (int i = 0; i < num; i++) {
            for (int k = 0; k < num; k++) {
                set(k,i,new DominoPart("vide", 0));
            }
        }
        set((num - 1) / 2,(num - 1) / 2, new DominoPart("Chateau", 0));
    }
    public int getMinX(){
        int minX = 0;
        for (int i = size - 1; i >= 0; i--) {
            for (int k = 0; k < size; k++) {
                if (!get(i,k).type.equals("vide")) {
                    minX = i;
                }
            }
        }
        return minX;
    }

    public int getMinY(){
        int minY = 0;
        for (int i = size - 1; i >= 0; i--) {
            for (int k = 0; k < size; k++) {
                if (!get(k,i).type.equals("vide")) {
                    minY = i;
                }
            }
        }
        return minY;
    }
    public DominoPart get(int x,int y){
        return array[y][x];
    }
    public void set(int x,int y,DominoPart dominoPart){
        array[y][x]= dominoPart;
    }



    public int calculateScore() {
        int score = 0;
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                if (!get(k,i).type.equals("vide") && !get(k,i).counted) {
                    Pair<Integer, Integer> pair = calculateScoreZone(get(k,i), i, k);
                    score += pair.getKey() * pair.getValue();
                    //System.out.println(board[i][k].type + "   -   "+pair.getKey() * pair.getValue());
                }
            }
        }
        for (DominoPart[] dominoParts : array) {
            for (DominoPart dominoPart : dominoParts) {
                dominoPart.counted = false;
            }
        }
        return score;
    }

    //calculate the score of a certain zone , return the number of cases and the number of crowns
    private Pair<Integer, Integer> calculateScoreZone(DominoPart part, int y, int x) {
        int totalArea = 1;
        int totalCrown = part.crown;
        Pair<Integer, Integer> newPair = new Pair<>(0, 0);
        String type = part.type;
        part.counted = true;
        if (x < array[y].length - 1 && array[y][x + 1].type.equals(type) && !array[y][x + 1].counted) {
            newPair = calculateScoreZone(array[y][x + 1], y, x + 1);
        }
        if (x > 0 && array[y][x - 1].type.equals(type) && !array[y][x - 1].counted) {
            newPair = calculateScoreZone(array[y][x - 1], y, x - 1);
        }
        if (y < array.length - 1 && array[y + 1][x].type.equals(type) && !array[y + 1][x].counted) {
            newPair = calculateScoreZone(array[y + 1][x], y + 1, x);
        }
        if (y > 0 && array[y - 1][x].type.equals(type) && !array[y - 1][x].counted) {
            newPair = calculateScoreZone(array[y - 1][x], y - 1, x);
        }
        totalArea += newPair.getKey();
        totalCrown += newPair.getValue();
        return new Pair<>(totalArea, totalCrown);
    }
}
