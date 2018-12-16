package game;

public class dominoPart {
    private boolean crown ;
    private String type;
    public dominoPart(String type, boolean crown){
        this.type = type;
        this.crown = crown;
    }
    public String toString() {
        return "["+this.type+","+this.crown+"]";
    }
}
