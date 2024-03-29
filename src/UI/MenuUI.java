package UI;

import game.GameManager;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.EnumSet;

public class MenuUI extends JPanel implements ActionListener {
    private final PlayerSelectUI[] listPlayerUI = new PlayerSelectUI[4];
    private final RulesUI rulesUI;

    public MenuUI(int width, int height) {
        Application.getInstance().turns = 1;
        setLayout(null);
        JLabel titre = new JLabel("Domi-Nations");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        add(titre);
        for (int i = 0; i < 4; i++) {
            PlayerSelectUI playerUI = new PlayerSelectUI(i + 1, (int) (width / 4.5), (int) (height / 2.5));
            listPlayerUI[i] = playerUI;
            add(playerUI);
        }

        rulesUI = new RulesUI(width, (int) (height / 4.4));
        add(rulesUI);
        JButton playBut = new JButton("Jouer !");
        add(playBut);
        playBut.addActionListener(this);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                removeComponentListener(this);
                titre.setBounds(0, 0, getWidth(), getHeight() / 5);
                titre.setFont(new Font("New Time Roman", Font.PLAIN, getHeight() / 18));
                for (int i = 0; i < 4; i++) {
                    PlayerSelectUI playerUI = listPlayerUI[i];
                    playerUI.setBounds(getWidth() / 80 + (i * (playerUI.getWidth() + getWidth() / 40)), getHeight() / 5, (int) (getWidth() / 4.5), (int) (getHeight() / 2.5));
                }
                playBut.setBounds(getWidth() / 2 - getHeight() / 7, getHeight() - getHeight() / 10, getWidth() / 4, getHeight() / 18);
                rulesUI.setBounds(0, (int) (getHeight() / 1.5), getWidth(), (int) (getHeight() / 4.4));
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
        setSize(new Dimension(width, height));
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
