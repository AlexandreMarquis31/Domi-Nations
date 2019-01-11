package game;

public class domino {
    int number;
    public dominoPart part1;
    public dominoPart part2;
    public int turnPriority;
    public player player = null;

    domino(dominoPart part1, dominoPart part2, int num) {
        this.number = num;
        this.part1 = part1;
        this.part2 = part2;
    }

    public boolean canBePlaced(int x1, int y1, int x2, int y2, dominoPart[][] board) {
        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || y1 > board.length || y2 > board.length || x1 > board[0].length || x2 > board[0].length) {
            return false;
        }
        if (!board[y1][x1].type.equals("vide") || !board[y2][x2].type.equals("vide")) {
            return false;
        }
        if (!sameTypeArounds(x1, y1, x2, y2, board)) {
            return false;
        }
        if (biggerThanBoard(x1, y1, x2, y2, board)) {
            return false;
        }
        return true;
    }

    private boolean sameTypeArounds(int x1, int y1, int x2, int y2, dominoPart[][] board) {
        return sameTypePart(x2, y2, board, part2) || (sameTypePart(x1, y1, board, part1));
    }

    //verify that a same part is around the position given on the board
    private boolean sameTypePart(int x, int y, dominoPart[][] board, dominoPart part) {
        if (y + 1 < board.length && (board[y + 1][x].type.equals(part.type) || board[y + 1][x].type.equals("Chateau"))) {
            return true;
        }
        if (x + 1 < board[y].length && (board[y][x + 1].type.equals(part.type) || board[y][x + 1].type.equals("Chateau"))) {
            return true;
        }
        if (x - 1 > 0 && (board[y][x - 1].type.equals(part.type) || board[y][x - 1].type.equals("Chateau"))) {
            return true;
        }
        if (y - 1 > 0 && (board[y - 1][x].type.equals(part.type) || board[y - 1][x].type.equals("Chateau"))) {
            return true;
        }
        return false;
    }

    //verify that a news dominoParts a the 2 positions will not be out of the rule's board boundaries
    static boolean biggerThanBoard(int x1, int y1, int x2, int y2, dominoPart[][] board) {
        int minX = 0;
        int minY = 0;
        for (int i = board.length - 1; i >= 0; i--) {
            for (int k = 0; k < board.length; k++) {
                if (!board[i][k].type.equals("vide")) {
                    minY = i;
                }
            }
        }
        for (int i = board.length - 1; i >= 0; i--) {
            for (dominoPart[] dominoParts : board) {
                if (!dominoParts[i].type.equals("vide")) {
                    minX = i;
                }
            }
        }
        minX = Math.min(minX, x1);
        minX = Math.min(minX, x2);
        minY = Math.min(minY, y1);
        minY = Math.min(minY, y2);
        if (x1 > minX + (board.length) / 2 || x2 > minX + (board.length) / 2 || y1 > minY + (board.length) / 2 || y2 > minY + (board.length) / 2) {
            return true;
        }
        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board.length; k++) {
                if ((i < minY || i > minY + (board.length) / 2 || k < minX || k > minX + (board.length) / 2) && !board[i][k].type.equals("vide")) {
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
