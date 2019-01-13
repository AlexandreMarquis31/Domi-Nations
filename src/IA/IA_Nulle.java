package IA;

import game.Domino;

import java.util.List;

public class IA_Nulle {
    static public Domino IAChooseDomino(List<Domino> list){
        for (Domino domino : list){
            if (domino.player == null) return domino;
        }
        return null;
    }
    static public boolean IAPlaceDomino(Domino domino){
        for (int i = 0; i<domino.player.board.size -1  ; i++){
            for (int k = 0; k<domino.player.board.size ; k++){
                if (domino.canBePlaced(i,k,i+1,k,domino.player.board)){
                    domino.player.board.set(i,k,domino.part1);
                    domino.player.board.set(i+1,k,domino.part2);
                    return true;
                }
            }
        }
        for (int i = 0; i<domino.player.board.size ; i++){
            for (int k = 0; k<domino.player.board.size - 1 ; k++){
                if (domino.canBePlaced(i,k,i,k+1,domino.player.board)){
                    domino.player.board.set(i,k,domino.part1);
                    domino.player.board.set(i,k+1,domino.part2);
                    return true;
                }
            }
        }
        for (int i = 0; i<domino.player.board.size -1 ; i++){
            for (int k = 0; k<domino.player.board.size ; k++){
                if (domino.canBePlaced(i+1,k,i,k,domino.player.board)){
                    domino.player.board.set(i+1,k,domino.part1);
                    domino.player.board.set(i,k,domino.part2);
                    return true;
                }
            }
        }
        for (int i = 0; i<domino.player.board.size ; i++){
            for (int k = 0; k<domino.player.board.size -1  ; k++){
                if (domino.canBePlaced(i,k+1,i,k,domino.player.board)){
                    domino.player.board.set(i,k+1,domino.part1);
                    domino.player.board.set(i,k,domino.part2);
                    return true;
                }
            }
        }
        domino.player.litter = true;
        return false;
    }

}
