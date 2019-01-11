package UI;

import game.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerUI extends JPanel {
    private int sizePart;
    Player player;
    DominoPartUI[][] boardUI;

    PlayerUI(Player p, int s) {
        player = p;
        sizePart = s;
        boardUI = new DominoPartUI[p.size][p.size];
        setSize(p.size * (s + 2) + 2, p.size * (s + 2) + 2 + 30);
        setLayout(null);
        setBackground(Color.orange);
        for (int i = 0; i < p.board.length; i++) {
            for (int k = 0; k < p.board[i].length; k++) {
                boardUI[k][i] = new DominoPartUI(p.board[k][i]);
                boardUI[k][i].setBounds((i * (s + 2)) + 2, (k * (s + 2)) + 2, s, s);
                add(boardUI[k][i]);
            }
        }
        JLabel label = new JLabel(p.name);
        label.setBounds(2, getHeight() - 27, 100, 20);
        add(label);
        JPanel color = new JPanel();
        color.setBorder(BorderFactory.createLineBorder(Color.white));
        color.setBackground(p.color);
        color.setBounds(getWidth() - 27, getHeight() - 27, 20, 20);
        add(color);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < player.board.length; i++) {
            for (int k = 0; k < player.board[i].length; k++) {
                boardUI[k][i].dominoPart = player.board[k][i];
            }
        }
    }
}