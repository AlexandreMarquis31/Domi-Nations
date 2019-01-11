package game;

import java.awt.*;

public class Player {
    public enum state {
        IDLE, DOMINOSELECTED, PLACINGDOMINO
    }

    public final Color color;
    public final String name;
    public state currentState = state.IDLE;
    public Board board;
    public int totalScore = 0;
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
    }
}