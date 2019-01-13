package UI;

import javax.swing.*;
import java.awt.*;

class TrashUI extends JPanel {
    TrashUI() {
        setBackground(Color.red);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel label = new JLabel("Poubelle");
        label.setFont(new Font("Time New Roman", Font.PLAIN, Application.getInstance().getHeight() / 44));
        label.setForeground(Color.white);
        add(label, gbc);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
