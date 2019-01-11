package game;

public class DominoPart {
    public int crown;
    public String type;
    boolean counted = false;

    DominoPart(String type, int crown) {
        this.type = type;
        this.crown = crown;
    }

    public String toString() {
        return "[" + this.type + "," + this.crown + "]";
    }
}
