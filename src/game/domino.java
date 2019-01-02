package game;

public class domino {
    public int number;
    public dominoPart part1;
    public dominoPart part2;
    public domino(dominoPart part1, dominoPart part2, int num){
        this.number = num;
        this.part1 = part1;
        this.part2 = part2;
    }
    public String toString() {
        return "["+this.part1+","+this.part2+","+this.number+"]";
    }

}
