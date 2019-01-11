package UI;

import game.GameManager;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;

public class MenuUI extends JPanel implements ActionListener {
    private final PlayerSelectUI[] listPlayerUI = new PlayerSelectUI[4];
    private final RulesUI rulesUI;

    public MenuUI(int width, int height) {
        Application.getInstance().turns = 1;
        setSize(new Dimension(width, height));
        setLayout(null);
        JLabel titre = new JLabel("Domi-Nations");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBounds(0, 0, width, height/5);
        titre.setFont(new Font("New Time Roman", Font.PLAIN, height/18));
        add(titre);
        for (int i = 0; i < 4; i++) {
            PlayerSelectUI playerUI = new PlayerSelectUI(i + 1, (int)(width/4.5), (int)(height/2.5));
            playerUI.setLocation(width/80 + (i * (playerUI.getWidth() + width/40)), height/5);
            listPlayerUI[i] = playerUI;
            add(playerUI);
        }

        rulesUI = new RulesUI(width, (int)(height/4.4));
        rulesUI.setLocation(0, (int)(height/1.5));
        add(rulesUI);
        JButton playBut = new JButton("Jouer !");
        playBut.setBounds(width / 2 - height/7, height - height/10, width/4, height/18);
        add(playBut);
        playBut.addActionListener(this);
    }

    @Override
    //launch a game with the corrects parameters
    public void actionPerformed(ActionEvent e) {
        GraphicsManager graphics = new GraphicsManager(getWidth(), getHeight());
        Application.getInstance().setGM(graphics);
        Thread thread = new Thread(() -> {
            ArrayList<Player> playerList = new ArrayList<>();
            for (PlayerSelectUI playerUI : listPlayerUI) {
                if (playerUI.selected) {
                    javafx.scene.paint.Color color = playerUI.colorField.getValue();
                    Player player = new Player(playerUI.nameField.getText(), new Color((float) color.getRed(),
                            (float) color.getGreen(),
                            (float) color.getBlue(),
                            (float) color.getOpacity()));
                    player.ia = playerUI.IASwitch.isSelected();
                    playerList.add(player);
                }
            }
            EnumSet<GameManager.Rule> rules = EnumSet.noneOf(GameManager.Rule.class);
            if (rulesUI.dynastySwitch.isSelected()) rules.add(GameManager.Rule.DYNASTY);
            if (rulesUI.harmonySwitch.isSelected()) rules.add(GameManager.Rule.HARMONY);
            if (rulesUI.middleEarthSwitch.isSelected()) rules.add(GameManager.Rule.MIDDLEEARTH);
            if (rulesUI.duelSwitch.isSelected()) rules.add(GameManager.Rule.DUEL);
            GameManager game = new GameManager(graphics);
            if (Application.getInstance().turns > 1) {
                Application.getInstance().turns--;
            }
            game.newGame(playerList, rules);
        });
        thread.start();
    }

}
