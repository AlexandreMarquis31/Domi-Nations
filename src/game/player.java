package game;

import java.awt.Color;

public class player {
    public enum state {
        IDLE, DOMINOSELECTED, PLACINGDOMINO
    }

    public int size;
    public Color color;
    public String name;
    public state currentState = state.IDLE;
    public DominoPart[][] board;
    public int cumuledScore = 0;
    public boolean litter = false;
    public boolean ia = false;

    public player(String nom, Color c) {
        newBoard(9);
        name = nom;
        color = c;
    }

    public String toString() {
        return "[" + name + "-" + color + "]";
    }

    public void showBoard() {
        for (DominoPart[] dominoParts : board) {
            for (DominoPart dominoPart : dominoParts) {
                System.out.print(dominoPart);
            }
            System.out.println();
        }
    }

    //initialize the board and the games dependants values
    public void newBoard(int num) {
        litter = false;
        currentState = state.IDLE;
        size = num;
        board = new DominoPart[num][num];
        for (int i = 0; i < num; i++) {
            for (int k = 0; k < num; k++) {
                board[i][k] = new DominoPart("vide", 0);
            }
        }
        board[(num - 1) / 2][(num - 1) / 2] = new DominoPart("Chateau", 0);
    }
}