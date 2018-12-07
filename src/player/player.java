package player;
import java.awt.Color;

public class player {
    Color color;
    String nom;
    int score;

    public player(String nom, Color color){
        this.nom = nom;
        this.color = color;
        this.score = 0;
    }
}
