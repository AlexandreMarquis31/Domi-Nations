package UI;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;

import javax.swing.*;
import java.awt.*;


class PlayerSelectUI extends JPanel {
    private final JToggleButton playerSwitch = new JToggleButton("Désactivé");
    boolean selected = false;
    final JRadioButton IASwitch = new JRadioButton("Utiliser une IA");
    final JTextField nameField;
    ColorPicker colorField;

    PlayerSelectUI(int num, int width, int height) {
        setLayout(null);
        setSize(width, height);
        JLabel titre = new JLabel("Joueur " + num);
        titre.setFont(new Font("New Time Roman", Font.BOLD, height/14));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBounds(0, 0, width, height / 8);
        add(titre);
        JLabel name = new JLabel("Nom du Joueur");
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setBounds(width / 8, height / 8, 6 * width / 8, height / 8);
        add(name);
        nameField = new JTextField("Joueur " + num, 0);
        nameField.setBounds(width / 8, 4 * height / 16, 6 * width / 8, height / 8);
        add(nameField);
        JLabel color = new JLabel("Couleur du Joueur");
        color.setHorizontalAlignment(SwingConstants.CENTER);
        color.setBounds(width / 8, 13 * height / 32, 6 * width / 8, height / 8);
        add(color);
        JFXPanel fxPanel = new JFXPanel();
        fxPanel.setBackground(Color.red);
        fxPanel.setBounds(width / 8, 17 * height / 32, width * 6 / 8, 2 * height / 20);
        add(fxPanel);
        Platform.runLater(() -> {
            Group root = new Group();
            Scene scene = new Scene(root);
            colorField = new ColorPicker();
            colorField.setMaxSize(getWidth() * 6 / 8, 2 * getHeight() / 20);
            colorField.setMinSize(getWidth() * 6 / 8, 2 * getHeight() / 20);
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
        playerSwitch.setBounds(width / 8, 7 * height / 8, 6 * width / 8, height / 8);
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

    }
}
