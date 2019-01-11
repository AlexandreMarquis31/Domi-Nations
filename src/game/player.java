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
    public dominoPart[][] board;
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
        for (dominoPart[] dominoParts : board) {
            for (dominoPart dominoPart : dominoParts) {
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
        board = new dominoPart[num][num];
        for (int i = 0; i < num; i++) {
            for (int k = 0; k < num; k++) {
                board[i][k] = new dominoPart("vide", 0);
            }
        }
        board[(num - 1) / 2][(num - 1) / 2] = new dominoPart("Chateau", 0);
    }
}