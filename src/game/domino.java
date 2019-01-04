package game;

import player.player;

public class domino {
    int number;
    public dominoPart part1;
    public dominoPart part2;
    public int turnPriority ;
    public player player = null;
    domino(dominoPart part1, dominoPart part2, int num) {
        this.number = num;
        this.part1 = part1;
        this.part2 = part2;
    }

    public boolean canBePlaced(int x1 , int y1 , int x2 , int y2, dominoPart[][] board){
        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || y1 > board.length || y2 > board.length || x1 > board[0].length || x2 > board[0].length ){
            return false;
        }
        if (!board[y1][x1].type.equals("vide") || !board[y2][x2].type.equals("vide")){
            return false;
        }
        if(!sameTypeArounds(x1,y1,x2,y2,board)){
            return false;
        }
        if(biggerThanBoard(x1,y1,x2,y2,board)){
            return false;
        }
        return true;
    }
    private boolean sameTypeArounds(int x1 , int y1 , int x2 , int y2, dominoPart[][] board){
        return sameTypePart(x2, y2, board, part2) || (sameTypePart(x1, y1, board, part1)) ;
    }

    private boolean sameTypePart(int x, int y, dominoPart[][] board, dominoPart part) {
        if(y+1 < board.length && (board[y+1][x].type.equals(part.type) || board[y+1][x].type.equals("Chateau"))){
            return true;
        }
        if(x+1 < board[y].length &&  (board[y][x+1].type.equals(part.type) || board[y][x+1].type.equals("Chateau")) ){
            return true;
        }
        if(x-1 > 0 && (board[y][x-1].type.equals(part.type) || board[y][x-1].type.equals("Chateau"))){
            return true;
        }
        if(y-1 >0 && (board[y-1][x].type.equals(part.type) || board[y-1][x].type.equals("Chateau")) ){
            return true;
        }
        return false;
    }

    private boolean biggerThanBoard(int x1 , int y1 , int x2 , int y2, dominoPart[][] board){
        for (int i = 0; i< x1-5 ; i++ ){
            if (!board[y1][i].type.equals("vide")){
                return true;
            }
        }
        for (int i = x1+5; i< board[y1].length ; i++){
            if (!board[y1][i].type.equals("vide")){
                return true;
            }
        }
        for (int i = 0; i< x2-5 ; i++ ){
            if (!board[y2][i].type.equals("vide")){
                return true;
            }
        }
        for (int i = x2+5; i< board[y2].length; i++){
            if (!board[y2][i].type.equals("vide")){
                return true;
            }
        }
        for (int i = 0; i< y1-5 ; i++ ){
            if (!board[i][x1].type.equals("vide")){
                return true;
            }
        }
        for (int i = y1+5; i< board.length ; i++){
            if (!board[i][x1].type.equals("vide")){
                return true;
            }
        }
        for (int i = 0; i< y2-5 ; i++ ){
            if (!board[i][x2].type.equals("vide")){
                return true;
            }
        }
        for (int i = y2+5; i< board.length ; i++){
            if (!board[i][x2].type.equals("vide")){
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "[" + this.part1 + "," + this.part2 + "," + this.number + "]";
    }

}
