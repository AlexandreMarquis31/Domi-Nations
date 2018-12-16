package player;
import java.awt.Color;
import game.dominoPart;
import javafx.util.Pair;

public class player {
    private Color color;
    public String name;
    private int score;
    public int kings;
    //public dominoPart[][] board = new dominoPart[9][9];
    public dominoPart[][] board= {
            {new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false)},
            {new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false)},
            {new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("Mer",false),new dominoPart("Mer",true),new dominoPart("Mer",true),new dominoPart("Mer",false),new dominoPart("Terre",false),new dominoPart("Terre",false)},
            {new dominoPart("Mer",true),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("Mer",true),new dominoPart("vide",false),new dominoPart("Terre",false)},
            {new dominoPart("Mer",true),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false)},
            {new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false)},
            {new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("Mont",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false)},
            {new dominoPart("vide",false),new dominoPart("Mont",false),new dominoPart("Mont",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false)},
            {new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("Mont",true),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false),new dominoPart("vide",false)},
    };
    public player(String nom, Color color){
        this.name = nom;
        this.color = color;
        this.score = 0;
        this.kings = 1;
        /*for (int i = 0; i< board.length; i++){
            for (int k = 0; k <board[i].length; k++){
                board[i][k] = new dominoPart("vide",false);
            }
        }*/
        System.out.println(calculateScore());
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
    public int calculateScore(){
        score = 0;
        for (int i = 0; i< board.length; i++){
            for (int k = 0; k <board[i].length; k++){
                if (!board[i][k].type.equals("vide")){
                    Pair<Integer,Integer> pair =calculateScoreZone(board[i][k],i,k);
                    score += pair.getKey()*pair.getValue();
                    System.out.println(pair);
                }
            }
        }
        return score;
    }
    private Pair<Integer,Integer> calculateScoreZone(dominoPart part,int y , int x){
        System.out.println(part+" "+x+" "+y);
        int totalArea = 1;
        int totalCrown = part.crown ? 1 : 0;
        Pair <Integer,Integer> newPair = new Pair<>(0,0);
        String type = part.type;
        part.type = "vide";
        if(x < board[y].length-1 && board[y][x+1].type.equals(type)){
            newPair = calculateScoreZone(board[y][x+1],y,x+1);
        }
        if( x > 0 && board[y][x-1].type.equals(type)){
            newPair = calculateScoreZone(board[y][x-1],y,x-1);
        }
        if(y < board.length-1 && board[y+1][x].type.equals(type)){
            newPair = calculateScoreZone(board[y+1][x],y+1,x);
        }
        if(y > 0 && board[y-1][x].type.equals(type)){
            newPair = calculateScoreZone(board[y-1][x],y-1,x);
        }
        totalArea += newPair.getKey();
        totalCrown += newPair.getValue();
        return new Pair<>(totalArea,totalCrown);
    }
}