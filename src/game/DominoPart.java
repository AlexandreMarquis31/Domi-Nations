package game;

public class DominoPart {
    public final int crown;
    public final String type;
    boolean counted = false;

    DominoPart(String type, int crown) {
        this.type = type;
        this.crown = crown;
    }

    public String toString() {
        return "[" + this.type + "," + this.crown + "]";
    }
}
