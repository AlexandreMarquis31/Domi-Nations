package UI;

import game.DominoPart;

import javax.swing.*;
import java.awt.*;

public class DominoPartUI extends JPanel {
    DominoPart dominoPart;

    DominoPartUI(DominoPart part) {
        dominoPart = part;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (dominoPart.type) {
            case "Champs":
                setBackground(new Color(255, 255, 0));
                break;
            case "Mer":
                setBackground(Color.blue);
                break;
            case "Foret":
                setBackground(new Color(0, 102, 0));
                break;
            case "Prairie":
                setBackground(new Color(0, 223, 15));
                break;
            case "Mine":
                setBackground(Color.black);
                break;
            case "Montagne":
                setBackground(new Color(102, 51, 0));
                break;
            case "Chateau":
                setBackground(new Color(100, 100, 100));
                break;
            default:
                setBackground(Color.white);
                break;
        }
        if (dominoPart.crown > 0) {
            g.drawImage(CrownUI.getInstance(), getWidth() - 15, 1, null);
            g.setColor(getContrastColor(getBackground()));
            g.setFont(new Font(" TimesRoman ", Font.PLAIN, 10));
            g.drawChars(String.valueOf(dominoPart.crown).toCharArray(), 0, String.valueOf(dominoPart.crown).toCharArray().length, getWidth() - 8, getHeight() - 1);
        }
    }

    private static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }
}
