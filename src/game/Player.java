package game;

import java.awt.Color;

public class Player {
    public enum state {
        IDLE, DOMINOSELECTED, PLACINGDOMINO
    }

    public Color color;
    public String name;
    public state currentState = state.IDLE;
    public Board board;
    public int cumuledScore = 0;
    public boolean litter = false;
    public boolean ia = false;

    public Player(String nom, Color c) {
        newBoard(9);
        name = nom;
        color = c;
    }

    public String toString() {
        return "[" + name + "-" + color + "]";
    }

    //initialize the board and the games dependants values
    public void newBoard(int num) {
        litter = false;
        currentState = state.IDLE;
        board = new Board(num);
        for (int i = 0; i < num; i++) {
            for (int k = 0; k < num; k++) {
                board.set(k,i,new DominoPart("vide", 0));
            }
        }
        board.set((num - 1) / 2,(num - 1) / 2 , new DominoPart("Chateau", 0));
    }
}