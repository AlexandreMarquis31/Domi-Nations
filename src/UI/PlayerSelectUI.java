package UI;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


class PlayerSelectUI extends JPanel {
    private final JToggleButton playerSwitch = new JToggleButton("Désactivé");
    boolean selected = false;
    final JRadioButton IASwitch = new JRadioButton("Utiliser une IA");
    final JTextField nameField;
    ColorPicker colorField;

    PlayerSelectUI(int num, int width, int height) {
        setLayout(null);
        JLabel titre = new JLabel("Joueur " + num);
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        add(titre);
        JLabel name = new JLabel("Nom du Joueur");
        name.setHorizontalAlignment(SwingConstants.CENTER);
        add(name);
        nameField = new JTextField("Joueur " + num, 0);
        add(nameField);
        JLabel color = new JLabel("Couleur du Joueur");
        color.setHorizontalAlignment(SwingConstants.CENTER);
        add(color);
        JFXPanel fxPanel = new JFXPanel();
        fxPanel.setBackground(Color.red);
        add(fxPanel);
        Platform.runLater(() -> {
            Group root = new Group();
            Scene scene = new Scene(root);
            colorField = new ColorPicker();
            colorField.setStyle("-fx-background-radius: 0px;");
            switch (num) {
                case 1:
                    colorField.setValue(new javafx.scene.paint.Color(1, 0, 0, 1));
                    break;
                case 2:
                    colorField.setValue(new javafx.scene.paint.Color(0, 0, 1, 1));
                    break;
                case 3:
                    colorField.setValue(new javafx.scene.paint.Color(0, 0, 0, 1));
                    break;
                default:
                    colorField.setValue(new javafx.scene.paint.Color(1, 1, 1, 1));
                    break;
            }
            root.getChildren().add(colorField);
            fxPanel.setScene(scene);
            fxPanel.repaint();
        });
        IASwitch.setBounds(width / 8, 23 * height / 32, 6 * width / 8, height / 8);
        add(IASwitch);
        if (num < 3) {
            playerSwitch.setText("Activé");
            playerSwitch.setSelected(true);
            selected = true;
        }
        playerSwitch.addActionListener(event -> {
            if (playerSwitch.getText().equals("Activé")) {
                playerSwitch.setText("Désactivé");
                playerSwitch.setSelected(false);
            } else if (playerSwitch.getText().equals("Désactivé")) {
                playerSwitch.setText("Activé");
                playerSwitch.setSelected(true);
            }
            selected = !selected;
        });
        add(playerSwitch);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                removeComponentListener(this);
                titre.setFont(new Font("New Time Roman", Font.PLAIN, getHeight()/14));
                titre.setBounds(0, 0, getWidth(), getHeight() / 8);
                name.setBounds(getWidth() / 8, getHeight() / 8, 6 * getWidth() / 8, getHeight() / 8);
                nameField.setBounds(getWidth() / 8, 4 * getHeight() / 16, 6 * getWidth() / 8, getHeight() / 8);
                name.setFont(new Font("New Time Roman", Font.PLAIN, getHeight()/20));
                color.setBounds(getWidth() / 8, 13 * getHeight() / 32, 6 * getWidth() / 8, getHeight() / 8);
                color.setFont(new Font("New Time Roman", Font.PLAIN, getHeight()/20));
                IASwitch.setFont(new Font("New Time Roman", Font.PLAIN, getHeight()/20));
                fxPanel.setBounds(getWidth() / 8, 17 * getHeight() / 32, getWidth() * 6 / 8, 2 * getHeight() / 20);
                Platform.runLater(() -> {
                    colorField.setMaxSize(getWidth() * 6 / 8, 2 * getHeight() / 20);
                    colorField.setMinSize(getWidth() * 6 / 8, 2 * getHeight() / 20);
                    fxPanel.repaint();
                });
                IASwitch.setBounds(getWidth() / 8, 23 * getHeight() / 32, 6 * getWidth() / 8, getHeight() / 8);
                playerSwitch.setBounds(getWidth() / 8, 7 * getHeight() / 8, 6 * getWidth() / 8, getHeight() / 8);
                addComponentListener(this);
            }
            @Override
            public void componentMoved(ComponentEvent e) {
            }
            @Override
            public void componentShown(ComponentEvent e) {
            }
            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        setSize(width, height);

    }
}
