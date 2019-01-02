package UI;

import game.dominoPart;
import player.player;

import javax.swing.*;
import java.awt.*;

public class PlayerUI extends JPanel {
    private int sizePart = 30;
    public player player;
    public int height = 0;
    public int width = 0;

    public PlayerUI(player p) {
        this.player = p;
        if (p != null) {
            this.height = player.board.length * (sizePart + 2) + 2 + 30;
            this.width = player.board[0].length * (sizePart + 2) + 2;
        }
    }

    /**
     * Manually control what's drawn on this JPanel by calling the paintComponent method
     * with a graphics object and painting using that object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.orange);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);
        for (int i = 0; i < player.board.length; i++) {
            for (int k = 0; k < player.board[i].length; k++) {
                g.setColor(player.board[k][i].color);
                g.fillRect((i * (30 + 2)) + 2, (k * (30 + 2)) + 2, 30, 30);
            }
        }
        g.setColor(Color.black);
        g.drawChars(player.name.toCharArray(), 0, player.name.toCharArray().length, 12, height - 12);
        g.setColor(player.color);
        g.fillRect(width - 27, height - 27, 20, 20);
    }
}