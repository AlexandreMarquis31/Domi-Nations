package UI;

import game.gameManager;
import game.player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;

public class MenuUI extends JPanel implements ActionListener {
    private PlayerSeclectUI[] listPlayerUI = new PlayerSeclectUI[4];
    private RulesUI rulesUI;

    public MenuUI(int width, int height) {
        Application.getInstance().manches = 1;
        setSize(new Dimension(width, height));
        setLayout(null);
        JLabel titre = new JLabel("Domi-Nations");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBounds(0, 0, width, 140);
        titre.setFont(new Font("New Time Roman", Font.PLAIN, 40));
        add(titre);
        for (int i = 0; i < 4; i++) {
            PlayerSeclectUI playerUI = new PlayerSeclectUI(i + 1, 180, 300);
            playerUI.setLocation(10 + (i * (playerUI.getWidth() + 20)), 140);
            listPlayerUI[i] = playerUI;
            add(playerUI);
        }

        rulesUI = new RulesUI(width, 160);
        rulesUI.setLocation(0, 460);
        add(rulesUI);
        JButton playBut = new JButton("Jouer !");
        playBut.setBounds(width / 2 - 100, height - 80, 200, 40);
        add(playBut);
        playBut.addActionListener(this);
    }

    @Override
    //launch a game with the corrects parameters
    public void actionPerformed(ActionEvent e) {
        graphicsManager graphics = new graphicsManager(getWidth(), getHeight());
        Application.getInstance().setGM(graphics);
        Thread thread = new Thread(() -> {
            ArrayList<player> playerList = new ArrayList<>();
            for (PlayerSeclectUI playerUI : listPlayerUI) {
                if (playerUI.selected) {
                    javafx.scene.paint.Color color = playerUI.colorField.getValue();
                    player player = new player(playerUI.nameField.getText(), new Color((float) color.getRed(),
                            (float) color.getGreen(),
                            (float) color.getBlue(),
                            (float) color.getOpacity()));
                    player.ia = playerUI.IASwitch.isSelected();
                    playerList.add(player);
                }
            }
            EnumSet<gameManager.Rule> rules = EnumSet.noneOf(gameManager.Rule.class);
            if (rulesUI.dynastySwitch.isSelected()) rules.add(gameManager.Rule.DYNASTY);
            if (rulesUI.harmonySwitch.isSelected()) rules.add(gameManager.Rule.HARMONY);
            if (rulesUI.middleEarthSwitch.isSelected()) rules.add(gameManager.Rule.MIDDLEEARTH);
            if (rulesUI.duelSwitch.isSelected()) rules.add(gameManager.Rule.DUEL);
            gameManager game = new gameManager(graphics);
            if (Application.getInstance().manches > 1) {
                Application.getInstance().manches--;
            }
            game.newGame(playerList, rules);
        });
        thread.start();
    }

}
