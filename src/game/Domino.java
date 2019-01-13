package game;

public class Domino {
    final int number;
    public final DominoPart part1;
    public final DominoPart part2;
    public int turnPriority;
    public Player player = null;

    Domino(DominoPart part1, DominoPart part2, int num) {
        this.number = num;
        this.part1 = part1;
        this.part2 = part2;
    }

    public boolean canBePlaced(int x1, int y1, int x2, int y2, Board board) {
        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || y1 > board.size || y2 > board.size || x1 > board.size || x2 > board.size) {
            return false;
        }
        if (!board.get(x1, y1).type.equals("vide") || !board.get(x2, y2).type.equals("vide")) {
            return false;
        }
        if (!sameTypeAround(x1, y1, x2, y2, board)) {
            return false;
        }
        return !biggerThanBoard(x1, y1, x2, y2, board);
    }

    private boolean sameTypeAround(int x1, int y1, int x2, int y2, Board board) {
        return sameTypePart(x2, y2, board, part2) || (sameTypePart(x1, y1, board, part1));
    }

    //verify that a same part is around the position given on the board
    private boolean sameTypePart(int x, int y, Board board, DominoPart part) {
        if (y + 1 < board.size && (board.get(x, y + 1).type.equals(part.type) || board.get(x, y + 1).type.equals("Chateau"))) {
            return true;
        }
        if (x + 1 < board.size && (board.get(x + 1, y).type.equals(part.type) || board.get(x + 1, y).type.equals("Chateau"))) {
            return true;
        }
        if (x > 0 && (board.get(x - 1, y).type.equals(part.type) || board.get(x - 1, y).type.equals("Chateau"))) {
            return true;
        }
        return y > 0 && (board.get(x, y - 1).type.equals(part.type) || board.get(x, y - 1).type.equals("Chateau"));
    }

    //verify that a news dominoParts a the 2 positions will not be out of the rule's board boundaries
    private static boolean biggerThanBoard(int x1, int y1, int x2, int y2, Board board) {
        int minX = board.getMinX();
        int minY = board.getMinY();
        minX = Math.min(minX, x1);
        minX = Math.min(minX, x2);
        minY = Math.min(minY, y1);
        minY = Math.min(minY, y2);
        if (x1 > minX + (board.size) / 2 || x2 > minX + (board.size) / 2 || y1 > minY + (board.size) / 2 || y2 > minY + (board.size) / 2) {
            return true;
        }
        for (int i = 0; i < board.size; i++) {
            for (int k = 0; k < board.size; k++) {
                if ((i < minY || i > minY + (board.size) / 2 || k < minX || k > minX + (board.size) / 2) && !board.get(k, i).type.equals("vide")) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        return "[" + this.part1 + "," + this.part2 + "," + this.number + "]";
    }

}
