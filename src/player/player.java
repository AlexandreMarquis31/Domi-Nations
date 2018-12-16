package player;
import java.awt.Color;
import game.dominoPart;

public class player {
    private Color color;
    public String name;
    private int score;
    public int kings;
    public dominoPart[][] board = new dominoPart[9][9];
    public player(String nom, Color color){
        this.name = nom;
        this.color = color;
        this.score = 0;
        this.kings = 1;
        for (int i = 0; i< board.length; i++){
            for (int k = 0; k <board[i].length; k++){
                board[i][k] = new dominoPart("vide",false);
            }
        }
    }
    public String toString(){
        return "["+name+"-"+color+"]";
    }
    public void showBoard(){
        System.out.println("a");
        for (dominoPart[] dominoParts : board) {
            for (dominoPart dominoPart : dominoParts) {
                System.out.print(dominoPart);
            }
            System.out.println();
        }
    }
}
