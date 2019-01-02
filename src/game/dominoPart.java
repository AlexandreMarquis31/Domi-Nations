package game;

import java.awt.*;

public class dominoPart {
    public int crown ;
    public String type;
    public Color color;
    public dominoPart(String type, int crown){
        this.type = type;
        this.crown = crown;
        switch(type){
            case "Champs":
                this.color = new Color(108,195,114);
                break;
            case "Mer":
                this.color = Color.blue;
                break;
            case "Foret":
                this.color = new Color(0,102,0);
                break;
            case "Prairie":
                this.color = new Color(0,223,15);
                break;
            case "Mine":
                this.color = Color.black;
                break;
            case "Montagne":
                this.color = new Color(102,51,0);
                break;
            default:
                this.color = Color.white;
                break;
        }
    }
    public String toString() {
        return "["+this.type+","+this.crown+"]";
    }
}
