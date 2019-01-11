package game;

public class dominoPart {
    public int crown;
    public String type;
    boolean counted = false;

    dominoPart(String type, int crown) {
        this.type = type;
        this.crown = crown;
    }

    public String toString() {
        return "[" + this.type + "," + this.crown + "]";
    }
}
