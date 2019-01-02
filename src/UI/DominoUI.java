package UI;

import game.domino;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;


public class DominoUI extends JPanel {
    private int sizePart = 30;
    public domino domino;
    public int height = 0;
    public int width = 0;

    public DominoUI(domino d) {
        this.domino = d;
        if (d != null) {
            this.height = 30 + 2;
            this.width = 2 * 30 + 4;
        }
        MouseInputAdapter mi = new MouseInputAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.println("aaaaaaa");
            }
        };
        this.addMouseMotionListener(mi);
        this.addMouseListener(mi);
    }

    /**
     * Manually control what's drawn on this JPanel by calling the paintComponent method
     * with a graphics object and painting using that object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.orange);
        g.fillRect(0, 0, width, height);
        g.setColor(domino.part1.color);
        g.fillRect(1, 1, 30, 30);
        g.setColor(domino.part2.color);
        g.fillRect(3 + 30, 1, 30, 30);
        if (domino.part1.crown > 0) {
            g.drawImage(CrownUI.getInstance(), 30 + 1 - 15, 1, null);
            g.setColor(getContrastColor(domino.part1.color));
            g.setFont(new Font(" TimesRoman ", 0, 10));
            g.drawChars(String.valueOf(domino.part1.crown).toCharArray(), 0, String.valueOf(domino.part1.crown).toCharArray().length, 30 + 1 - 15 - 8, 10);
        }
        if (domino.part2.crown > 0) {
            g.drawImage(CrownUI.getInstance(), 60 + 3 - 15, 1, null);
            g.setColor(getContrastColor(domino.part2.color));
            g.setFont(new Font(" TimesRoman ", 0, 10));
            g.drawChars(String.valueOf(domino.part2.crown).toCharArray(), 0, String.valueOf(domino.part2.crown).toCharArray().length, 60 + 3 - 15 - 8, 10);
        }

    }

    private static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }
}

