package UI;

import javax.swing.*;
import java.awt.*;

public class TrashUI extends JPanel {
    TrashUI() {
        setBackground(Color.red);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel label = new JLabel("Poubelle");
        label.setFont(new Font("Time New Roman", Font.PLAIN, 16));
        label.setForeground(Color.white);
        add(label, gbc);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
