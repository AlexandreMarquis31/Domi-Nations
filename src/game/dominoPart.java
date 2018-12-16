package game;

public class dominoPart {
    public boolean crown ;
    public String type;
    public dominoPart(String type, boolean crown){
        this.type = type;
        this.crown = crown;
    }
    public String toString() {
        return "["+this.type+","+this.crown+"]";
    }
}
