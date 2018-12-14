package player;
import java.awt.Color;

public class player {
    public Color color;
    String nom;
    int score;
    public int kings;

    public player(String nom, Color color){
        this.nom = nom;
        this.color = color;
        this.score = 0;
        this.kings = 1;
    }
    public String toString(){
        return "["+this.nom+"-"+this.color+"]";
    }
}
